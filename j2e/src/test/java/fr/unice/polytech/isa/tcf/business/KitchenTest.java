package fr.unice.polytech.isa.tcf.business;


import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.OrderProcessing;
import fr.unice.polytech.isa.tcf.Tracker;
import fr.unice.polytech.isa.tcf.entities.*;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class KitchenTest extends AbstractTCFTest {


	@EJB private OrderProcessing processor;
	@EJB private Tracker tracker;

	@PersistenceContext private EntityManager entityManager;

	private Customer custProgress;
	private Order inProgress;
	private Customer custReady;
	private Order ready;

	@Before
	public void setUpContext() throws Exception {
		// Cookies to order
		Set<Item> items = new HashSet<>();
		items.add(new Item(Cookies.CHOCOLALALA, 3));
		items.add(new Item(Cookies.DARK_TEMPTATION, 2));

		// Customer with a "p" => Order is "in Progress"
		custProgress = new Customer("pat", "1234567890");
		entityManager.persist(custProgress);

		inProgress = new Order(custProgress, items);
		custProgress.add(inProgress);
		entityManager.persist(inProgress);

		// Customer with an "R" => Order is "Ready"
		custReady = new Customer("ron", "1234567890");
		entityManager.persist(custReady);

		ready = new Order(custReady, items);
		custReady.add(ready);
		entityManager.persist(ready);
	}

	@After
	public void cleanUp() throws Exception {
		custProgress = entityManager.merge(custProgress);
		entityManager.remove(custProgress);
		custReady = entityManager.merge(custReady);
		entityManager.remove(custReady);
	}

	@Test
	public void processCommand() throws Exception {
		processor.process(inProgress);
		assertEquals(OrderStatus.IN_PROGRESS, tracker.status(""+inProgress.getId()));

		processor.process(ready);
		assertEquals(OrderStatus.READY, tracker.status(""+ready.getId()));
	}




}
