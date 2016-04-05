# Conclusions [TCF case study]

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016

## Summary

This reference implementation demonstrates the following points with respect to the Introduction to Software Architecture course objectives:

  - Modeling a component-based architecture focused on offered and required functional interfaces;
  - Implementing such components using (stateless) EJB Sessions with J2E;
  - Using SOAP-based Web Services as an interoperable layer to integrate heterogeneous technologies through _Remote Procedure Call_ (RPC): `remote client <--> J2E`;
  - Using REST-based Web Services as an interoperable layer to integrate heterogeneous technologies through _Resource exposition_: `J2E <--> .Net`;
  - Consuming web services (SOAP & Rest) from remote clients (B2C or B2B);
  - Using EJB entities to support the implementation of a persistence layer;
  - Using interceptors to work at the message (_invocation context_) level;
  - Differentiating Unit tests and Integration tests using Maven.
  
## Perspectives

  - Use "real" application servers to support TCF deployment (Tomcat, IIS)
    - See the DevOps course contents
  - Use a "real" database server (_e.g._ postgres, MySQL)
    - See the DevOps course contents 
  - Use an _Enterprise Service Bus_ (ESB) to decouple the J2E system from the .Net one
    - Consider to attend the _Service Integration_ course next year (id: SOA-1). 
