package fr.unice.polytech.isa.tcf;

import fr.unice.polytech.isa.tcf.entities.Customer;

import javax.ejb.Local;
import java.util.Optional;

@Local
public interface CustomerFinder {

	Optional<Customer> findByName(String name);

}

