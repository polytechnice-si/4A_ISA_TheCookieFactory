package fr.unice.polytech.isa.tcf;


import fr.unice.polytech.isa.tcf.entities.Order;

import javax.ejb.Local;

@Local
public interface OrderProcessing {

	String process(Order order);

	void moveForward(Order c);


}
