# Behaviour-driven Development for test scenarios 

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr) - _PENDING_
  * Version: 03.2017
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Conclusions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Conclusions.md) 

## Bridging the gap between scenarios and tests


We consider here the use case _Adding cookies to a cart_. The associated scenario is quite simple:

  1. Considering a customer that exists in the system;
  2. The customer add some cookies to her cart
  3. The cart is updated (and remove duplicates, if any).

This scenario exemplifies operations exposed by the `CartModifier` interface. One can easily define a test that implements this scenario:

```java
@EJB(name = "cart-stateless") private CartModifier cart;
@PersistenceContext private EntityManager entityManager;
@Inject private UserTransaction utx;
	
@Before
public void setUpContext() {
	john = new Customer(NAME, "1234567890");
	entityManager.persist(john);
}

@After
public void cleaningUp() throws Exception {
	utx.begin();
		john = entityManager.merge(john);
		entityManager.remove(john);
		john = null;
	utx.commit();
}

@Test
public void modifyQuantities() {
	cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
	cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
	cart.add(john, new Item(Cookies.CHOCOLALALA, 3));
	Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 5), new Item(Cookies.DARK_TEMPTATION, 3)  };
	assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(john));
}
```

However, it is hard from a _Product Owner_ point of view to be confident that this java code, as readable as it is, really implements the business feature expected at the use case level.

The idea of [_Behavior-driven development_](https://en.wikipedia.org/wiki/Behavior-driven_development) (aka BDD) is to use object-oriented analysis design models (_e.g._, use cases and scenarios) as shared artefacts between development teams and requirements engineering experts. And to fill this gap as smoothly as possible.

## Setting-up Cucumber

The _de facto_ standard to implements BDD in the Java ecosystem is the [Cucumber](https://cucumber.io/) framework. It bounds a requirements engineering language ([Gherkin](https://cucumber.io/docs/reference)] to JUnit tests, using plain regular expressions.

For TCF, we need to use a version of Cucumber that is EJB-compliant, as the code to test lives inside an application container. Hopefully, a version of Cucumber bound to Arquillian exists: [Cukespace](https://github.com/cukespace/cukespace).

We add the following dependency in the POM file:

```xml
<dependency>
  <groupId>com.github.cukespace</groupId>
  <artifactId>cukespace-core</artifactId>
  <version>1.6.5</version>
  <scope>test</scope>
</dependency>
```

And we activate in the arquillian config file (`src/test/resources/arquillian.xml`) the associated extension:

```xml
<extension qualifier="cucumber">
  <property name="report">true</property>
  <property name="generateDocs">true</property>
  <property name="report-directory">target/cucumber-report</property>
  <property name="persistenceEventsActivated">true</property>
</extension>
```

## Modelling Use cases as Features

The Use case _Adding cookies to a cart_ is modelled as a `Feature`, and described using Gherkin, a requirements language based on the _Given, When, Then_ paradigm. We create a file named `OrderingCookies.feature`, where we describe an instance of this very scenario

```gherkin
Feature: Ordering Cookies

  This feature support the way a Customer can order cookies through the TCF system

  Background:
    Given a customer named Seb with credit card 1234896983
    
  Scenario: Modifying the number of cookies inside an order
    When Seb orders 2 x CHOCOLALALA
      And Seb orders 3 x DARK_TEMPTATION
      And Seb orders 3 x CHOCOLALALA
      And Seb asks for his cart contents
    Then there are 2 items inside the cart
      And the cart contains the following item: 5 x CHOCOLALALA
      And the cart contains the following item: 3 x DARK_TEMPTATION
```        

A `Scenario` contains several steps. A `Given` one represents the context of the scenario, a `When` one the interaction with the SuT (_system under test_) and a `Then` is an assertion expected from the SuT. The `Background` section is a sub-scenario that is common to all the others, and executed before their contents.

To implement the behaviour of each steps, we rely on plain JUnit. We create a test class named `OrderingCookies`, where each step is implemented as a method. The matching that binds a step to a test method is reified as classical regular expressions. Method parameters are modelled using capturing groups (between parenthesis).

```java
@RunWith(CukeSpace.class)
@CucumberOptions(features = "src/test/resources")
public class OrderingCookies extends AbstractTCFTest {
	
	@EJB private CustomerRegistration registration;
    @EJB private CustomerFinder finder;
    @EJB(name = "cart-stateless") private CartModifier cart;

	private Customer customer;
	private Set<Item> cartContents;

    @Given("^a customer named (.*) with credit card (\\d{10})$")
    public void create_customer(String customerName, String creditCard)
            throws Exception {
        registration.register(customerName, creditCard);
    }
    
    @When("^(.*) orders (\\d+) x (.*)$")
    public void ordering_cookie(String customerName, int howMany, String recipe) {
        customer = finder.findByName(customerName).get();
        Cookies cookie = Cookies.valueOf(recipe);
        cart.add(customer, new Item(cookie, howMany));
    }
    
    @When("^(.*) asks for (?:his|her) cart contents$")
    public void retrieve_cart_contents(String customerName) {
        customer = finder.findByName(customerName).get();
        cartContents = cart.contents(customer);
    }
    
    @Then("^there (?:is|are) (\\d+) items? inside the cart$")
    public void check(int size) {
        assertEquals(size, cartContents.size());
    }

    @Then("^the cart contains the following item: (\\d+) x (.*)$")
    public void check_cart_contents(int howMany, String recipe) {
        Item expected = new Item(Cookies.valueOf(recipe), howMany);
        assertTrue(cartContents.contains(expected));
    }
    
    /**
     * Cleaning up the test context between each test cases
     */
    
    @PersistenceContext private EntityManager entityManager;
    @Inject UserTransaction utx;

    @cucumber.api.java.After
    public void cleaningUpContext() throws Exception {
        utx.begin();
            customer = entityManager.merge(customer);
            entityManager.remove(customer);
        utx.commit();
        customer = null;
        cartContents = null;
    }
}
```

To run the features implemented in the `features` package, we ask Maven to do so by specifying the `test` property from the command line:

```
azrael:j2e mosser$ mvn clean package -q -Dtest=features.*

...
1 Scenarios (1 passed)
8 Steps (8 passed)
0m0.966s
...

Results :

Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
azrael:j2e mosser$
```

A report is generated in the `/target/cucumber-report/feature-overview.html` file.

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/bdd_overview.png"/>
</p>


<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/bdd_feature.png"/>
</p>


## IDE Support

IntelliJ supports Cucumber thanks to the eponymous plugin. It will provide automated completion of steps based on the defined regular expressions, syntax colouring, and folding / unfolding mechanisms.

<p align="center">
  <img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/bdd_intellij.png"/>
</p> 

__Remark__: Unfortunately, the execution plugin associated to Cucumber os not compatible with Cukespace, so one cannot triggers the tests directly from the IDE.
