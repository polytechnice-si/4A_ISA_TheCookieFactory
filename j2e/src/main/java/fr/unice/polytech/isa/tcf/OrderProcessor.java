package fr.unice.polytech.isa.tcf;


import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;

import java.util.Set;
import java.util.UUID;

public interface OrderProcessor {

	String process(Customer customer, Set<Item> items);

	void moveForward(Customer c);

}
