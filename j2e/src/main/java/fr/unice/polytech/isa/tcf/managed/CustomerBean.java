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
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean
@SessionScoped
public class CustomerBean implements Serializable {


    @EJB private transient CustomerFinder finder;
    @EJB private transient CustomerRegistration registry;

    private static final Logger log = Logger.getLogger(CustomerBean.class.getName());

    private String name;
    private String creditCard;

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getCreditCard() { return creditCard; }
	public void setCreditCard(String creditCard) { this.creditCard = creditCard; }

	// Invoked when the "Select" button is pushed
	public String select() {
		if (finder.findByName(getName()).isPresent()) {
			return Signal.SELECTED;
		} else {
			FacesContext.getCurrentInstance()
					.addMessage("form-error", new FacesMessage("Unknown customer: " + getName()));
			return Signal.UNKOWN;
		}
	}

	// Invoked when the "Register" button is pushed
	public String register() {
		try {
			registry.register(getName(), getCreditCard());
			return Signal.ADDED;
		} catch (AlreadyExistingCustomerException e) {
            log.log(Level.WARNING, "Unknown customer", e);
			FacesContext.getCurrentInstance()
					.addMessage("form-error", new FacesMessage("Customer " + getName() + " already exists!"));
			return Signal.EXISTING;
		}
	}

}
