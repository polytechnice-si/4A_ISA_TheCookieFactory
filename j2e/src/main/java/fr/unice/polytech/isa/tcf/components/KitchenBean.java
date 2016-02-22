package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.OrderProcessing;
import fr.unice.polytech.isa.tcf.Tracker;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;

import javax.ejb.Stateless;

@Stateless
public class KitchenBean implements OrderProcessing, Tracker {


	@Override
	public String process(Order order) {
		return null;
	}

	@Override
	public void moveForward(Order c) {

	}

	@Override
	public OrderStatus status(String orderId) {
		return null;
	}
}
