#!/bin/bash

# Crontab

while true; do

curl https://badblock.fr/api/update-server-graph

sleep 10

curl https://badblock.fr/api/cache/all-posts

sleep 10

curl https://badblock.fr/api/minecraft/players

sleep 10

curl https://badblock.fr/api/stats/json

sleep 10

curl https://badblock.fr/api/cache/all-staff

sleep 10

curl https://badblock.fr/api/cache/all-stats

sleep 10

done