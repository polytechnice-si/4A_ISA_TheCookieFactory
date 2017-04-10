package fr.unice.polytech.isa.tcf.exceptions;


import javax.xml.ws.WebFault;
import java.io.Serializable;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/cart")
public class PaymentException extends Exception implements Serializable {

	private String name;
	private double amount;

	public PaymentException(String customerName, double amount) {
		this.name = customerName;
		this.amount = amount;
	}

    public PaymentException(String customerName, double amount, Exception source) {
        super(source);
        this.name = customerName;
        this.amount = amount;
    }

	public PaymentException() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
