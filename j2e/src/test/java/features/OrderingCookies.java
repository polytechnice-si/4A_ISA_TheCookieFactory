package features;

import arquillian.AbstractTCFTest;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.arquillian.CukeSpace;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.Set;


import fr.unice.polytech.isa.tcf.*;
import fr.unice.polytech.isa.tcf.entities.*;


@RunWith(CukeSpace.class)
@CucumberOptions(features = "src/test/resources")
public class OrderingCookies extends AbstractTCFTest {
    
    @EJB private CustomerRegistration registration;
    @EJB private CustomerFinder finder;
    @EJB(name = "cart-stateless") private CartModifier cart;
    @EJB(name = "cart-stateless") private CartProcessor processor;

    private Customer customer;
    private Set<Item> cartContents;

    @Given("^a customer named (.*) with credit card (\\d{10})$")
    public void create_customer(String customerName, String creditCard)
            throws Exception {
        registration.register(customerName, creditCard);
    }

    @When("^(.*) asks for (?:his|her) cart contents$")
    public void retrieve_cart_contents(String customerName) {
        customer = finder.findByName(customerName).get();
        cartContents = processor.contents(customer);
    }

    @When("^(.*) orders (\\d+) x (.*)$")
    public void ordering_cookie(String customerName, int howMany, String recipe) {
        customer = finder.findByName(customerName).get();
        Cookies cookie = Cookies.valueOf(recipe);
        cart.add(customer, new Item(cookie, howMany));
    }

    @When("^(.*) decides not to buy (\\d+) x (.*)$")
    public void removing_cookie(String customerName, int howMany, String recipe) {
        customer = finder.findByName(customerName).get();
        Cookies cookie = Cookies.valueOf(recipe);
        cart.remove(customer, new Item(cookie, howMany));
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

    @Then("^the price of (.*)'s cart is equals to (\\d+.\\d+?)$")
    public void check_cart_price(String customerName, float expectedPrice) {
        customer = finder.findByName(customerName).get();
        assertEquals(expectedPrice, processor.price(customer), 0.01);
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
