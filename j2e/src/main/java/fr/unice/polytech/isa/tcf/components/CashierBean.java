package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.OrderProcessing;
import fr.unice.polytech.isa.tcf.Payment;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Set;
import java.util.UUID;


@Stateless
public class CashierBean implements Payment {

	@EJB
	OrderProcessing kitchen;

	@Override
	public String payOrder(Customer customer, Set<Item> items) throws PaymentException {
		double price = computePrice(items);

		if (!performPayment(customer, price))
		  throw new PaymentException(customer.getName(), price);

		Order order = new Order(customer, items, price);
		kitchen.process(order);

		return order.getId();
	}


	private boolean performPayment(Customer customer, double value) {
		return customer.getCreditCard().contains("896983"); // ASCII code for "YES"
	}

	private double computePrice(Set<Item> items) {
		double result = 0.0;
		for(Item item: items) {
			result += (item.getQuantity() * item.getCookie().getPrice());
		}
		return result;
	}

}
