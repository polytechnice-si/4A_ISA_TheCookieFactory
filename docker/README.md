# Docker environment for the CoD System

  - Author: Sébastien Mosser
  - Reviewer: Benjamin Benni
  - Version: 02.2017
  
  
## Motivations

We propose here a _naive_ container-based deployment environment associated to the cookie on demand system. A more complete version should rely on a real DBMS. However, the system is interesting enough to cover basic principles of the Docker system. In this tutorial, we will create 3 containers (the external partners, the CoD system and the client), and build a composed environment that bind these three images together in a single app.

  - Pre-requisites:
    - docker: 1.13
    - docker-compose: 1.11


## Designing the images

### The "External Partners" image (.Net)

The external partner binary is a .Net executable file (`server.exe`), with the following assumptions:

  - It relies on the `mono` environment,
  - when started, it will expose the services on port 9090,
  - the entry point of the image is the `server.exe` binary,
  - it must be run using the `/standalone` option to support daemon-like execution

The [implementation](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/docker/partners/Dockerfile) is then straightforward

```
FROM mono:3.10
COPY ./server.exe ./server.exe
EXPOSE 9090
ENTRYPOINT ["mono", "./server.exe"]
CMD ["/standalone"]
```

To [build the image](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/docker/partners/build.sh), we need first to produce the `server.exe` file, and then ask docker to start the build process. The image will be named `petitroll/tcf-ext`.

```
azrael:partners mosser$ docker build -t petitroll/tcf-ext .
Sending build context to Docker daemon 15.36 kB
Step 1/6 : FROM mono:3.10
 ---> f53523d73104
Step 2/6 : MAINTAINER Sébastien Mosser (mosser@i3s.unice.fr)
 ---> Using cache
 ---> c8bda40342e7
Step 3/6 : COPY ./server.exe ./server.exe
 ---> Using cache
 ---> 3f809560f3e7
Step 4/6 : EXPOSE 9090
 ---> Using cache
 ---> 4a192e0172df
Step 5/6 : ENTRYPOINT mono ./server.exe
 ---> Using cache
 ---> e27885793f82
Step 6/6 : CMD /standalone
 ---> Using cache
 ---> f92f96d58aa8
Successfully built f92f96d58aa8
```
  
To start the system as a daemon (in _detached_ mode), simply ask docker to do so (`-d` option).  We must bind the port exposed by the container to one available on localhost. Here we'll use the very same one, _i.e._, 9090. The `--rm` option asks docker to remove the container after stopping it.

```
azrael:partners mosser$ docker run --rm -d -p 9090:9090 petitroll/tcf-ext
```

### The "Cookie on demand" system image (J2E)

The internal implementation of the CoD system will relies on the following assumptions:

  - it will be deployed on TomEE+, with Java 8,
  - we keep the default port for TomEE (_i.e._ 8080),
  - the web app used to monitor the application server will be activated,
  - the external partner configuration must be declared when starting the container,
  - The system is considered _healthy_ as soon as TomEE is up and running

We start the [image](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/docker/tcf/Dockerfile) on top of the officiel TomEE+ image, configured with Java 8. We basically copy the `war` file inside the image, in the _webapp_ directory.

To activate the monitoring web app, we need to overwrite two files in the default configuration: _(i)_ `tomcat-users.xml` and _(ii)_ `manager-context.xml`. With this configuration, one can access to the managing web app using the `tommy/eemot` identifier, from outside the container.

The configuration that binds the container to the external partners cannot be set at build time. One must be able to change it without rebuilding the image. This link is set in the system inside a file named `bank.properties`, embedded as a resource in the `war` file. To support this requirement, we declare 2 environment variable to store the `bank_host` and `bank_port` default value. At runtime, when the container will be started, we use a shell script ([`start-tcf.sh`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/docker/tcf/resources/start-tcf.sh)) that creates the needed `bank.properties` file, updates the war contents, and then starts TomEE with the updated web app.

```
FROM tomee:8-jdk-7.0.1-plus
WORKDIR /usr/local/tomee/
COPY ./tcf-backend.war ./webapps/.
COPY ./resources/tomcat-user.xml ./conf/tomcat-users.xml
COPY ./resources/manager-context.xml ./webapps/manager/META-INF/context.xml
ENV bank_host=localhost
ENV bank_port=9090
COPY ./resources/start-tcf.sh .
RUN ["chmod", "u+x", "./start-tcf.sh"]
HEALTHCHECK --interval=5s CMD curl --fail http://localhost:8080/ || exit 1
EXPOSE 8080
ENTRYPOINT ["./start-tcf.sh"]
```

We use this docker file to create an image named `petitroll/tcf-int`:

```
azrael:tcf mosser$ docker build -t petitroll/tcf-int .
```

To start the image and overriding the default environment variable, one can use the `-e` option:

```
azrael:tcf mosser$ run --rm -d -p 8080:8080 \
                       -e bank_host='hostName' -e bank_port='portNumber' \
                       petitroll/tcf-int
```

### The Client image (Java)

This [image](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/docker/client/Dockerfile) relies on an OpenJDK implementation (Java 8), and executes the JAR client (a single assembly) with the right option.

The client can load a file on the local filesystem using the `play` command. Thus, we need to declare a `VOLUME` (here named `/host`) one can use to mount the local filesystem inside the container and share files between the host and the container.

```
FROM openjdk:8
COPY ./tcf-client-1.0-SNAPSHOT-jar-with-dependencies.jar ./tcf-client.jar
VOLUME /host
ENTRYPOINT ["java", "-jar", "./tcf-client.jar"]
CMD ["localhost", "8080"]
```

We use this Dockerfile to build an image named `petitroll/tcf-client` image:

```
azrael:client mosser$ docker build -t petitroll/tcf-client .
```

As we declared in the `tcf-client` program an entry point (`ENTRYPOINT`), and the configuration as the starting command (`CMD`), one can invoke this image like a classical command, putting arguments directly on the command line. The image is used in interactive mode (`-it`):

```
azrael:client mosser$ docker run --rm -it -v `pwd`:/host petitroll/tcf-client server 1234

Starting Cookie on Demand by The Cookie Factory
  - Remote server: server
  - Port number:   1234
Interactive shell started. ? for help.

CoD > bye
Exiting Cookie on Demand by The Cookie Factory

azrael:client mosser$ 
```

## Composing the final system

The three images can now be used independently, as standalone components. But one can combine the three images with the right configuration to define a valid deployment for the CoD system.

This is implemented thanks to a [`docker-compose.yml`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/docker/docker-compose.yml) descriptor. 

We basically use the composition descriptor to expose the different ports (internally with `expose` directives, and externally with `ports` directives). We set the different environment variables to ensure that the J2E systems can communicate with the .Net one. One must notice that inside a composition, each container can be identified on the network thanks to its container name.

The client can start even if the J2E system is not up. However, for the sake of demonstration, we declare a precedence rule, stating that the client will only start after the J2E system is considered `healthy`.

To run the system, one can ask`docker-compose` to start in detached mode:

```
azrael:docker mosser$ docker-compose up -d
Creating network "docker_default" with the default driver
Creating tcf_partners
Creating tcf_system
Creating tcf_client
azrael:docker mosser$ docker ps
CONTAINER ID        IMAGE                  COMMAND                  CREATED             STATUS                    PORTS                    NAMES
868f283949a4        petitroll/tcf-client   "java -jar ./tcf-c..."   6 seconds ago       Up 5 seconds                                       tcf_client
9c279659ed94        petitroll/tcf-int      "./start-tcf.sh"         19 seconds ago      Up 18 seconds (healthy)   0.0.0.0:8080->8080/tcp   tcf_system
222e1a0b8989        petitroll/tcf-ext      "mono ./server.exe..."   20 seconds ago      Up 19 seconds             0.0.0.0:9090->9090/tcp   tcf_partners
```

The client is started inside the assembly, and already running. Thus, one can attach and then detach the current context to the container. To attach the current context to a container, the `docker attach` command do the job. When attached, one can detach using the `^P ^Q` key combination.

```
azrael:docker mosser$ docker attach tcf_client
?
  - bye: Exit Cookie on Demand
  - recipes: List all available recipes
  - order: Order some cookies for a given customer (order CUSTOMER QUANTITY RECIPE)
  - play: Play commands stored in a given file (play FILENAME)
  - process: Process a given cart into an order (process CUSTOMER_NAME)
  - register: Register a customer in the CoD backend (register CUSTOMER_NAME CREDIT_CARD_NUMBER)
  - remove: Remove some cookies for a given customer (remove CUSTOMER QUANTITY RECIPE)
  - cart: show the cart contents for a given customer (cart CUSTOMER_NAME)
  - track: track order status (track ORDER_ID)
CoD > recipes
  CHOCOLALALA
  SOO_CHOCOLATE
  DARK_TEMPTATION
CoD > azrael:docker mosser$ 
```
