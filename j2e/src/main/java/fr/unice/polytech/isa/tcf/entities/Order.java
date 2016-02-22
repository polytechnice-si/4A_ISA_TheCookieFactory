package fr.unice.polytech.isa.tcf.entities;

import java.util.Set;
import java.util.UUID;

public class Order {

	private String id;
	private Customer customer;
	private Set<Item> items;
	private OrderStatus status;

	public Order(Customer customer, Set<Item> items) {
		this.customer = customer;
		this.items = items;
		this.status = OrderStatus.VALIDATED;
		this.id = UUID.randomUUID().toString();
	}

	public OrderStatus getStatus() { return status; }
	public void setStatus(OrderStatus status) { this.status = status; }
	public String getId() { return id; }
	public Customer getCustomer() { return customer; }
	public Set<Item> getItems() { return items; }

	public double getPrice() {
		double result = 0.0;
		for(Item item: items) {
			result += (item.getQuantity() * item.getCookie().getPrice());
		}
		return result;
	}

}
