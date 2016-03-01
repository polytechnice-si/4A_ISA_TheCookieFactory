package fr.unice.polytech.isa.tcf.exceptions;

import java.io.Serializable;

public class ExternalPartnerException extends Exception implements Serializable {


	public ExternalPartnerException() {}

	public ExternalPartnerException(String n) {super(n); }

	public ExternalPartnerException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String toString() {
		return "ExternalPartnerException on " + getMessage() + " ->" + getCause().toString();
	}
}
