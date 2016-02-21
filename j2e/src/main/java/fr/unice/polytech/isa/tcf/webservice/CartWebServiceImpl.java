package fr.unice.polytech.isa.tcf.webservice;

import fr.unice.polytech.isa.tcf.Cart;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Set;

@WebService(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf")
@Stateless(name = "CartWS")
public class CartWebServiceImpl implements CartWebService {

	@EJB(name="stateless-cart") Cart cart;

	@Override
	public void addItemToCustomerCart(String customerName, Item it) {
		cart.add(new Customer(customerName), it);
	}

	@Override
	public Set<Item> getCustomerCartContents(String customerName) {
		return cart.contents(new Customer(customerName));
	}
}
