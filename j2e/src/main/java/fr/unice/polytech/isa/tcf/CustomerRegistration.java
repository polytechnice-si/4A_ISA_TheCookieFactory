package fr.unice.polytech.isa.tcf;


import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;

import javax.ejb.Local;

@Local
public interface CustomerRegistration {

	void register(String name, String creditCard)
			throws AlreadyExistingCustomerException;

}

