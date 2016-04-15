package fr.unice.polytech.isa.tcf.managed;

import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.CustomerRegistration;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class CustomerBean implements Serializable {

	private String name;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	private String creditCard;
	public String getCreditCard() { return creditCard; }
	public void setCreditCard(String creditCard) { this.creditCard = creditCard; }

	@EJB private CustomerFinder finder;
	@EJB private CustomerRegistration registry;

	// Invoked when the "Select" button is pushed
	public String select() {
		if (finder.findByName(getName()).isPresent()) {
			return "SELECTED";
		} else {
			FacesContext.getCurrentInstance()
					.addMessage("form-error", new FacesMessage("Unknown customer: " + getName()));
			return "UNKNOWN";
		}
	}

	// Invoked when the "Register" button is pushed
	public String register() {
		try {
			registry.register(getName(), getCreditCard());
			return "ADDED";
		} catch (AlreadyExistingCustomerException e) {
			FacesContext.getCurrentInstance()
					.addMessage("form-error", new FacesMessage("Customer " + getName() + " already exists!"));
			return "EXISTING";
		}
	}

}
