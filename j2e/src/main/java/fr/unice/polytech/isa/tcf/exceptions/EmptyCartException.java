package fr.unice.polytech.isa.tcf.exceptions;

import javax.xml.ws.WebFault;
import java.io.Serializable;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/cart")
public class EmptyCartException extends Exception implements Serializable {

	private String name;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public EmptyCartException() {}
	public EmptyCartException(String customerName) { this.name = customerName; }

}
