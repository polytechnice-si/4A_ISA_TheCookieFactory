package fr.unice.polytech.isa.tcf.webservice;

import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface CustomerCareService {

	@WebMethod
	void register(@WebParam(name="customer_name") String name,
				  @WebParam(name="credit_card_number") String creditCard)
			throws AlreadyExistingCustomerException;

}
