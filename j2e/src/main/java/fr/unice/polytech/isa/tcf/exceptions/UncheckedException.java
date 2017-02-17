package fr.unice.polytech.isa.tcf.exceptions;


public class UncheckedException extends RuntimeException {

    public UncheckedException(Exception e) {
        super(e);
    }

    public UncheckedException(String msg, Exception e) {
        super(msg, e);
    }

}
