# Component-based Web UI 

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Making things persistent](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Persistence.md)



## Configuring the JSF framework

### Loading the framework

We are going to use _Java Server Faces_ (JSF) to support a web-based user interface for our system. The JSF framework will be provided by the application server used at runtime. TomEE relies on the Apache implementation (MyFaces) of the JSF specifications. We need to add a dependency in the POM to gain access to the framework at compile time:

```xml
<!-- Dependencies for the JSF framework -->
<dependency>
	<groupId>org.apache.myfaces.core</groupId>
   <artifactId>myfaces-api</artifactId>
   <version>2.2.10</version>
   <scope>provided</scope>
</dependency>
```


### Configuring the application to support JSF

The JSF framework will intercept HTTP requests and process web-based templates and back-end component to render a web application. This process is implemented by a Java _servlet_ that will catch requests and do the needed rendering to serve web pages based on our component-based system. Activating this servlet is done in the `web.xml` file. First we load the JSF _servlet_, and then we bind any request matching `*.jsf` or `*.xhtml` to this very _servlet_. 

```xml
<servlet>
   <servlet-name>Faces Servlet</servlet-name>
   <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
   <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
   <servlet-name>Faces Servlet</servlet-name>
   <url-pattern>*.jsf</url-pattern>
</servlet-mapping>
<servlet-mapping>
   <servlet-name>Faces Servlet</servlet-name>
   <url-pattern>*.xhtml</url-pattern>
</servlet-mapping>
``` 

## JSF101: Creating a read-only Face

Previously, we created an interceptor that counts the number of processed carts. We are now going to create a web page accessing to this data.

First, we need an XHTML template representing the web page skeleton. In the `WEB-INF` directory, we create a `statistics.xhtml` for this purpose:

```xml
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:h="http://java.sun.com/jsf/html">

    <h:head>
        <title>.:: The Cookie Factory :: Admin ::.</title>
    </h:head>

    <h:body>
        <h1>Cookie on Demand / Statistics </h1>
        <ul>
            <li>Number of processed carts: #{statisticsBean.processed}</li>
        </ul>
    </h:body>

</html>
```

Templates are _backed_ by elements called `ManagedBean`s. The role of a `ManagedBean` is to implement the data binding needed for a template and the associated behavior, if needed. 
In our example, we need a bean that will access to the statistics we want to display. This is implemented in a dedicated bean named `StatisticsBean`, which exposed a `processed` property that is used in the template (`#{statisticsBean.processed}`)

```java
@ManagedBean
public class StatisticsBean implements Serializable {

	@EJB private Database memory;
	public int getProcessed() { return memory.howManyCarts(); }

}
```

The resulting web page can be accessed at the following url: [http://localhost:8080/tcf-backend/statistics.jsf](http://localhost:8080/tcf-backend/statistics.jsf).

__Things to know, or to learn the hard way__:

  * Be sure that the template file name ends with `.xhtml`, even if you access it as `.jsf` through the URL. 
    * Symptom: assertion error in the JVM, at runtime
  * Be sure that you are importing `javax.faces.bean.ManagedBean` and not `javax.annotation.ManagedBean` in your managed bean class. 
    * Symptom: managed bean resolved to null
  * Even if the class name starts with an upper case letter, the template must refer to it using a lower case letter at the beginning.
    * Symptom: managed bean resolved to null
  * JSF rendering error logs are not displayed on stdout. One should look at the contents of the server log file to access to error details:
    * `cd target/apache-tomee/logs/`, and then look into `localhost.YYYY-MM-DD.log`.

## Creating a (real) web application

We now want to support cookie ordering using a web interface. The application is quite simple, defined as three pages:

  * the first page is used to pick an existing customer, or to create a new customer if needed;
  * the second page allows one to access to the customer carts, modify it by adding/removing cookies and processing the cart into an order;
  * the third page is the order tracker, using order references to track the status if a given order.

  * Next: [Conclusions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Conclusions.md)


