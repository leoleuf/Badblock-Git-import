#!/bin/bash

# Crontab

while true; do
cd /home/web/dev-web/badblock-website
php pull.php

curl https://dev-web.badblock.fr/api/update-server-graph -u fluor:paypal
sleep 5
done