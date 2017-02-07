#!/bin/bash

#Preparing environment
cd ../../dotNet
./compile.sh
cp ./server.exe ../docker/partners/.

# building the docker image
cd ../docker/partners/
docker build -t petitroll/tcf-ext .

# cleaning up the environment
rm -rf server.exe
