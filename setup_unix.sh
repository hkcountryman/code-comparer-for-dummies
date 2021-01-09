#!/bin/bash

if [[ $(/usr/bin/id -u) -ne 0 ]]; then
    echo "Not running as root"
    exit
fi

cp -r clipcomp_unix /usr/local
cd /usr/local/bin
ln -s /usr/local/clipcomp_unix/clipcomp.sh clipcomp
