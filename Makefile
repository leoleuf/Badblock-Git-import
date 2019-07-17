install:
    apt install python3 sshfs fish build-essential htop tree
    cp config.fish /etc/config.fish
	cp connect /usr/bin/connect
	chown root:root /usr/bin/connect
	chmod 755 /usr/bin/connect
	chmod 600 *
	chown root:root *
