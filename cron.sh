#!/bin/bash

# Crontab

while true; do
cd /home/web/dev-web/badblock-website
php pull.php

curl https://badblock.fr/api/update-server-graph
curl https://badblock.fr/api/all-posts
sleep 5
done