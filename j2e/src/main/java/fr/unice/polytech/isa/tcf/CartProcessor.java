package fr.unice.polytech.isa.tcf;


import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.exceptions.EmptyCartException;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;

import javax.ejb.Local;
import java.util.Set;

@Local
public interface CartProcessor {

    Set<Item> contents(Customer c);

    double price(Customer c);

    String validate(Customer c) throws PaymentException, EmptyCartException;


}
