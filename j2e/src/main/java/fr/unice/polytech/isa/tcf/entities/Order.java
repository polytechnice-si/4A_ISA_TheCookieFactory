package fr.unice.polytech.isa.tcf.entities;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public class Order implements Serializable {

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


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Order)) return false;
		Order order = (Order) o;
		if (!getId().equals(order.getId())) return false;
		if (!getCustomer().equals(order.getCustomer())) return false;
		if (!getItems().equals(order.getItems())) return false;
		return getStatus() == order.getStatus();

	}

	@Override
	public int hashCode() {
		int result = getId().hashCode();
		result = 31 * result + getCustomer().hashCode();
		result = 31 * result + getItems().hashCode();
		result = 31 * result + getStatus().hashCode();
		return result;
	}
}
