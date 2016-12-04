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
sh install_zabbixagent.sh
# Update the kernel to 4.8.8
sh ../upkeep/update_kernel4.8.8.sh
# Reboot
shutdown -r now