package fr.unice.polytech.isa.tcf.webservice;

import fr.unice.polytech.isa.tcf.CartModifier;
import fr.unice.polytech.isa.tcf.CartProcessor;
import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.exceptions.EmptyCartException;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;
import fr.unice.polytech.isa.tcf.exceptions.UnknownCustomerException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Optional;
import java.util.Set;

@WebService(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/cart")
@Stateless(name = "CartWS")
public class CartWebServiceImpl implements CartWebService {

	@EJB(name="stateless-cart") private CartModifier cart;
	@EJB(name="stateless-cart") private CartProcessor processor;
	@EJB private CustomerFinder finder;

	@Override
	public void addItemToCustomerCart(String customerName, Item it)
			throws UnknownCustomerException {
		cart.add(readCustomer(customerName), it);
	}

	@Override
	public void removeItemToCustomerCart(String customerName, Item it)
			throws UnknownCustomerException {
		cart.remove(readCustomer(customerName), it);
	}

	@Override
	public Set<Item> getCustomerCartContents(String customerName)
			throws UnknownCustomerException {
		return processor.contents(readCustomer(customerName));
	}

	@Override
	public String validate(String customerName)
			throws PaymentException, EmptyCartException, UnknownCustomerException {
		return processor.validate(readCustomer(customerName));
	}

	private Customer readCustomer(String customerName)
			throws UnknownCustomerException {
		Optional<Customer> c = finder.findByName(customerName);
		if(!c.isPresent())
			throw new UnknownCustomerException(customerName);
		return c.get();
	}

}
