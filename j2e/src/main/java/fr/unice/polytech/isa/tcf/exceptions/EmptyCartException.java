package fr.unice.polytech.isa.tcf.exceptions;


import java.io.Serializable;

public class EmptyCartException extends Exception implements Serializable {

	private String name;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public EmptyCartException() {}
	public EmptyCartException(String customerName) { this.name = customerName; }

}
