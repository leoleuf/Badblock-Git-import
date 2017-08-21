#!/bin/sh
#Automatic pull webhooks
cd /home/web/dev-web/badblock-website/
git pull https://git remote edit origin http://vps446463.ovh.net/Website/badblock-website.git

#update composer dependencies
php composer.phar update
