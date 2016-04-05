package fr.unice.polytech.isa.tcf.persistence;


import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.entities.Customer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.RollbackException;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class StorageTest extends AbstractTCFTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserTransaction manual;

	@Test
	@Transactional(TransactionMode.COMMIT)
	public void testCustomerStorage() throws Exception {
		Customer c = new Customer();
		c.setCreditCard("1234567890");
		c.setName("John Doe");
		entityManager.persist(c);
		int id = c.getId();
		assertNotNull(id);
		Customer stored = (Customer) entityManager.find(Customer.class, id);
		assertEquals(c, stored);
	}

	@Test(expected = ConstraintViolationException.class)
	public void constraintViolation() throws Exception {
		Customer c = new Customer();
		c.setName("Foo Bar");
		c.setCreditCard("1234567890xxxx");
		manual.begin();
		try {
			entityManager.persist(c);
			manual.commit();
		} catch(Exception e) {
			manual.rollback();
			throw e;
		}
	}

}
