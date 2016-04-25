package fr.unice.polytech.isa.tcf.persistence;

import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.entities.*;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.RollbackException;
import javax.transaction.UserTransaction;

@RunWith(Arquillian.class)

public class CascadingTest extends AbstractTCFTest {

	@PersistenceContext private EntityManager entityManager;
	@Resource private UserTransaction manual;

	/****************************
	 * Cascading-related cases **
	 ****************************/

	@Test(expected = RollbackException.class)
	public void cannotStoreOrderWithTransientCustomer() throws Exception {
		Order order = new Order(new Customer("John Doe", "1234567890"));
		order.setStatus(OrderStatus.IN_PROGRESS);
		order.addItem(new Item(Cookies.CHOCOLALALA, 3));
		manual.begin();
			entityManager.persist(order); // the customer is not persistent => the order cannot persist by itself
		manual.commit();
	}


}
