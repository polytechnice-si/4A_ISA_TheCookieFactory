package fr.unice.polytech.isa.tcf;


import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CashierTest extends AbstractTCFTest {

	@EJB private Payment cashier;

	private Set<Item> items;

	@Before
	public void setUpContext() {
		memory.flush();
		items = new HashSet<>();
		items.add(new Item(Cookies.CHOCOLALALA, 3));
		items.add(new Item(Cookies.DARK_TEMPTATION, 2));
	}

	@Test
	public void processToPayment() throws Exception {
		// paying order
		Customer john = new Customer("john", "1234-896983");  // ends with the secret YES Card number
		String id = cashier.payOrder(john, items);

		// memory contents
		Order order = memory.getOrders().get(id);
		assertNotNull(order);
		assertEquals(john, order.getCustomer());
		assertEquals(items, order.getItems());
		double price = (3 * Cookies.CHOCOLALALA.getPrice()) + (2 * Cookies.DARK_TEMPTATION.getPrice());
		assertEquals(price, order.getPrice(), 0.0);
	}

	@Test(expected = PaymentException.class)
	public void identifyPaymentError() throws Exception {
		// paying order
		Customer pat = new Customer("pat", "1234-567890");  // will be rejected by the payment service
		String id = cashier.payOrder(pat, items);
	}

}
