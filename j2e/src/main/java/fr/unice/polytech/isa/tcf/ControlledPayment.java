package fr.unice.polytech.isa.tcf;



import fr.unice.polytech.isa.tcf.utils.BankAPI;

import javax.ejb.Local;

@Local
public interface ControlledPayment extends Payment{

	void useBankReference(BankAPI bank);
}
