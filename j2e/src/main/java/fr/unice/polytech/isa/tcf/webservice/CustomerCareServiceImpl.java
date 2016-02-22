package fr.unice.polytech.isa.tcf.webservice;


import fr.unice.polytech.isa.tcf.CustomerRegistry;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

@WebService(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/customer-care")
@Stateless(name = "CustomerCareWS")
public class CustomerCareServiceImpl implements CustomerCareService {

	@EJB
	CustomerRegistry registry;

	@Override
	public void register(String name, String creditCard) throws AlreadyExistingCustomerException {
		registry.register(name, creditCard);
	}

}
