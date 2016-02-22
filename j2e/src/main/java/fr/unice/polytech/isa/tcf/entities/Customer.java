package fr.unice.polytech.isa.tcf.entities;


import java.util.HashSet;
import java.util.Set;

public class Customer {

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Customer)) return false;
		Customer customer = (Customer) o;
		if (!getName().equals(customer.getName())) return false;
		return getCreditCard().equals(customer.getCreditCard());

	}

	@Override
	public int hashCode() {
		int result = getName().hashCode();
		result = 31 * result + getCreditCard().hashCode();
		return result;
	}
}
