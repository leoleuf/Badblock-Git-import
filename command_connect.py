#!/usr/bin/python3

import config
import sys
import os

def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)

user = os.getenv("SUDO_USER")

if not user or os.getuid() != 0:
    eprint("this script should be run with sudo")
    sys.exit(1)

if len(sys.argv) != 2:
    eprint("bad usage")
    eprint("usage: <user>@<host>")

    sys.exit(1)

if sys.argv[1] == "list":
    try:
        user = config.get_user(user)

        for (server, suser) in user.allowed_users():
            print('{}@{}'.format(suser.name, server.shortname))

        sys.exit(0)
    except Exception as e:
        eprint(e)
        sys.exit(1)

host = sys.argv[1].lower().split("@")

if len(host) != 2:
    eprint("bad host format; should be user@domain (ex: mc@game02)")
    sys.exit(1)

try:
    user = config.get_user(user)
    (server, suser) = user.get_host(host[1], host[0])

    os.execvp("ssh", ["ssh", "{}@{}".format(suser.name, server.name)])
except Exception as e:
    eprint(e)
    sys.exit(1)
