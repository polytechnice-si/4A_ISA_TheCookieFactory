package fr.unice.polytech.isa.tcf.webservice;

import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;
import fr.unice.polytech.isa.tcf.exceptions.UnknownOrderId;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.Set;

@WebService(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/customer-care")
public interface CustomerCareService {

	@WebMethod
	void register(@WebParam(name="customer_name") String name,
				  @WebParam(name="credit_card_number") String creditCard)
			throws AlreadyExistingCustomerException;


	@WebMethod
	@WebResult(name = "status")
	OrderStatus track(@WebParam(name="order_id") String orderId)
		throws UnknownOrderId;

	@WebMethod
	@WebResult(name = "recipes")
	Set<Cookies> listAllRecipes();

}
