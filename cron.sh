#!/bin/bash

# Crontab

while true; do

curl https://badblock.fr/api/update-server-graph

sleep 1

curl https://badblock.fr/api/cache/all-posts

sleep 1

curl https://badblock.fr/api/minecraft/players

sleep 1

curl https://badblock.fr/api/stats/json

sleep 1

curl https://badblock.fr/api/cache/all-staff

sleep 1

curl https://badblock.fr/api/cache/all-stats

sleep 1

done