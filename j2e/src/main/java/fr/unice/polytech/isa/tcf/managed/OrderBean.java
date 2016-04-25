package fr.unice.polytech.isa.tcf.managed;

import fr.unice.polytech.isa.tcf.CartModifier;
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

@ManagedBean
@SessionScoped
public class OrderBean implements Serializable {

	/***************************
	 ** Components references **
	 ***************************/

	@EJB private CustomerFinder finder;
	@EJB(name = "cart-stateless") private CartModifier cartManager;
	@EJB private CatalogueExploration catalogue;

	/****************************
	 ** Concrete Data bindings **
	 ****************************/

	@ManagedProperty("#{customerBean.name}")
	private String customerName;
	public void setCustomerName(String customerName) { this.customerName = customerName; }
	public String getCustomerName() { return customerName; }

	private Customer customer;
	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	// Initializing the customer after the construction of the bean
	@PostConstruct private void loadCustomer() { this.customer = finder.findByName(getCustomerName()).get(); }

	private Cookies cookie;
	public Cookies getCookie() { return cookie; }
	public void setCookie(Cookies cookie) { this.cookie = cookie; }

	private int quantity;
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }

	private String orderId;
	public String getOrderId() { return orderId; }
	public void setOrderId(String orderId) { this.orderId = orderId; }

	/***************************
	 ** Virtual Data bindings **
	 ***************************/

	public List<Item> getCart() throws Exception { return new ArrayList<Item>(cartManager.contents(getCustomer())); }
	public List<Cookies> getRecipes() { return new ArrayList<Cookies>(catalogue.listPreMadeRecipes()); }


	/*************
	 ** Actions **
	 *************/

	public String add() {
		cartManager.add(getCustomer(),new Item(getCookie(),getQuantity()));
		return "ADDED";
	}

	public String remove() {
		cartManager.remove(getCustomer(),new Item(getCookie(),getQuantity()));
		return "REMOVED";
	}

	public String process() {
		try {
			setOrderId(cartManager.validate(getCustomer()));
			return "ORDERED";
		} catch (PaymentException e) {
			FacesContext.getCurrentInstance().addMessage("order-error", new FacesMessage("Payment error!"));
			return "PAYMENT_ERROR";
		} catch (EmptyCartException ece) {
			FacesContext.getCurrentInstance().addMessage("order-error", new FacesMessage("Cannot validate an empty cart!"));
			return "EMPTY_CART";
		}
	}

}
