package fr.unice.polytech.isa.tcf;

// business imports
import fr.unice.polytech.isa.tcf.components.CartStateFullBean;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;

import java.util.*;
// component test framework import
import fr.unice.polytech.isa.tcf.utils.Database;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
// java annotations
import org.jboss.arquillian.container.test.api.Deployment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.ejb.EJB;
// static import to lighten test writing
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CartTest {

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addPackage(Database.class.getPackage())
				.addPackage(Customer.class.getPackage())  		   // Business Objects
				.addPackage(Cart.class.getPackage())               // Components interfaces
				.addPackage(CartStateFullBean.class.getPackage()); // Component implementation
	}

	@EJB private Database memory;
	@EJB(name = "cart-stateless") private Cart cart;

	@Before
	public void flushDatabase() { memory.flush(); }

	@Test
	public void emptyCartByDefault() {
		Customer c = new Customer(UUID.randomUUID().toString());
		Set<Item> data = cart.contents(c);
		assertArrayEquals(new Item[] {}, data.toArray());
	}

	@Test
	public void addItems() {
		Customer john = new Customer("john");
		cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
		cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
		Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 2), new Item(Cookies.DARK_TEMPTATION, 3)  };
		assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(john));
	}

	@Test
	public void removeItems() {
		Customer john = new Customer("john");
		cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
		cart.remove(john, new Item(Cookies.CHOCOLALALA, 2));
		assertArrayEquals(new Item[] {}, cart.contents(john).toArray());
		cart.add(john, new Item(Cookies.CHOCOLALALA, 6));
		cart.remove(john, new Item(Cookies.CHOCOLALALA, 5));
		assertArrayEquals(new Item[] {new Item(Cookies.CHOCOLALALA, 1)}, cart.contents(john).toArray());
	}

	@Test
	public void modifyQuantities() {
		Customer john = new Customer("john");
		cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
		cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
		cart.add(john, new Item(Cookies.CHOCOLALALA, 3));
		Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 5), new Item(Cookies.DARK_TEMPTATION, 3)  };
		assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(john));
	}

}
