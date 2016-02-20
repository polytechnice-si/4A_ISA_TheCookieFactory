# The Cookie Factory (ISA Case study)

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016

This case study is used to illustrate the different technologies involved in the _Introduction to Software Architecture_  course given at Polytech Nice - Sophia Antipolis at the graduate level. This demonstration code requires the following software to run properly:

  * Java 8
  * Maven 3
  * Mono X.Y

To compile the demonstration (j2e, .Net and client parts). The first compilation can take (a lot of) time, considering that Maven will have to download all the java dependencies necessary to build and run the system:

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

We decided to use the latest OpenEJB implementation, as the example repository available does not provide backward compatibility with the previous version of the framework. As a consequence, it unfortunately implies to use _snapshot_ versions of the artifacts, which is acceptable in the context of this course.

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
 
This code is purely functional, assuming a `Cart`. But as the Cart is a component, its lifecycle is handled by the J2E container. As a consequence, we need three additional information to make this test a working one: (i) define how elements can be packaged into a containable unit, (ii) inject a Cart inside this unit and (iii) asks for the _Arquilian_ test runner instead of the classical JUnit one to run the test inside a deployed version of our system.

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

The test suite must be updated to refer to these changes. First, we have to explicitly load the stateless bean, using the `name` argument of the `@EJB` annotation. Then, we need to modify the WebArchive containable unit to include the database (stored in a package named `utils`). Finally, we need to flush the database before each test using the classical `@Before` mechanism available in Junit.

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

__Warning__: When a component implements an interface, it is supposed to properly implement the contract. Thus, if the container does not find a bean with the name given, it will chose one randomly among the available one.

## Exposing TCF to remote clients

### Web service as interoperable layer

### Invoking a web service from a Java client

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

## Summary
  