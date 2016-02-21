# The Cookie Factory (ISA Case study)

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016

This case study is used to illustrate the different technologies involved in the _Introduction to Software Architecture_  course given at Polytech Nice - Sophia Antipolis at the graduate level. This demonstration code requires the following software to run properly:

  * Build & J2E environment configuration: Maven 3
  * J2E implementation language: Java 8
  * .Net implemenyation language: Mono >3.12

To compile the demonstration (j2e, .Net and client parts), simply run the compilation script. The first compilation can take (a lot of) time, considering that Maven will have to download all the java dependencies necessary to build and run the system (the application server weights 43Mb):

    mosser@azrael $ ./buildAll.sh
    
To run the demonstration, first start the two servers in two different terminals, then start the remote client in a third one:

    # J2E terminal 						(^C to stop)
    mosser@azrael $ cd j2e
    mosser@azrael j2e$ mvn tomee:run
  
    # .Net terminal						(^C to stop)
    mosser@azrael $ cd dotNet
    mosser@azrael dotNet$ XXX
    
    # Remote Client						(bye to stop)
    mosser@azrael $ cd client
    mosser@azrael client$ mvn exec:java

## Architecture

System description

### Business Architecture

Interfaces

Business objects

### Technological Choices

As the focus of the course is an _Introduction to Software Architecture_, we made the choice to go as lightweight as possible with respect to the tooling. As a consequence, we decided not to deploy a _real_ set of application servers and use embedded artifacts instead. This is the very justification of using _TomEE+_ as J2E container (instead of a classical Tomcat or Glassfish container) and _Mono_ as .Net implementation (instead of the classical Visual Studio technological stack). We defend that the execution details are not important when compared to the complexity of designing the right system. In addition, mapping this demonstration to existing application servers is pure engineering, with no added value.

Regarding the IDE support, this demonstration was designed using IntelliJ 15 Ultimate. The community version does not include the J2E and Web Service technological stack. However, the only constraint on the IDE is that it __must__ support the generation of stubs code from WSDL contracts.

### File Architecture

#### J2E backend

The J2E part of the TCF system is defined as a Maven project, in the `j2e` directory. The `pom.xml` file declares a set of dependency to support EJB development, as well as the configuration of the TomEE+ application server to smoothly deploy the implemented beans. As the system is implemented as a _WAR_ artifact for deployment purpose, we need to declare an empty _web.xml_ file in the `webapp/WEB-INF` directory. The unit tests are implemented as JUnit tests (classical), combined with the Arquilian framework to support the testing of components deployed in an application server. The configuration of Arquilian for test purpose is declared in the `src/test/resource` directory (file named `arquilian.xml`).

__Warning__: Starting the backend with `mvn tomee:run` will not trigger a compilation of the system if the backend was previously built. You'll have to invoke `mvn package tomee:run` to force maven to recompile the system.

## First step: Customer's Cart

We focus here on the implementation of our first component, dedicated to handle customer's carts. The component is very basic, as it only exposes 3 operations: (i) adding an Item to a given customer's cart, (ii) removing an item in such a cart and (iii) retrieving the contents of a given cart. The definition of this component is modeled as a classical Java interface, annotated as `@Local` as we are implementing a local component.

```java
@Local
public interface Cart {
	boolean add(Customer c, Item item);
	boolean remove(Customer c, Item item);
	Set<Item> contents(Customer c);
}
```

### State-full implementation

The simple way to implement this service is to rely on a _State-full_ bean. The semantics of such class of beans is that each artifact connected to a given instance of the bean will always talk to the same instance. It implies for the J2E container to support a session between the caller and the callee, which consumes memory, introduce a bottleneck and prevent load-balancing. However, this is only the first step, so let's go easy for this one.

The implementation of the component is straightforward, using a Map to store the binding that exists between customers and items. The state-full property ensures that we'll always talk to the same map.

```java
@Stateful
public class CartStateFullBean implements Cart {

	private Map<Customer, Set<Item>> carts = new HashMap<>();

	@Override
	public boolean add(Customer c, Item item) {
		Set<Item> items = contents(c);
		Optional<Item> existing = items.stream().filter(e -> e.getCookie().equals(item.getCookie())).findFirst();
		if(existing.isPresent()) {
			items.remove(existing.get());
			Item toAdd = new Item(item.getCookie(), item.getQuantity() + existing.get().getQuantity());
			if(toAdd.getQuantity() > 0) { items.add(toAdd); }
		} else {
			items.add(item);
		}
		carts.put(c, items);
		return true;
	}

	@Override
	public boolean remove(Customer c, Item item) {
		return add(c, new Item(item.getCookie(), -item.getQuantity()));
	}

	@Override
	public Set<Item> contents(Customer c) {
		return carts.getOrDefault(c, new HashSet<Item>());
	}
}
```

### Testing a component

The previously implemented component should ensure the four following properties: (i) the cart of a given customer is empty by default, (ii) adding multiple items results in a cart containing such items, (iii) one can remove cookies from a cart and finally (iii) one can modify the already existing quantity for a given item. Considering a given `Cart` named `cart`, the test implementation is also straightforward.

```java
@Test public void emptyCartByDefault() {
	Customer c = new Customer(UUID.randomUUID().toString());
	Set<Item> data = cart.contents(c);
	assertArrayEquals(new Item[] {}, data.toArray());
}

@Test public void addItems() {
	Customer john = new Customer("john");
	cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
	cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
	Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 2), new Item(Cookies.DARK_TEMPTATION, 3)  };
	assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(john));
}

@Test public void removeItems() {
	Customer john = new Customer("john");
	cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
	cart.remove(john, new Item(Cookies.CHOCOLALALA, 2));
	assertArrayEquals(new Item[] {}, cart.contents(john).toArray());
	cart.add(john, new Item(Cookies.CHOCOLALALA, 6));
	cart.remove(john, new Item(Cookies.CHOCOLALALA, 5));
	assertArrayEquals(new Item[] {new Item(Cookies.CHOCOLALALA, 1)}, cart.contents(john).toArray());
}

@Test public void modifyQuantities() {
	Customer john = new Customer("john");
	cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
	cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
	cart.add(john, new Item(Cookies.CHOCOLALALA, 3));
	Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 5), new Item(Cookies.DARK_TEMPTATION, 3)  };
	assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(john));
}
```
 
This code is purely functional, assuming a `Cart` (the interface, no one cares about the concrete implementation). But as the Cart is a component, its lifecycle is handled by the J2E container. As a consequence, we need three to run this very test inside a container, on a deployed component. Additional information to make this test a working one is needed: (i) define how elements can be packaged into a deployable unit, (ii) inject a Cart inside this unit and (iii) asks for the _Arquilian_ test runner instead of the classical JUnit one to run the test inside a deployed version of our system.

```java
@RunWith(Arquillian.class)
public class CartTest {

	// Classes to package into a deployable unit used to run the test
	@Deployment public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Business Objects
				.addPackage(Customer.class.getPackage())  
				// Components interfaces
				.addPackage(Cart.class.getPackage())  
				// Component implementation    
				.addPackage(CartStateFullBean.class.getPackage()); 
	}

	// Injecting a Cart inside the test suite
	@EJB private Cart cart;
	
	// test code goes here ... 
}
```

You must remark that the `Cart` is never initialized. This is how _dependency injection_ works. The container analyzes the `@EJB` annotation and will bind your local variable to an instance a component that respect this interface, at runtime. __It is not your responsibility anymore to instantiate objects when they implement EJBs__.

### Going State-less

The previously described component is actually wrong. It works as a client will always be connected to the very same instance of the bean, but multiple beans will not share the same in memory map, as each one contains a partial information. In addition, being state-full implies to maintain a session between the caller and the callee, which is a performance-killer for the container as it prevent the bean management process to properly handle the component lifecycle.

Going stateless means to expose a bean that does not store itself customer's information. These information will be stored in an in-memory _Database_, shared by all the cart beans. 

We first define an _abstract_ cart bean, that will factorize the commonality existing between our two implementations.

```java 
public abstract class AbstractCartBean implements Cart {

	@Override
	public final boolean remove(Customer c, Item item) {
		return add(c, new Item(item.getCookie(), -item.getQuantity()));
	}

	/**
	 * Protected method to update the cart of a given customer, shared by both statefull and stateless beans
	 */
	protected Set<Item> updateCart(Customer c, Item item) {
		Set<Item> items = contents(c);
		Optional<Item> existing = items.stream().filter(e -> e.getCookie().equals(item.getCookie())).findFirst();
		if(existing.isPresent()) {
			items.remove(existing.get());
			Item toAdd = new Item(item.getCookie(), item.getQuantity() + existing.get().getQuantity());
			if(toAdd.getQuantity() > 0) { items.add(toAdd); }
		} else {
			items.add(item);
		}
		return items;
	}
}
```

We then implement a shared database, using an in-memory map. This database must be a singleton, as it will be shared by all the beans that exists in the system.

```java
@Singleton
public class Database {

	private Map<Customer, Set<Item>> carts = new HashMap<>();
	public Map<Customer, Set<Item>> getCarts() { return carts; }

	public void flush() { carts = new HashMap<>(); }

}
```

We can now implement a stateless bean, that will interact with the database (thanks to the `@EJB` annotation). As the state-full and state-less beans share the same component interface, we need to name the bean (using the `name` argument) so that one can explicitly refer to a given implementation if needed.

```java
@Stateless(name = "cart-stateless")
public class CartStateLessBean extends AbstractCartBean {

	@EJB Database memory;

	@Override
	public boolean add(Customer c, Item item) {
		memory.getCarts().put(c, updateCart(c, item));
		return true;
	}

	@Override
	public Set<Item> contents(Customer c) {
		return memory.getCarts().getOrDefault(c, new HashSet<Item>());
	}
}
```

The test suite must be updated to refer to these changes. First, we have to explicitly load the stateless bean, using the `name` argument of the `@EJB` annotation. Then, we need to modify the WebArchive deployable unit to include the database (stored in a package named `utils`). Finally, we need to flush the database before each test using the classical `@Before` mechanism available in Junit.

```java
@Deployment public static WebArchive createDeployment() {
	return ShrinkWrap.create(WebArchive.class)
			.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
			.addPackage(Database.class.getPackage())
			.addPackage(Customer.class.getPackage())
			.addPackage(Cart.class.getPackage()) 
			.addPackage(CartStateFullBean.class.getPackage());
}

@EJB private Database memory;
@EJB(name = "cart-stateless") private Cart cart;

@Before public void flushDatabase() { memory.flush(); }

// test code goes here, unchanged.
```

__Warning__: When a component implements an interface, it is supposed to properly implement the contract. Thus, if the container does not find a bean with the given name, it will chose one randomly among the available one, as it should be semantically equivalent.

## Exposing TCF to remote clients

The Cookie Factory system must be exposed to remote clients, so that a customer can order some cookies through the service. Two options: (i) exposing the EJBs as remote ones, or (ii) expose these beans through a Web Service. The first option implies for the clients to be J2E-compliant, when the second option will allows clients to be developed in any language.

The other advantage of exposing the internal system thanks to a set of satellite web services is the decoupling ensured by this approach. The operations exposed by the web service are not the exact reflect of the associated bean, a web service can combine multiple beans to add value to the system. The Web Service layer is the _public API_ of our architecture. 

### Web service as interoperable layer

__Warning__: Implementing web services implies to respect a set of constraints:

  * Web services are stateless, according to the WS standard. As a consequence, any beans exposed to the outside world through a Web Service must be stateless. 
  * Business objects exposed through the public API must be serializable, and must define an empty constructor and get/set methods to be properly populated by the WE framework
  * The TomEE container must 

Here, we decide to expose two operations in the public API:  adding a cookie to a cart and getting cart contents. We exposed the `Item` business object in these methods as it exactly models what we are using in these methods. But the `Customer` business class is purely internal, it does not make any sense to expose to the outside world our complete representation of a Customer. Thus, we use a plain String to identify the customer in the public API.

A Web service contract is defined by an annotated interface. The annotations are straightforward: 

  * A `WebMethod` annotation tags the methods to expose as service operations;
  * A `WebParam` annotation tags the parameters to change their name, or handle xml namespace manually;
  * A `WebResult` annotation tags the returned value, like `@WebParam`.

```java
@WebService
public interface CartWebService {

	@WebMethod
	void addItemToCustomerCart(@WebParam(name = "customer_name") String customerName,
							   @WebParam(name = "item") Item it);

	@WebMethod
	@WebResult(name = "cart_contents")
	Set<Item> getCustomerCartContents(@WebParam(name = "customer_name") String customerName);

}
``` 

The implementation of these operations is simply done by a concrete class that implements the interface and delegates business work to the bean. This concrete class is actually a stateless bean that consumes another bean.

```java
@WebService(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf")
@Stateless(name = "CartWS")
public class CartWebServiceImpl implements CartWebService {

	@EJB(name="stateless-cart") Cart cart;

	@Override
	public void addItemToCustomerCart(String customerName, Item it) {
		cart.add(new Customer(customerName), it);
	}

	@Override
	public Set<Item> getCustomerCartContents(String customerName) {
		return cart.contents(new Customer(customerName));
	}
}
```

The application container will support the exposition of this class according the the Web Service standard. To start the container in server mode, use the `mvn tomee:run` 

### Invoking a web service from a Java client

We create a remote client for the service, in a new maven project (`client`) directory.

The contract of the service, exposed using the _Web Service Description Language_ (WSDL), is automatically exposed by TomEE: [http://localhost:8080/tcf-backend/webservices/CartWS?wsdl](http://localhost:8080/tcf-backend/webservices/CartWS?wsdl).

Copy paste the contents of this contract (use the _view source_ option in your browser) into a local file located inside the client project, for example in `src/main/resources/CartWS.wsdl`. Then, use the _web services capabilities_ of your IDE to generate the Java code that will support the interactions with the service. For IntelliJ Ultimate users (the community edition does not contains the web service stack), _right-click_ on the WSDL contract, then select _Web Services_ and _Generate Java from WSDL_. The Java files generated from the WSDL contract will be stored inside a given package (_e.g._, `stubs.cart`). 

__Warning__: Obviously, each time the service contract will change, you'll have to rerun the stub generation process on the latest WSDL contract.

To instantiate the stub that will support the communication between the client and the service, we simply ask the generated stub code to do so (in the class `CartWSDemo`):

```java
private static CartWebService initialize() {
	System.out.println("#### Instantiating the WS Proxy");
	CartWebServiceImplService factory = new CartWebServiceImplService();
	CartWebService ws = factory.getCartWebServiceImplPort();
	return ws;
}
```	

From the client perspective, the code to be used to consume the web service is pretty simple: it is a Java version of the exposed contract. One can remark that some information is lost during the _Java to WSDL to Java_ contract generation then stub generation process. For example, the cart is modeled as a _Set_ on the server side, and was translated into a _List_ on the client side.

```java
public static void main() {
	CartWebService ws = initialize();

	List<Item> cart = ws.getCustomerCartContents("john");
	System.out.println("Cart is empty: " + cart.isEmpty());

	Item i = new Item();
	i.setCookie(Cookies.CHOCOLALALA); i.setQuantity(3);
	ws.addItemToCustomerCart("john", i);
	i.setCookie(Cookies.DARK_TEMPTATION); i.setQuantity(2);
	ws.addItemToCustomerCart("john", i);
	i.setCookie(Cookies.CHOCOLALALA); i.setQuantity(4);
	ws.addItemToCustomerCart("john", i);

	cart = ws.getCustomerCartContents("john");
	System.out.println("John's cart: " +cart);
}

```

### Removing runtime location dependencies

The previously described code works well, but rely on a very string assumption: the service will always be located at the very same location (on localhost). Moreover, the client will load at runtime the WSDL contract, so if one moves the contract elsewhere, the client code does not work anymore. And it is anyway not reasonable to package a distributed application that will only run on localhost. We need to _clean_ our default implementation to be more _aware_ of the server location.

First point, look at the WSDL contract. Even if stored locally as a resource, it refers to a remote file located at [localhost:8080/tcf-backend/webservices/CartWS?wsdl=CartWebService.wsdl](localhost:8080/tcf-backend/webservices/CartWS?wsdl=CartWebService.wsdl). We store this file as a local one, side by side with the initial contract, in a file named `CartWSType.wsdl` (as it basically defines the data types associated to our contract). Then, we edit the `CartWS.wsdl` file to point to this local file instead of the remote one. We should now edit the initialization code to refer to our local file instead of the remote one.

```java
private static CartWebService initialize() {
	System.out.println("#### Loading the WSDL contract");
	URL wsdlLocation = CartWSDemo.class.getResource("/CartWS.wsdl");
	System.out.println("#### Instantiating the WS Proxy");
	CartWebServiceImplService factory = new CartWebServiceImplService(wsdlLocation);
	CartWebService ws = factory.getCartWebServiceImplPort();
	return ws;
}		
```

It only solves half of the problem. Now, the client code will load at runtime the local WSDL contract, but it will always try to consume a service deployed on localhost. We need to configure the _endpoint_ at runtime to point anywhere the service will be deployed. We apply the following assumption: by default it is deployed in development mode on _localhost_, and if command line arguments are provided, the first one is the hostname and the second one, if provided, is the port number.

```java
private static CartWebService initialize(String host, String port) {
	System.out.println("#### Loading the WSDL contract");
	URL wsdlLocation = CartWSDemo.class.getResource("/CartWS.wsdl");
	System.out.println("#### Instantiating the WS Proxy");
	CartWebServiceImplService factory = new CartWebServiceImplService(wsdlLocation);
	CartWebService ws = factory.getCartWebServiceImplPort();
	System.out.println("#### Updating the endpoint address dynamically");
	String address = "http://"+host+":"+port+"/tcf-backend/webservices/CartWS";
	((BindingProvider) ws).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
	return ws;
}
```

## Complete TCF Architecture with Mocked data



### Implementing the components

### Web service exposition

## Persistent entities

### Scalar entities

### Composite entities

### Querying the persistent objects

## External partner

### Implementing a .Net web service using Mono

### Invoking a web service within a J2E component

## Interceptors

### Aspect-oriented programing concepts

### Counting the number of processed carts

## Summary
  