#!/bin/bash

#Preparing environment
cd ../../dotNet
./compile.sh
cp ./server.exe ../docker/ext/.

# building the docker image
cd ../docker/ext/
docker build -t petitroll/tcf-ext .

# cleaning up the environment
rm -rf server.exe
