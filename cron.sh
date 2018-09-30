#!/bin/bash

# Crontab

while true; do

curl https://badblock.fr/api/update-server-graph
curl https://badblock.fr/api/all-posts

sleep 5
done