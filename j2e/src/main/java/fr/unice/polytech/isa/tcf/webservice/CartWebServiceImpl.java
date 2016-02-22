package fr.unice.polytech.isa.tcf.webservice;

import fr.unice.polytech.isa.tcf.Cart;
import fr.unice.polytech.isa.tcf.CustomerRegistry;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.exceptions.UnknownCustomerException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Optional;
import java.util.Set;

@WebService(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/cart")
@Stateless(name = "CartWS")
public class CartWebServiceImpl implements CartWebService {

	@EJB(name="stateless-cart")
	Cart cart;

	@EJB
	CustomerRegistry registry;

	@Override
	public void addItemToCustomerCart(String customerName, Item it) throws UnknownCustomerException {
		Optional<Customer> c = registry.findByName(customerName);
		if(!c.isPresent())
			throw new UnknownCustomerException(customerName);
		cart.add(c.get(), it);
	}

	@Override
	public Set<Item> getCustomerCartContents(String customerName) throws UnknownCustomerException {
		Optional<Customer> c = registry.findByName(customerName);
		if(!c.isPresent())
			throw new UnknownCustomerException(customerName);
		return cart.contents(c.get());
	}
}
