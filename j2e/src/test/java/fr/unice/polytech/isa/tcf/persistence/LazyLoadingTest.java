package fr.unice.polytech.isa.tcf.persistence;


import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Order;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class LazyLoadingTest extends AbstractTCFTest  {

	@PersistenceContext private EntityManager entityManager;
	@Resource  private UserTransaction manual;

	/********************************
	 ** Lazy loading demonstration **
	 ********************************/

	@Test
	public void lazyLoadingDemo() throws Exception {
		// Code executed inside a given transaction
		manual.begin();
			Customer john = new Customer("John Doe", "1234567890");
			entityManager.persist(john);
			Order o1 = new Order(john, Cookies.CHOCOLALALA, 3); entityManager.persist(o1); john.add(o1);
			Order o2 = new Order(john, Cookies.DARK_TEMPTATION, 1); entityManager.persist(o2); john.add(o2);
			Order o3 = new Order(john, Cookies.SOO_CHOCOLATE, 2); entityManager.persist(o3); john.add(o3);
			Customer sameTransaction = loadCustomer(john.getId()) ;
			assertEquals(john, sameTransaction);
			assertEquals(3, john.getOrders().size()); // orders are attached in this transaction => available
		manual.commit();

		// Code executed outside the given transaction
		Customer detached = loadCustomer(john.getId()) ;
		assertNotEquals(john, detached);
		assertNull(detached.getOrders()); // orders are not attached outside of the transaction => null;
	}


	private Customer loadCustomer(int id) {
		return entityManager.find(Customer.class, id);
	}

}
