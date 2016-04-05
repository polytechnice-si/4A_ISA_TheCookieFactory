package fr.unice.polytech.isa.tcf.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NotNull
	private String name;

	@NotNull
	@Pattern(regexp = "\\d{10}+", message = "Invalid creditCardNumber")
	private String creditCard;

	@OneToMany(mappedBy = "customer")
	private Set<Order> orders = new HashSet<>();

	public Customer() {}
	public Customer(String n, String c) { this.name = n; this.creditCard = c; }

	public int getId() {
		return id;
	}

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

		if (getName() != null ? !getName().equals(customer.getName()) : customer.getName() != null) return false;
		if (getCreditCard() != null ? !getCreditCard().equals(customer.getCreditCard()) : customer.getCreditCard() != null)
			return false;
		return getOrders() != null ? getOrders().equals(customer.getOrders()) : customer.getOrders() == null;

	}

	@Override
	public int hashCode() {
		int result = getName() != null ? getName().hashCode() : 0;
		result = 31 * result + (getCreditCard() != null ? getCreditCard().hashCode() : 0);
		result = 31 * result + (getOrders() != null ? getOrders().hashCode() : 0);
		return result;
	}
}
