# Prepare installation
sh ../upkeep/update.sh
# Installation of basic programs
apt-get install nload htop speedtest-cli sudo pigz
hostname $0
rm /etc/hostname
echo "$0" >> /etc/hostname
# Install Java - JRE8
echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee /etc/apt/sources.list.d/webupd8team-java.list
echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
apt-get update
apt-get install oracle-java8-installer
# Install vRack
cd /opt/
wget https://badblock.fr/sdata/vRack.tar.gz
tar xvf vRack.tar.gz
rm vRack.tar.gz
cd vRack/
sh boot.sh
screen -ls
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