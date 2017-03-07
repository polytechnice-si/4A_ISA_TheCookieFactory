package fr.unice.polytech.isa.tcf.business;


import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.ControlledPayment;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;
import fr.unice.polytech.isa.tcf.utils.BankAPI;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CashierTest extends AbstractTCFTest {

	@EJB private ControlledPayment cashier;
	@PersistenceContext private EntityManager entityManager;
	@Inject	private UserTransaction utx;

	// Test context
	private Set<Item> items;
	private Customer john;
	private Customer pat;

	@Before
	public void setUpContext() throws Exception {
		initData();
		initMock();
	}

	@After
	public void cleanUpContext() throws Exception {
		utx.begin();
			john = entityManager.merge(john);
			entityManager.remove(john);
			pat = entityManager.merge(pat);
			entityManager.remove(pat);
		utx.commit();
	}

	@Test
	public void processToPayment() throws Exception {
		// paying order
 		String id = cashier.payOrder(john, items);

		// memory contents from the Order point of view
		Order order = entityManager.find(Order.class, Integer.parseInt(id));
		john = entityManager.merge(john);

		assertNotNull(order);
		assertEquals(john, order.getCustomer());
		assertEquals(items, order.getItems());
		double price = (3 * Cookies.CHOCOLALALA.getPrice()) + (2 * Cookies.DARK_TEMPTATION.getPrice());
		assertEquals(price, order.getPrice(), 0.0);
	}

	@Test(expected = PaymentException.class)
	public void identifyPaymentError() throws Exception {
		cashier.payOrder(pat, items);
	}


	private void initData() throws Exception {
		items = new HashSet<>();
		items.add(new Item(Cookies.CHOCOLALALA, 3));
		items.add(new Item(Cookies.DARK_TEMPTATION, 2));
		john = new Customer("john", "1234896983");
		entityManager.persist(john);
		pat = new Customer("pat",  "1234567890");
		entityManager.persist(pat);
	}

	private void initMock() throws Exception {
		// Mocking the external partner
		BankAPI mocked = mock(BankAPI.class);
		cashier.useBankReference(mocked);
		when(mocked.performPayment(eq(john), anyDouble())).thenReturn(true);
		when(mocked.performPayment(eq(pat),  anyDouble())).thenReturn(false);
	}

}
