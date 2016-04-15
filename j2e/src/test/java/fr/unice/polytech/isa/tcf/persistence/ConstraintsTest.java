package fr.unice.polytech.isa.tcf.persistence;


import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;

@RunWith(Arquillian.class)
public class ConstraintsTest extends AbstractTCFTest {

	@PersistenceContext private EntityManager entityManager;
	@Resource private UserTransaction manual;

	/********************************
	 ** Constraint Violation cases **
	 ********************************/

	@Test(expected = ConstraintViolationException.class)
	public void cannotStoreCustomerWithBadCreditCard() throws Exception {
		Customer customer = new Customer();
		customer.setName("Foo Bar");
		customer.setCreditCard("1234567890xxxx");
		persistWithinTransaction(customer);
	}

	@Test(expected = ConstraintViolationException.class)
	public void cannotStoreOrderWithNoCustomer() throws Exception {
		Order order = new Order();
		order.setStatus(OrderStatus.IN_PROGRESS);
		persistWithinTransaction(order);
	}

	/**
	 * Asks the entity manager to persist a given object, within a manually handled transaction. Used to catch the
	 * real reason of a transactional error and avoid to hide it inside a RollbackException. Useful to assess that
	 * the root cause of an expected error is the right one.
	 */
	private void persistWithinTransaction(Object obj) throws Exception {
		manual.begin();
		try {
			entityManager.persist(obj); manual.commit();
		} catch(Exception e) {
			manual.rollback(); throw e;
		}
	}

}
