# TCF Java Enterprise Edition (J2E) Kernel

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016

  
The _Cookie on Demand_ system is implemented as a component-based system that relies on the J2E framework. It relies on the following technologies:

  * _Enterprise Java Beans_ (EJBs, OpenEJB) to implement components;
  * _Java Server Faces_ (JSF, MyFaces) to implement web-based user interfaces;
  * _Java Persistence API_ (JPA, OpenJPA) to support object persistence;
  * _HyperSQL Database_ (HSQLDB) to store data;
  * _Web Services_ (CXF) using SOAP and REST paradigms;
  * _Java Message Service_ (JMS, ActiveMQ) for asynchronous communications;
  * _TomEE+_, a lightweight version of TomCat as J2E application container;
  * _Arquillian_, a state-of-the-art testing framework for J2E systems;
  * _Maven_ to put all these technologies together _"easily"_.

## Compiling and Running the system

To compile the system, use the following command. It produces a file named `tcf-backend.war` in the `target` directory.

    azrael:mosser j2e$ mvn package
    
Considering a compiled system, one can start a TomEE+ application container and deploy the previously generated WAR file inside it using the following command:

    azrael:mosser j2e$ mvn tomee:run

__Remarks__:

  * The persistent database used by the production system is stored inside the `target` directory. As a consequence, using the `mvn clean` command will flush the database contents.
  * Tests can be (very) long to run. One can bypass the test suite by adding `-DskipTests` to any maven command line.
  * We provide one integration test, invoked using the `mvn integration-test` command. For this test to end properly, the .Net backend must be started.
  
  
## Source code architecture  

As a classical maven artefact, the project relies on the `pom.xml` file to model its content, and on two directories: `src/main/java` for the business code, and `src/test/java` for the test code.

### Test source architecture (src/test/java)

  * The `resources` directory contains a file named `arquillian.xml` that models  the different resources needed by the testing framework (_e.g._, which port to use when starting the application container, which database connection to be used for test purposes)
  * The `arquillian` package defined an abstract root class named `AbstractTCFest`. This class defines the deployment method required by any Arquillian test once and for all. It basically load all the business classes into a deployable artefact the testing framework will use to run the test suite.
  * The `fr.unice.polytech.isa.tcf` packages contains 3 sub-packages that define the test suite:
    * `business` contains all the tests that assess the behavior of the business components (domain layer, EJB sessions)
    * `persistence` contains all the tests that assess the behavior of the persistent classes (persistence layer, EJB entities) 
    * `integration` defines one integration tests that assess the connection between the J2E backend and the third-part system written in .Net

### Business source architecture (src/main/java)  

  * The `resources` directory contains two configuration files:
    * `bank.properties` that is used to store the location of the third part system (host and port)
    * `META-INF/persistence.xml` that declares the persistence unit associated to the system (which classes are persistent among the source code);
  * The `webapp` directory contains elements dedicated to the application container and the _web_ part of the system:
    * The `WEB-INF` directory stores information for the application container:
      * `ejb-jar.xml` to bind cross-cutting interceptors to the deployed components
      *  `resources.xml` to store the database connection related information;
      *  `web.xml` to activate the JSF framework;
      *  `faces-config.xml` to define the flow that exists between the different JSF pages;
    * XHTML views implementing the JSF-based web interface
    * a _Java Server Page_ (JSP) that redirects the incoming requests to the entry point of the JSF application;
  * The source code is defined inside the `fr.unice.polytech.isa.tcf` package:
    * All the component interfaces are available at the top level;
    * The `asynchronous` package contains Message-driven Beans to support asynchronous communications;
    * The `components` package contains concrete implementation of the previously described interfaces;
    * The `entities` package contains the business object used inside the system;
    * The `exceptions` package contains all the business faults the system can fall in;
    * The `interceptors` package contains cross-cutting concerns bound to the system using _aspect-oriented_ like mechanisms;
    * The `managed` package contains the backing bean necessary for the JSF views;
    * The `utils` package contains a library wrapping the HTTP request necessary to interact with the .Net third-part system, and an in-memory database used for statistics;
    * The `webservices` package contains the interfaces of the SOAP-based web services as well as their concrete implementation.

## Perspectives

  - [ ] Modularize the TCF system (see DevOps course contents);  
  - [ ] Run _Sonar_ on the source code;    