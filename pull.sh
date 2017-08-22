#!/bin/sh
#Automatic pull webhooks
cd /home/web/dev-web/badblock-website/
git pull https://web_bot:kPnZSY3DW9gCCnyBxrA2Fm6eAa3tcafe@http://vps446463.ovh.net/Website/badblock-website.git

#update composer dependencies
php composer.phar update
