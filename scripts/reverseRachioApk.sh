#!/bin/bash
wget https://github.com/skylot/jadx/releases/download/v0.6.1/jadx-0.6.1.zip
unzip jadx-0.6.1.zip
cd jadx/bin/
cp ~/Downloads/Rachio.apk .
./jadx Rachio.apk
