package fr.unice.polytech.isa.tcf.business;

import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.CustomerRegistration;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CustomerRegistryTest extends AbstractTCFTest {

	@EJB private CustomerRegistration registry;
	@EJB private CustomerFinder finder;


	@PersistenceContext private EntityManager entityManager;
	@Inject private UserTransaction utx;

	private Customer john;

	@Before
	public void setUpContext() throws Exception {
		john = new Customer("John", "0987654321");
	}

	@After
	public void cleaningUp() throws Exception {
	    utx.begin();
            Optional<Customer> toDispose = finder.findByName(john.getName());
            toDispose.ifPresent(cust -> { Customer c = entityManager.merge(cust); entityManager.remove(c); });
        utx.commit();
	}

	@Test
	public void unknownCustomer() {
		assertFalse(finder.findByName("Bob").isPresent());
	}

	@Test
	public void registerCustomer() throws Exception {
		registry.register(john.getName(), john.getCreditCard());
		Optional<Customer> customer = finder.findByName(john.getName());
		assertTrue(customer.isPresent());
		Customer retrieved = customer.get();
		assertEquals(john.getName(), retrieved.getName());
		assertEquals(john.getCreditCard(), retrieved.getCreditCard());
	}

	@Test(expected = AlreadyExistingCustomerException.class)
	public void cannotRegisterTwice() throws Exception {
		registry.register(john.getName(), john.getCreditCard());
		registry.register(john.getName(), john.getCreditCard());
	}

}
