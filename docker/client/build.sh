#!/bin/bash

#Preparing environment
cd ../../client
echo "Compiling the TCF client system"
mvn -q -DskipTests clean package assembly:single
echo "Done"
cp ./target/tcf-client-1.0-SNAPSHOT-jar-with-dependencies.jar ../docker/client/.

# building the docker image
cd ../docker/client/
docker build -t petitroll/tcf-client .

# cleaning up the environment
rm -rf tcf-client-1.0-SNAPSHOT-jar-with-dependencies.jar
