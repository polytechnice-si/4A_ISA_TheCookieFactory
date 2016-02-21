package fr.unice.polytech.isa.tcf;

import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;

import javax.ejb.Local;
import java.util.Set;

@Local
public interface Cart {

	boolean add(Customer c, Item item);

	boolean remove(Customer c, Item item);

	Set<Item> contents(Customer c);

}
