# Business Components

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Architecture](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Architecture.md)

We focus here on the implementation of our first component, dedicated to handle customer's carts. The component is very basic, as it only exposes 3 operations: (i) adding an Item to a given customer's cart, (ii) removing an item in such a cart and (iii) retrieving the contents of a given cart. The definition of this component is modeled as a classical Java interface, annotated as `@Local` as we are implementing a local component.

```java
@Local
public interface Cart {
	boolean add(Customer c, Item item);
	boolean remove(Customer c, Item item);
	Set<Item> contents(Customer c);
}
```

## Stateful implementation

The simple way to implement this service is to rely on a _Stateful_ bean. The semantics of such class of beans is that each artifact connected to a given instance of the bean will always talk to the same instance. It implies for the J2E container to support a session between the caller and the callee, which consumes memory, introduce a bottleneck and prevent load-balancing. However, this is only the first step, so let's go easy for this one.

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

## Testing a component

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
 
This code is purely functional, assuming a `Cart` (the interface, no one cares about the concrete implementation). But as the Cart is a component, its lifecycle is handled by the J2E container. As a consequence, we need three to run this very test inside a container, on a deployed component. Additional information to make this test a working one is needed: (i) define how elements can be packaged into a deployable unit, (ii) inject a Cart inside this unit and (iii) asks for the _Arquillian_ test runner instead of the classical JUnit one to run the test inside a deployed version of our system.

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

To run the tests from your IDE, you must configure the environment to be _Arquillian-compliant_ (_i.e._, started inside a J2E container). IntelliJ users simply have to answer to the questions asked by the IDE, selecting a TomEE container when asked for. This also allows one to activate the debug mode while testing. If you decide to go for the latests versions of the different APIs (_i.e._, using SNAPSHOTs versions in your POM), there is no IDE integration provided, you'll only rely on Maven for test execution, and no debug mode will be available.

## Going Stateless

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
	 * Protected method to update the cart of a given customer, shared by both stateful and stateless beans
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


  * Next: [Exposing components as Web Services (SOAP)](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Exposing_SOAP.md)