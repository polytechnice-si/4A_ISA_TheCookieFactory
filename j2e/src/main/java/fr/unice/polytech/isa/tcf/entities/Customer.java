package fr.unice.polytech.isa.tcf.entities;


public class Customer {

	private String name;

	public Customer() {}
	public Customer(String n) { this.name = n; }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Customer)) return false;
		Customer customer = (Customer) o;
		return getName().equals(customer.getName());
	}

	@Override
	public int hashCode() {	return getName().hashCode(); }
}
