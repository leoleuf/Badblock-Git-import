#!/bin/bash

# Crontab

while true; do
cd /home/web/dev-web/badblock-website
php pull.php
sleep 5
done