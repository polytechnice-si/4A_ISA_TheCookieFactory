package fr.unice.polytech.isa.tcf;

// business imports
import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;

import java.util.*;
// component test framework import
import org.jboss.arquillian.junit.Arquillian;
// java annotations
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
// static import to lighten test writing
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CartTest extends AbstractTCFTest {


	@EJB(name = "cart-stateless") private CartModifier cart;
	@EJB CustomerRegistration registry;
	@EJB CustomerFinder finder;

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserTransaction transaction;

	private static final String NAME = "John";
	private Customer john;

	@Before
	public void setUpContext() throws Exception {
		memory.flush();
		registry.register(NAME, "1234567890");
		john = finder.findByName(NAME).get();
	}

	@After
	public void cleaningUp() throws Exception {
		// cannot be annotated with @Transactional, as it is not a test method
		transaction.begin();
			Customer detached = finder.findByName(NAME).get();
	    	Customer attached = entityManager.merge(detached);
			entityManager.remove(attached);
			john = null;
		transaction.commit();
	}

	@Test
	public void emptyCartByDefault() {
		Set<Item> data = cart.contents(john);
		assertArrayEquals(new Item[] {}, data.toArray());
	}

	@Test
	public void addItems() {
		cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
		cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
		Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 2), new Item(Cookies.DARK_TEMPTATION, 3)  };
		assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(john));
	}

	@Test
	public void removeItems() {
		cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
		cart.remove(john, new Item(Cookies.CHOCOLALALA, 2));
		assertArrayEquals(new Item[] {}, cart.contents(john).toArray());
		cart.add(john, new Item(Cookies.CHOCOLALALA, 6));
		cart.remove(john, new Item(Cookies.CHOCOLALALA, 5));
		assertArrayEquals(new Item[] {new Item(Cookies.CHOCOLALALA, 1)}, cart.contents(john).toArray());
	}

	@Test
	public void modifyQuantities() {
		cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
		cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
		cart.add(john, new Item(Cookies.CHOCOLALALA, 3));
		Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 5), new Item(Cookies.DARK_TEMPTATION, 3)  };
		assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(john));
	}

}
