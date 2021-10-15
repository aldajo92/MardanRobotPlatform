#!/bin/bash

sudo apt-get update && sudo apt-get upgrade
curl -sSL https://get.docker.com | sh

sudo usermod -aG docker ${USER}

sudo apt-get install -y libffi-dev libssl-dev python3-dev python3 python3-pip

sudo pip3 install docker-compose

sudo systemctl enable docker