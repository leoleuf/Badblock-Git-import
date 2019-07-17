import config
import sys
import os
import subprocess

from pathlib import Path

force = False

# parse arguments
if len(sys.argv) == 2 and sys.argv[1] == "--force-reload":
    force = True

elif len(sys.argv) != 1:
    print("usage: (--force-reload)", file=sys.stderr)
    sys.exit(1)

# mount sshfs for each users
for user in config.users:
    root = Path(user.fs_mountpoint)

    root.mkdir(parents = True, exist_ok = True)

    # set owner
    subprocess.call(["chown", user.name, str(root)])
    # disable read/write for other users
    subprocess.call(["chmod", "700", str(root)])

    for (server, suser) in user.allowed_users():
        for remote_path in suser.fs:
            local_path = suser.fs[remote_path].replace("%host%", server.shortname)
            mountpoint = root / local_path

            # if already mounted
            if mountpoint.is_mount():
                if not force:
                    continue

                # if --force-reload, unmount
                subprocess.call(["umount", str(mountpoint)])

            remote = "{}@{}:{}".format(remote_path, suser.name, server.name)

            # create mountpoint
            mountpoint.mkdir(parents = True, exist_ok = True)

            # set owner
            subprocess.call(["chown", user.name, str(root)])

            # mount sshfs
            subprocess.call(["sshfs", remote,
                                        str(mountpoint),
                                        "-o", "allow_other",
                                        "-o", "reconnect"])
