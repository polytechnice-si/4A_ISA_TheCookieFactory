# The Cookie Factory (ISA Case study)

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * Continuous integration status: [![Build Status](https://travis-ci.org/polytechnice-si/4A_ISA_TheCookieFactory.svg?branch=develop)](https://travis-ci.org/polytechnice-si/4A_ISA_TheCookieFactory)


This case study is used to illustrate the different technologies involved in the _Introduction to Software Architecture_  course given at Polytech Nice - Sophia Antipolis at the graduate level. This demonstration code requires the following software to run properly:

  * Build & J2E environment configuration: Maven 3
  * J2E implementation language: Java 8
  * .Net implementation language: Mono >3.12


## Product vision

_The Cookie Factory_ (TCF) is a major bakery brand in the USA. The _Cookie on Demand_ (CoD) system is an innovative service offered by TCF to its customer. They can order cookies online thanks to an application, and select when they'll pick-up their order in a given shop. The CoD system ensures to TCF's happy customers that they'll always retrieve their pre-paid warm cookies on time.

The software architecture to be developed in this document will rely on the following stack:

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/big_pict.png"/>
</p>

## Chapters

  1. [Architecture](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Architecture.md)
  2. [Business components with EJB Sessions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/BusinessComponents.md)
  3. [Exposing components as Web Services (SOAP)](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Exposing_SOAP.md)
  4. [Consuming external Web Services (REST)](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Consuming_REST.md)
  5. [Unit testing _versus_ Integration testing](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/IntegrationTesting.md)
  6. [Complete architecture overview](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/VolatileOverview.md)
  7. [Message interceptors to support the NTUI (_Never Trust User Input_) golden rule](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Interceptors.md)
  8. [Making things persistent](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Persistence.md)
  9. [Web user interface using JSF](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/UI_JSF.md) 
  10. [Asynchronous Communication using Messages](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/MessageDrivenBeans.md) 
  11. [Conclusions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Conclusions.md)
  12. _Bonus_: [Using behavioural-driven development to model test scenarios](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/BDD.md)  

__Important remark__: one can notice that the persistence layer (_aka_ the database) is almost the last step of this document. This is done on purpose. Databases are part of a given architecture, but must not be considered as the its essence. The essence of an architecture is the set of supported features, at the business level. Databases are in this context only a way (among others) to store data.

__Technical Information__:

  - [Setting up IntelliJ](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/ides/intelliJ/README.md): How to setup IntelliJ 
  - [Docker](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/docker/README.md): a containerised version of TCF, ready to start.

## How to use this repository
  
  * The `develop` branch (the default one) represents the system under development. 
    * The [`releases/v1.0`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/tree/release/v1.0) branch contains the code that implements the system without persistence;
    * The [`releases/v2.0`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/tree/release/v2.0) branch contains the code that implements the system with a real persistence layer;
    * The [`releases/v2.1`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/tree/release/v2.1) branch contains the code that implements asynchronous communications.
  * Issues can be submitted using the [GitHub ticketing system](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/issues)

### Compilation & Execution

To compile the demonstration (j2e, .Net and client parts), simply run the compilation script. The first compilation can take (a lot of) time, considering that Maven will have to download all the java dependencies necessary to build and run the system (the application server weights 43Mb):

    mosser@azrael $ ./buildAll.sh
    
To run the demonstration, first start the two servers in two different terminals, then start the remote client in a third one:

    # J2E terminal 						(^C to stop)
    mosser@azrael $ cd j2e
    mosser@azrael j2e$ mvn tomee:run
  
    # .Net terminal						(return to stop)
    mosser@azrael $ cd dotNet
    mosser@azrael dotNet$ mono server.exe
    
    # Remote Client						(bye to stop)
    mosser@azrael $ cd client
    mosser@azrael client$ mvn exec:java

## Code information
 
```bash
azrael:4A_ISA_TheCookieFactory mosser$ cloc j2e client dotNet
     141 text files.
     134 unique files.                                          
      18 files ignored.

github.com/AlDanial/cloc v 1.70  T=2.66 s (46.3 files/s, 2410.0 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                           102            993           1370           3024
Maven                            1              7             10            191
XML                              7             21              0            166
C#                               4             44             34            163
Markdown                         3             71              0            152
XHTML                            4             32              2            113
JSP                              1              0              0              4
Bourne Shell                     1              1              0              2
-------------------------------------------------------------------------------
SUM:                           123           1169           1416           3815
-------------------------------------------------------------------------------
```

## Technological Stack

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/tech_stack.png"/>
</p>