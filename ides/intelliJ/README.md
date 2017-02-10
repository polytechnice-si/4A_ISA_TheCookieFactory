# Setting up IntelliJ

  - Author: Sebastien Mosser
  - Version: 02.2017

## IDE Version & Plugins

With respect tot the tools and frameworks we are using in this course, it is more useful to rely on the IntelliJ Ultimate Edition. As a student, you can register to the [Jetbrains Student pack](https://www.jetbrains.com/student/) and get a license for free.

To support testing inside the application container, we rely on the Arquillian framework. Be sure that the "Arquilian JBoss support is installed"

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/ides/intelliJ/arquillian_plugin.png"/>
</p>

## Running Tests with Arquillian

Arquillian allows one to transparently start an application container, deploy the application inside and run tests suites.

To start a test suite, you cannot use the classical Unit runner, but use the Arquillian one instead (the icon looks like a little ET-like alien).


<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/ides/intelliJ/run_with_arquilian.png"/>
</p>


The run must be configured to specify which Application container must be used. In our case, we rely on TomEE, and considering it is specified in the maven pom.xml files, the IDE automatically propose to select it.

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/ides/intelliJ/configure_tomEE.png"/>
</p>

Unfortunately, the arquillian plugin does not support automatic enhancement of classes, which is necessary for OpenJPA to make classes persistent. We need to delegate this behaviour to a _java agent_, by specifying its path in the JVM options. The path is relative to the root of your IntelliJ Project.

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/ides/intelliJ/with_javaagent.png"/>
</p>