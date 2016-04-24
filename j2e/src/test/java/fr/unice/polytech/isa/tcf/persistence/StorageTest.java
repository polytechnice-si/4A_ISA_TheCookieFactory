package fr.unice.polytech.isa.tcf.persistence;


import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.entities.*;
import org.apache.openjpa.persistence.InvalidStateException;
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

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class StorageTest extends AbstractTCFTest {

	@PersistenceContext private EntityManager entityManager;

	/*******************************
	 ** Regular persistence cases **
	 *******************************/

	@Test
	public void storingCustomer() throws Exception {
		Customer c = new Customer("John Doe", "1234567890");
		assertEquals(0,c.getId());

		entityManager.persist(c);
		int id = c.getId();
		assertNotEquals(0,id);

		Customer stored = (Customer) entityManager.find(Customer.class, id);
		assertEquals(c, stored);
	}

	@Test
	public void storingOrder() throws Exception {
		Customer c = new Customer("John Doe", "1234567890");
		entityManager.persist(c);

		Order order = new Order(c);
		order.setStatus(OrderStatus.IN_PROGRESS);
		order.addItem(new Item(Cookies.CHOCOLALALA, 3));
		order.addItem(new Item(Cookies.DARK_TEMPTATION, 2));
		assertEquals(0, order.getId());

		entityManager.persist(order);
		int id = order.getId();
		assertNotEquals(0, id);

		Order stored = (Order) entityManager.find(Order.class, id);
		assertEquals(c, stored.getCustomer());
		assertEquals(2, stored.getItems().size());
		assertEquals(order, stored); // the alltogether
	}

	@Test
	public void updatingCustomer() throws Exception {
		Customer john = new Customer("John Doe", "1234567890");
		entityManager.persist(john);

		Customer storedC = (Customer) entityManager.find(Customer.class, john.getId());
		assertEquals(0, storedC.getOrders().size());

		Order order = new Order(john);
		order.setStatus(OrderStatus.IN_PROGRESS);
		order.addItem(new Item(Cookies.CHOCOLALALA, 3));
		order.addItem(new Item(Cookies.DARK_TEMPTATION, 2));
		entityManager.persist(order);

		john.add(order);

		// the persistence context is shared => modifications are propagated
		assertEquals(1, john.getOrders().size());
		assertEquals(1, storedC.getOrders().size());
		assertEquals(john.getOrders(), storedC.getOrders());

		Order storedO = (Order) entityManager.find(Order.class, order.getId());
		assertEquals(order, storedO);
	}

	@Test
	public void removingCustomer() throws Exception {
		Customer john = new Customer("John Doe", "1234567890");
		entityManager.persist(john);
		int johnId = john.getId();
		Order order = new Order(john);
		order.setStatus(OrderStatus.IN_PROGRESS);
		order.addItem(new Item(Cookies.CHOCOLALALA, 3));
		entityManager.persist(order);
		int orderId = order.getId();
		john.add(order);

		assertNotNull(entityManager.find(Customer.class, john.getId()));
		assertNotNull(entityManager.find(Order.class, order.getId()));

		entityManager.remove(john);

		assertNull(entityManager.find(Customer.class, john.getId()));
		assertNull(entityManager.find(Order.class, order.getId()));
	}

	@Test
	public void removingOrderInCustomer() throws Exception {
		Customer john = new Customer("John Doe", "1234567890");
		entityManager.persist(john);
		Order o1 = new Order(john);
		o1.setStatus(OrderStatus.IN_PROGRESS);
		o1.addItem(new Item(Cookies.CHOCOLALALA, 3));
		entityManager.persist(o1);
		john.add(o1);

		Order o2 = new Order(john);
		o2.setStatus(OrderStatus.IN_PROGRESS);
		o2.addItem(new Item(Cookies.DARK_TEMPTATION, 3));
		entityManager.persist(o2);
		john.add(o2);

		int io1 = o1.getId();
		john.getOrders().remove(o1);
		entityManager.remove(o1);

		assertNull(entityManager.find(Order.class, io1));
		assertNotNull(entityManager.find(Order.class, o2.getId()));
		assertEquals(1, john.getOrders().size());
		assertEquals(o2, john.getOrders().toArray()[0]);
		assertEquals(john, entityManager.find(Customer.class, john.getId()));
	}

}
