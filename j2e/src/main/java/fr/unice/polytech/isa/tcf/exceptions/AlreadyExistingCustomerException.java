package fr.unice.polytech.isa.tcf.exceptions;

import java.io.Serializable;


public class AlreadyExistingCustomerException extends Exception implements Serializable {

	private String conflictingName;

	public AlreadyExistingCustomerException(String name) {
		conflictingName = name;
	}


}
