package fr.unice.polytech.isa.tcf.managed;

import fr.unice.polytech.isa.tcf.CartModifier;
import fr.unice.polytech.isa.tcf.CartProcessor;
import fr.unice.polytech.isa.tcf.CatalogueExploration;
import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.exceptions.EmptyCartException;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean
@SessionScoped
public class OrderBean implements Serializable {

	/***************************
	 ** Components references **
	 ***************************/

	@EJB private transient CustomerFinder finder;
	@EJB(name = "cart-stateless") private transient CartModifier cartManager;
	@EJB(name = "cart-stateless") private transient CartProcessor cartProcessor;
	@EJB private transient CatalogueExploration catalogue;

    private static final Logger log = Logger.getLogger(OrderBean.class.getName());

	/****************************
	 ** Concrete Data bindings **
	 ****************************/

	@ManagedProperty("#{customerBean.name}")
	private String customerName;

	private Customer customer;
	private Cookies cookie;
	private int quantity;
	private String orderId;

	public void setCustomerName(String customerName) { this.customerName = customerName; }
	public String getCustomerName() { return customerName; }


	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }

	// Initializing the customer after the construction of the bean
	@PostConstruct private void loadCustomer() {
        finder.findByName(getCustomerName()).ifPresent(c -> this.customer = c );
	}


	public Cookies getCookie() { return cookie; }
	public void setCookie(Cookies cookie) { this.cookie = cookie; }


	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }


	public String getOrderId() { return orderId; }
	public void setOrderId(String orderId) { this.orderId = orderId; }

	/***************************
	 ** Virtual Data bindings **
	 ***************************/

	public List<Item> getCart() {
		return new ArrayList<Item>(cartProcessor.contents(getCustomer()));
	}

	public List<Cookies> getRecipes() {
		return new ArrayList<Cookies>(catalogue.listPreMadeRecipes());
	}


	/*************
	 ** Actions **
	 *************/

	public String add() {
		cartManager.add(getCustomer(),new Item(getCookie(),getQuantity()));
		return Signal.ADDED;
	}

	public String remove() {
		cartManager.remove(getCustomer(),new Item(getCookie(),getQuantity()));
		return Signal.REMOVED;
	}

	public String process() {
		try {
			setOrderId(cartProcessor.validate(getCustomer()));
			return Signal.ORDERED;
		} catch (PaymentException e) {
            log.log(Level.WARNING,"payment error", e);
			FacesContext.getCurrentInstance().addMessage("order-error", new FacesMessage("Payment error!"));
			return Signal.PAYMENT_ERROR;
		} catch (EmptyCartException ece) {
            log.log(Level.WARNING,"Empty cart, cannot proceed", ece);
			FacesContext.getCurrentInstance().addMessage("order-error", new FacesMessage("Cannot validate an empty cart!"));
			return Signal.EMPTY_CART;
		}
	}

}
