# Architecture

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

## Components assembly

The system is defined as layers:

  * A remote client (green) , that will run on each customer's device;
  * A J2E kernel (blue), implementing the business logic of the CoD system;
  * An interoperability layer (grey) between the client and the kernel, implemented as SOAP-based web services;
  * An external partner (orange, implemented in .Net), communicating with the CoD system through a Web Service.


<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/archi.png"/>
</p>

## Functional interfaces

To deliver the expected features, the coD system defines the following interfaces:

  * [`CartModifier`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/CartModifier.java): operations to handle a given customer's cart, like adding or removing cookies, retrieving the contents of the cart and validating the cart to process the associated order;
  * [`CustomerFinder`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/CustomerFinder.java): a _finder_ interface to retrieve a customer based on her identifier (here simplified to her name);
  * [`CustomerRegistration`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/CustomerRegistration.java): operations to handle customer's registration (users profile, ...)
  * [`CatalogueExploration`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/CatalogueExploration.java): operations to retrieve recipes available for purchase in the CoD;
  * [`OrderProcessing`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/OrderProcessing.java): process an order (kitchen order lifecycle management);
  * [`Payment`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/Payment.java): operations related to the payment of a given cart's contents;
  * [`Tracker`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/Tracker.java): order tracker to retrieve information about the current status of a given order.

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/interfaces.png"/>
</p>

## Business objects

The business objects are simple: [`Cookies`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/entities/Cookies.java) are defined as an enumerate, binding a name to a price. An [`Item`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/entities/Item.java) models the elements stored inside a cart, _i.e._, a given cookie and the quantity to order. A [`Customer`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/entities/Customer.java) makes orders thanks to the CoD system, and an [`Order`](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/j2e/src/main/java/fr/unice/polytech/isa/tcf/entities/Order.java) stores the set of items effectively ordered by the associated customer (bidirectional association).

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/business.png"/>
</p>

## Technological Choices

As the focus of the course is an _Introduction to Software Architecture_, we made the choice to go as lightweight as possible with respect to the tooling. As a consequence, we decided not to deploy a _real_ set of application servers and use embedded artifacts instead. This is the very justification of using _TomEE+_ as J2E container (instead of a classical Tomcat or Glassfish container) and _Mono_ as .Net implementation (instead of the classical Visual Studio technological stack). We defend that the execution details are not important when compared to the complexity of designing the right system. In addition, mapping this demonstration to existing application servers is pure engineering, with no added value.

Regarding the IDE support, this demonstration was designed using IntelliJ 15 Ultimate. The community version does not include the J2E and Web Service technological stack. However, the only constraint on the IDE is that it __must__ support the generation of stubs code from WSDL contracts.

## File Architecture

### J2E backend

The J2E part of the TCF system is defined as a Maven project, in the `j2e` directory. The `pom.xml` file declares a set of dependency to support EJB development, as well as the configuration of the TomEE+ application server to smoothly deploy the implemented beans. As the system is implemented as a _WAR_ artifact for deployment purpose, we need to declare an empty _web.xml_ file in the `webapp/WEB-INF` directory. The unit tests are implemented as JUnit tests (classical), combined with the Arquilian framework to support the testing of components deployed in an application server. The configuration of Arquilian for test purpose is declared in the `src/test/resource` directory (file named `arquilian.xml`).

__Warning__: Starting the backend with `mvn tomee:run` will not trigger a compilation of the system if the backend was previously built. You'll have to invoke `mvn package tomee:run` to force maven to recompile the system.

### Client 

The client acts as an interactive command-line. We store the WSDL contracts as java resources (`src/main/resources`). The package `cli.framework` defines a very simple interactive shell, and the package `cli.commands` implements the different commands available in the shell for the customer. 

  * Next: [Business components with EJB sessions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/BusinessComponents.md) 