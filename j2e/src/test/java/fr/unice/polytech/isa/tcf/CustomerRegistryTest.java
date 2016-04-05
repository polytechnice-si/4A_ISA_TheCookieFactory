package fr.unice.polytech.isa.tcf;

import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CustomerRegistryTest extends AbstractTCFTest {

	@EJB private CustomerRegistration registry;
	@EJB private CustomerFinder finder;


	@PersistenceContext private EntityManager entityManager;
	@Resource private UserTransaction transaction;

	@Before
	public void setUpContext() throws Exception {
		memory.flush();
	}

	@After
	public void cleaningUp() throws Exception {
	 	transaction.begin();
			entityManager.createQuery("DELETE FROM Customer").executeUpdate();
		transaction.commit();
	}

	@Test
	public void unknownCustomer() {
		assertFalse(finder.findByName("John").isPresent());
	}

	@Test
	public void registerCustomer() throws Exception {
		String name = "John";
		String creditCard = "0987654321";
		registry.register(name, creditCard);
		Optional<Customer> customer = finder.findByName(name);
		assertTrue(customer.isPresent());
		Customer john = customer.get();
		assertEquals(name, john.getName());
		assertEquals(creditCard, john.getCreditCard());
	}


	@Test(expected = AlreadyExistingCustomerException.class)
	public void cannotRegisterTwice() throws Exception {
		String name = "John";
		String creditCard = "0987654321";
		registry.register(name, creditCard);
		registry.register(name, creditCard);
	}

}
