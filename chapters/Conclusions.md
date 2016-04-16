# Conclusions 

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Web user interface using JSF](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/UI_JSF.md) 

## Summary

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/big_pict.png"/>
</p>

This reference implementation demonstrates the following points with respect to the Introduction to Software Architecture course objectives:

  - __3-tiers Architecture__
    - [x] Modeling a component-based architecture focused on offered and required functional interfaces;
    - [x] Using SOAP-based Web Services as an interoperable layer to integrate heterogeneous technologies through _Remote Procedure Call_ (RPC): `remote client <--> J2E`;
    - [x] Using REST-based Web Services as an interoperable layer to integrate heterogeneous technologies through _Resource exposition_: `J2E <--> .Net`;
    - [x] Consuming web services (SOAP & Rest) from remote clients (B2C or B2B);

  - __Persistence layer__
    - [x] Using EJB entities to support the implementation of a persistence layer;
    - [x] Define relationships between entities to support complex object composition;
    - [x] Express validation rules at the persistence level to ensure data consistency.

  - __Business Layer__
    - [x] Implementing components using (stateless) EJB Sessions with J2E;
    - [x] Differentiating Unit tests and Integration tests using Maven;
    - [x] Using interceptors to work at the message (_invocation context_) level;

  - __Exposition Layer__
    - [x] Using Web services to support _business-to-business_ integration
    - [x] Define Java Server Faces to implement a Web-based user interface
    - [x] Share data inside and outside a given HTTP session


## Perspectives

  - [ ]  Use "real" application servers to support TCF deployment (Tomcat, IIS)
    - [ ]  See the DevOps course contents
  - [ ]  Use a "real" database server (_e.g._ postgres, MySQL)
    - [ ]  See the DevOps course contents 
  - [ ]  Use an _Enterprise Service Bus_ (ESB) to decouple the J2E system from the .Net one
    - [ ]  Consider to attend the _Service Integration_ course next year (id: SOA- [x] 1). 
