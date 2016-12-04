# Prepare installation
sh ../upkeep/update.sh
# Installation of basic programs
sh install_basics.sh
# Install Java - JRE8
sh install_java.sh
# Install vRack
sh install_vrack.sh
# Install Docker
cd /home/
mkdir mc
cd mc/
wget https://badblock.fr/sdata/Docker.tar.gz
tar xvf Docker.tar.gz
rm Docker.tar.gz
nano /home/mc/Docker/dynamic_configuration.json
nano /home/mc/Docker/configuration.json
# Install Zabbix-Agent
apt-get install zabbix-agent
nano /etc/zabbix/zabbix_agentd.conf
# Update the kernel to 4.8.8
cd /boot
wget ftp://ftp.ovh.net/made-in-ovh/bzImage/4.8.8/System.map-4.8.8-xxxx-std-ipv6-64
wget ftp://ftp.ovh.net/made-in-ovh/bzImage/4.8.8/bzImage-4.8.8-xxxx-std-ipv6-64
update-grub
# Reboot
shutdown -r now