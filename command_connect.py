import config
import sys
import os

def eprint(*args, **kwargs):
    print(*args, file=sys.stderr, **kwargs)

if len(sys.argv) != 3:
    eprint("bad usage")
    eprint("usage: <unix user> <user>@<host>")

    sys.exit(1)

if sys.argv[2] == "list":
    try:
        user = config.get_user(sys.argv[1])

        for (server, suser) in user.allowed_users():
            print('{}@{}'.format(suser.name, server.shortname))

        sys.exit(0)
    except Exception as e:
        eprint(e)
        sys.exit(1)

host = sys.argv[2].lower().split("@")

if len(host) != 2:
    eprint("bad host format; should be user@domain (ex: mc@game02)")
    sys.exit(1)

try:
    user = config.get_user(sys.argv[1])
    (server, suser) = user.get_host(host[1], host[0])

    os.execvp("ssh", ["ssh", "{}@{}".format(suser.name, server.name)])
except Exception as e:
    eprint(e)
    sys.exit(1)
