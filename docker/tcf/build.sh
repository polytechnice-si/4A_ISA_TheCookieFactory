#!/bin/bash

#Preparing environment
cd ../../j2e
echo "Compiling the TCF system"
mvn -q -DskipTests clean package
echo "Done"
cp ./target/tcf-backend.war ../docker/tcf/.

# building the docker image
cd ../docker/tcf/
docker build -t petitroll/tcf-int .

# cleaning up the environment
rm -rf tcf-backend.war
