package fr.unice.polytech.isa.tcf.exceptions;

import java.io.Serializable;


public class UnknownCustomerException extends Exception implements Serializable {

	private String name;

	public UnknownCustomerException(String name) {
		name = name;
	}


}
