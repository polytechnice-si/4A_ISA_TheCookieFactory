package fr.unice.polytech.isa.tcf;

import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;
import fr.unice.polytech.isa.tcf.interceptors.CartCounter;

import javax.ejb.Local;
import javax.interceptor.Interceptors;
import java.util.Set;

@Local
public interface CartModifier {

	boolean add(Customer c, Item item);

	boolean remove(Customer c, Item item);

	Set<Item> contents(Customer c);

	String validate(Customer c) throws PaymentException;

}
