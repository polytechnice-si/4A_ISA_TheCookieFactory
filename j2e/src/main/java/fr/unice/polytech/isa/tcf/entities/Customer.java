package fr.unice.polytech.isa.tcf.entities;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Customer implements Serializable {

	private String name;
	private String creditCard;
	private Set<Order> orders = new HashSet<>();

	public Customer() {}
	public Customer(String n, String c) { this.name = n; this.creditCard = c; }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCreditCard() { return creditCard; }
	public void setCreditCard(String creditCard) { this.creditCard = creditCard; }

	public void add(Order o) { this.orders.add(o); }
	public Set<Order> getOrders() { return orders; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Customer)) return false;
		Customer customer = (Customer) o;
		if (!getName().equals(customer.getName())) return false;
		if (!getCreditCard().equals(customer.getCreditCard())) return false;
		return orders.equals(customer.orders);

	}

	@Override
	public int hashCode() {
		int result = getName().hashCode();
		result = 31 * result + getCreditCard().hashCode();
		return result;
	}
}
