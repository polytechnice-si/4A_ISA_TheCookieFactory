package fr.unice.polytech.isa.tcf;

import fr.unice.polytech.isa.tcf.entities.OrderStatus;

import javax.ejb.Local;

@Local
public interface Tracker {

	OrderStatus status(String orderId);

}
