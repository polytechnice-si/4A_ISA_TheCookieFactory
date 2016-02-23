package fr.unice.polytech.isa.tcf.webservice;


import fr.unice.polytech.isa.tcf.CatalogueExploration;
import fr.unice.polytech.isa.tcf.CustomerRegistration;
import fr.unice.polytech.isa.tcf.Tracker;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;
import fr.unice.polytech.isa.tcf.exceptions.UnknownOrderId;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Set;

@WebService(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/customer-care")
@Stateless(name = "CustomerCareWS")
public class CustomerCareServiceImpl implements CustomerCareService {

	@EJB private CustomerRegistration registry;
	@EJB private CatalogueExploration catalogue;
	@EJB private Tracker tracker;

	@Override
	public void register(String name, String creditCard) throws AlreadyExistingCustomerException {
		registry.register(name, creditCard);
	}

	@Override
	public OrderStatus track(String orderId) throws UnknownOrderId {
		return tracker.status(orderId);
	}

	@Override
	public Set<Cookies> listAllRecipes() {
		return catalogue.listPreMadeRecipes() ;
	}

}
