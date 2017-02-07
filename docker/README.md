# Docker environment for the CoD System

  - Author: Sébastien Mosser
  - Reviewer: Benjamin Benni
  - Version: 02.2017
  
  
## Motivations

We propose here a _naive_ container-based deployment environment associated to the cookie on demand system. A more complete version should rely on a real DBMS. However, the system is interesting enough to cover basic principles of the Docker system. In this tutorial, we will create 3 containers (the externat partners, the CoD system and the client), and build a composed environment that bind these three images together in a single app.

  - Pre-requisites:
    - docker: 1.13
    - docker-compose: 1.11


## The "External Partners" image (.Net)

The external partner binary is a .Net executable file (`server.exe`), with the following assumptions:

  - It relies on the `mono` environment;
  - when started, it will expose the services on port 9090; 
  - the entry point of the image is the `server.exe` binary;
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
 ---> Running in 38693152aea1
 ---> e27885793f82
Removing intermediate container 38693152aea1
Step 6/6 : CMD /standalone
 ---> Running in 268042a54842
 ---> f92f96d58aa8
Removing intermediate container 268042a54842
Successfully built f92f96d58aa8
```
  
To start the system as a daemon (in _detached_ mode), simply ask docker to do so (`-d`).  We must bind the port exposed by the container to one available on localhost. Here we'll use the very same one, _i.e._, 9090. The `--rm` option asks docker to remove the container after stopping it.

```
azrael:partners mosser$ docker run --rm -d -p 9090:9090 petitroll/tcf-ext
35f22321a2c58f747ab44701ef631373650289c026841c1a10c3f8db672c0d6b
azrael:partners mosser$
```

## The "Cookie on demand" system image (J2E)

The internal implementation of the CoD system will relies on the following assumptions:

  - it will be deployed on TomEE+, with Java 8;
  - we keep the default port for TomEE (8080);
  - the web app used to monitor the application server will be activated;
  - 
  - 