#!/bin/bash

## Building client
cd client
mvn clean package
cd ..

## Building the j2e system
cd j2e
mvn clean package
cd ..

## Building the .Net system
cd dotNet
mcs src/*.cs -pkg:wcf -out:server.exe
cd ..
