package fr.unice.polytech.isa.tcf.exceptions;

import javax.xml.ws.WebFault;
import java.io.Serializable;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf/customer-care")
public class AlreadyExistingCustomerException extends Exception implements Serializable {

	private String conflictingName;

	public AlreadyExistingCustomerException(String name) {
		super(name);
		conflictingName = name;
	}


	public String getConflictingName() {
		return conflictingName;
	}

	public void setConflictingName(String conflictingName) {
		this.conflictingName = conflictingName;
	}
}
