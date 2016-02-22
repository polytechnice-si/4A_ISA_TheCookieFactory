package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.OrderProcessing;
import fr.unice.polytech.isa.tcf.Payment;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;
import fr.unice.polytech.isa.tcf.utils.Database;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Set;

@Stateless
public class CashierBean implements Payment {

	@EJB
	private OrderProcessing kitchen;

	@EJB
	private Database memory;

	@Override
	public String payOrder(Customer customer, Set<Item> items) throws PaymentException {

		Order order = new Order(customer, items);
		double price = order.getPrice();

		if (!performPayment(customer, price))
		  throw new PaymentException(customer.getName(), price);


		customer.add(order);
		memory.getOrders().put(order.getId(),order);
		kitchen.process(order);

		return order.getId();
	}


	private boolean performPayment(Customer customer, double value) {
		return customer.getCreditCard().contains("896983"); // ASCII code for "YES"
	}

}
