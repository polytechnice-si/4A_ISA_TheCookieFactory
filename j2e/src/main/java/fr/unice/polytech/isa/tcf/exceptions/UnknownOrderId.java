package fr.unice.polytech.isa.tcf.exceptions;

import javax.xml.ws.WebFault;
import java.io.Serializable;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/customer-care")
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
