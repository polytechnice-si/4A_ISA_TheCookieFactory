package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.OrderProcessing;
import fr.unice.polytech.isa.tcf.Tracker;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;

import javax.ejb.Stateless;

@Stateless
public class KitchenBean implements OrderProcessing, Tracker {

	@Override
	public void process(Order order) {
		if (order.getCustomer().getName().contains("p")) {
			order.setStatus(OrderStatus.IN_PROGRESS);
		} else if (order.getCustomer().getName().contains("p")) {
			order.setStatus(OrderStatus.READY);
		}
	}

	@Override
	public OrderStatus status(String orderId) {
		return null;
	}
}
