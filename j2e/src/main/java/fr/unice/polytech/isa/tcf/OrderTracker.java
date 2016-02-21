package fr.unice.polytech.isa.tcf;

import fr.unice.polytech.isa.tcf.entities.OrderStatus;

public interface OrderTracker {

	OrderStatus status(String orderId);

}
