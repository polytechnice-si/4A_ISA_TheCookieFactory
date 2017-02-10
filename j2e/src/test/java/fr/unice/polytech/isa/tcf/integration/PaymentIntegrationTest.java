package fr.unice.polytech.isa.tcf.integration;


import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.CustomerRegistration;
import fr.unice.polytech.isa.tcf.Payment;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class PaymentIntegrationTest extends AbstractTCFTest {

	@EJB private Payment cashier;
	@EJB private CustomerFinder finder;
	@EJB private CustomerRegistration registration;

	@PersistenceContext	private EntityManager entityManager;

	private Set<Item> items;

	@Before
	public void setUpContext() {
		items = new HashSet<>();
		items.add(new Item(Cookies.CHOCOLALALA, 3));
		items.add(new Item(Cookies.DARK_TEMPTATION, 2));
	}

	@Test
	public void integrationBetweenCustomersAndOrders() throws Exception {
		registration.register("john doe", "1234896983");
		Customer retrieved = finder.findByName("john doe").get();
		retrieved = entityManager.merge(retrieved);
		assertTrue(retrieved.getOrders().isEmpty());
		String id = cashier.payOrder(retrieved, items);
		Order order = entityManager.find(Order.class, Integer.parseInt(id));
		assertTrue(retrieved.getOrders().contains(order));
	}


}
