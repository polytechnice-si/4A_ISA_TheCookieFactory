package fr.unice.polytech.isa.tcf;


import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;

import javax.ejb.Local;
import java.util.Optional;

@Local
public interface CustomerRegistry {

	void register(String name, String creditCard) throws AlreadyExistingCustomerException;

	Optional<Customer> findByName(String name);

}
