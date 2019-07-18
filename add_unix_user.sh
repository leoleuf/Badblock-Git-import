#!/bin/bash

if [ $# -eq 1 ];
then
    useradd -m -- "$1"
    passwd -- "$1"
    chsh -s /usr/bin/fish -- "$1"
else
    echo "usage: ./add_user.sh <username>"
    exit 1
fi
