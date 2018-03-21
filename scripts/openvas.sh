#!/bin/bash
apt-get update
apt-get install openvas
openvas-setup
openvas-check-setup
openvas-start

#openvas-stop