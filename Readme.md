# The Cookie Factory (ISA Case study)

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016

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
  10. [Conclusions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Conclusions.md)

__Important remark__: one can notice that the persistence layer (_aka_ the database) is almost the last step of this document. This is done on purpose. Databases are part of a given architecture, but must not be considered as the its essence. The essence of an architecture is the set of supported features, at the business level. Databases are in this context only a way (among others) to store data.

## How to use this repository
  
  * The `develop` branch (the default one) represents the system under development. 
    * The [`releases/1.0`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/tree/release/v1.0) branch contains the code that implements the system without persistence
    * The [`releases/2.0`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/tree/release/v2.0) branch contains the code that implements the system with a real persistence layer
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
azrael:4A_ISA_TheCookieFactory mosser$ cloc .
     139 text files.
     139 unique files.                                          
      21 files ignored.

http://cloc.sourceforge.net v 1.64  T=0.53 s (225.2 files/s, 11248.6 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                           100            970           1349           2911
Maven                            1              7             10            188
XML                              7             19              0            138
C#                               4             37             13            132
JavaServer Faces                 4             32              0            115
Bourne Shell                     2              4              3             12
JSP                              1              0              0              4
-------------------------------------------------------------------------------
SUM:                           119           1069           1375           3500
-------------------------------------------------------------------------------
```
