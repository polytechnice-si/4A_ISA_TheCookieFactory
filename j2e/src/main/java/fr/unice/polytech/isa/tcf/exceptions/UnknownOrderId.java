package fr.unice.polytech.isa.tcf.exceptions;

import java.io.Serializable;

public class UnknownOrderId extends Exception implements Serializable {

	private String orderId;

	public UnknownOrderId(String id) {
		orderId = id;
	}


	public UnknownOrderId() {
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
