package cli.commands;

import api.TCFPublicAPI;
import cli.framework.Command;

import java.util.List;

public class RegisterCustomer extends Command<TCFPublicAPI> {

	private String customerName;
	private String creditCardNumber;

	@Override
	public String identifier() { return "register"; }

	@Override
	public void load(List<String> args) {
		customerName = args.get(0);
		creditCardNumber = args.get(1);
	}

	@Override
	public void execute() throws Exception {
		shell.system.ccs.register(customerName, creditCardNumber);
	}

	@Override
	public String describe() {
		return "Register a customer in the CoD backend (register CUSTOMER_NAME CREDIT_CARD_NUMBER)";
	}
}
