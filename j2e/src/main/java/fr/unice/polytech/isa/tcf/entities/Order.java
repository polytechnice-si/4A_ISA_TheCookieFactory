package fr.unice.polytech.isa.tcf.entities;

import java.util.Set;
import java.util.UUID;

public class Order {

	private String id;
	private Customer customer;
	private Set<Item> items;
	private double price;
	private OrderStatus status;

	public Order(Customer customer, Set<Item> items, double price) {
		this.customer = customer;
		this.items = items;
		this.price = price;
		this.status = OrderStatus.VALIDATED;
		this.id = UUID.randomUUID().toString();
	}

	public OrderStatus getStatus() { return status; }
	public void setStatus(OrderStatus status) { this.status = status; }
	public String getId() { return id; }
	public Customer getCustomer() { return customer; }
	public Set<Item> getItems() { return items; }
	public double getPrice() { return price; }

}
