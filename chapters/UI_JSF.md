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

## First JSF page

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

## Creating JSFs with parameters


We consider now a _tracker_ page. This page accepts as parameter the identifier of the order to be tracked, and displays information about the order(in our case just its status). To support this feature, we need a backing bean that will make available the orderId to work with, and the associated status. We simply asks the JSF framework to fill the `orderId` property from the parameters available in the page, through the declaration of a `ManagedProperty`. The `status` property is read-only (no setter), and relies on the `Tracker` session bean to retrieve the expected data.

```java
@ManagedBean
public class TrackerBean {

	@EJB private Tracker tracker;

	@ManagedProperty("#{param.orderId}")
	private String orderId;
	public String getOrderId() { return orderId; }
	public void setOrderId(String orderId) { this.orderId = orderId; }

	public String getStatus() {
		if(orderId == null) { return "No orderId given!"; }
		try {
			return tracker.status(orderId).name();
		} catch (UnknownOrderId uoid) {
			return "Unknown Order [" + orderId + "]";
		}
	}
}
``` 

The associated view is stored in the file `tracker.xhtml`. One can access to the status of the order #42 using the following URL: [http://localhost:8080/tcf-backend/tracker.jsf?orderId=42](http://localhost:8080/tcf-backend/tracker.jsf?orderId=42)


## Creating a (real) web application

We now want to support cookie ordering using a web interface. The application is quite simple, defined as three views:

  * the first view is used to pick an existing customer, or to create a new customer if needed;
  * the second view allows one to access to the customer carts, modify it by adding/removing cookies and then processing the cart into an order;
  * the third view is the previously defined tracker, using order references to track the status if a given order.

The following diagram represent the flow between the different views, as an automata. As `CustomerBean` and `OrderBean` will share the customer to work with (selecting her and then modifying her cart), we will use a server _session_ to share this information. The previously defined tracker should be able to track any order, and is then session independent. As a consequence, we will have to transfer to the tracker the identifier of the created order.

<p align="center">
	<img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/webflow.png"/>
</p>




### Defining the Customer view

The customer view is quite simple, it contains two forms: one to pick an existing customer, and another one to create a customer if needed. We also add a  message zone to display error messages (such as "existing customer" or "unknown customer").

```html
<h1>Cookie On Demand Web Interface</h1>

<h:messages id="form-error" style="color: red"/>
   
<h2>Pick an existing customer</h2>
<f:view>
   <h:form id="select-form">
      <h:panelGrid columns="2">
         <h:outputText value='Customer name:'/>
         <h:inputText value='#{customerBean.name}' required="true"/>
         <h:commandButton action="#{customerBean.select}" value="Select"/>
      </h:panelGrid>
   </h:form>
</f:view>

<h2>Create a new customer</h2>
<f:view>
   <h:form id="register-form">
      <h:panelGrid columns="2">
         <h:outputText value='Customer name'/>
         <h:inputText value='#{customerBean.name}' required="true" />
         <h:outputText value='Credit card number'/>
         <h:inputText value='#{customerBean.creditCard}' required="true"
                      validatorMessage="Credit card number must be 10 digits">
            <f:validateRegex pattern="\\d{10}+" />
         </h:inputText>
         <h:commandButton action="#{customerBean.register}" value="Register"/>
      </h:panelGrid>
   </h:form>
</f:view>
```

The JSF view supports client-side validation. Elements can be annotated as `required`, and specific validators can be defined. For example, the credit card number is validated thanks to a regular expression to check that it is composed by 10 digits.


The managed bean property are similar to the previously defined, excepting that any data used in an input field must be a _Read/Write_ property. Forms actions are defined as classical instance methods. The interesting thing is that these method must return a `String` acting as an automata transition, to be used by JSF to handle the web flow and move from one page to the other one.

```java
@ManagedBean
@SessionScoped
public class CustomerBean implements Serializable {

	// Bean properties go here (name and creditCard + getters and setters)
	
	@EJB private CustomerFinder finder;
	@EJB private CustomerRegistration registry;

	// Invoked when the "Select" button is pushed
	public String select() {
		if (finder.findByName(getName()).isPresent()) {
			return "SELECTED";
		} else {
			FacesContext.getCurrentInstance().addMessage("form-error", new FacesMessage("Unknown customer: " + getName()));
			return "UNKNOWN";
		}
	}
	
	// Invoked when the "Register" button is pushed
	public String register() {
		try {
			registry.register(getName(), getCreditCard());
			return "ADDED";
		} catch (AlreadyExistingCustomerException e) {
			FacesContext.getCurrentInstance().addMessage("form-error", new FacesMessage("Customer " + getName() + " already exists!"));
			return "EXISTING";
		}
	}
}
```

As the managed bean is `SessionScoped`, it must be `Serializable` to be stored in the session. Error messages are broadcasted using the `FaceContext`. The automata is defined in a file named `faces-config.xml`, stored in the `webapp/WEB-INF` directory. By default, a view will stay on the very same one excepting if a given _navigation rule_ is defined in config file.

```xml
<navigation-rule>
   <from-view-id>/customer.xhtml</from-view-id>
   <navigation-case>
      <from-outcome>SELECTED</from-outcome>
      <to-view-id>/order.xhtml</to-view-id>
   </navigation-case>
   <navigation-case>
     <from-outcome>ADDED</from-outcome>
     <to-view-id>/order.xhtml</to-view-id>
   </navigation-case>
</navigation-rule>
```

### Defining the Order view 

The order view consists in two parts: (i) a form to modify current customer cart contents and (ii) a list describing what is currently in the customer's cart. Contrarily to the previously defined view that were scalars, we need iterators here: one to iterate over all the available cookies recipes for the cart modifier interface, and another one to iterate over the cart contents to display it. One should note that JSF iterators require __ordered__ collections, and will not work when applied to a `Set`.

The available cookies recipes are presented in a classical _select_ form element. The iteration is handled by JSF thanks to the `f:selectItems` tag.

```html
<h:panelGrid columns="2">
   <h:outputText value='Cookie:'/>
   <h:selectOneMenu value="#{orderBean.cookie}">
      <f:selectItems value="#{orderBean.recipes}" var="cookie"
                     itemLabel="#{cookie.name}" itemValue="#{cookie.name}" />
   </h:selectOneMenu>
</h:panelGrid>
```

The cart contents will be presented as an unordered list, and the iteration is performed using an `ui:repeat` tag:

```html
<ul>
   <ui:repeat var="i" value="#{orderBean.cart}">
      <li><h:outputText value="#{i.cookie}" /> (x<h:outputText value="#{i.quantity}" />) </li>
   </ui:repeat>
</ul>
```

### Sharing parameters inside the session

The `order` view must share the customer name with the `customer` view. As the two beans are `SessionScoped`, we rely on a `ManagedProperty` that access to the contents of the `CustomerBean` instance automatically stored in the session from the `OrderBean` one.

```java
@ManagedProperty("#{customerBean.name}")
private String customerName;
public void setCustomerName(String customerName) { this.customerName = customerName; }
public String getCustomerName() { return customerName; }
```

### Transferring parameters outside the session

When processing the ordering form, we need to obtain the newly created `orderId` from the backend, and transfer it as a parameter to the `tracker` view. To support this task, we create a property named `orderId` in the `OrderBean` backing bean. Considering this element, it is set by the method triggered by the form and basically forwarded to the `tracker` view through the navigation rule.

```java
private String orderId;
public String getOrderId() { return orderId; }
public void setOrderId(String orderId) { this.orderId = orderId; }

public String process() {
	try {
		setOrderId(cartManager.validate(getCustomer()));
		return "ORDERED";
	} catch (PaymentException e) {
		FacesContext.getCurrentInstance().addMessage("order-error", new FacesMessage("Payment error!"));
		return "PAYMENT_ERROR";
	} catch (EmptyCartException ece) {
		FacesContext.getCurrentInstance().addMessage("order-error", new FacesMessage("Cannot validate an empty cart!"));
		return "EMPTY_CART";
	}
}
```

The navigation rule relies on a `redirect` tag to transfer the parameter:

```xml
<navigation-rule>
   <from-view-id>/order.xhtml</from-view-id>
   <navigation-case>
      <from-outcome>ORDERED</from-outcome>
      <to-view-id>/tracker.xhtml</to-view-id>
      <redirect>
         <view-param>
            <name>orderId</name>
            <value>#{orderBean.orderId}</value>
         </view-param>
      </redirect>
   </navigation-case>
</navigation-rule>
```

### Using the PostConstruct hook to load elements

The `order` view works with a given customer. Unfortunately, the `customer` view only pick a name, and not an instance of a customer. We then define a property in the `order` view to access to the current instance of `Customer` we are working with. To automatically initialize this property, we use a `PostConstruct` hook: the managed bean will be instantiated automatically, sharing customer's name with the `customer` one. Then, we simply automatically set our `Customer` property after the bean construction, using the `@PostConstruct` annotation.

```java
private Customer customer;
public Customer getCustomer() { return customer; }
public void setCustomer(Customer customer) { this.customer = customer; }
// Initializing the customer after the construction of the bean
@PostConstruct 
private void loadCustomer() { this.customer = finder.findByName(getCustomerName()).get(); }
```

  * Next: [Conclusions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Conclusions.md)


