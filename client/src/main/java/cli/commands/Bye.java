package cli.commands;

import api.TCFPublicAPI;
import cli.framework.Command;

public class Bye extends Command<TCFPublicAPI> {

	@Override
	public String identifier() { return "bye"; }

	@Override
	public void execute() { }

	@Override
	public String describe() {
		return "Exit Cookie on Demand";
	}

	@Override
	public boolean shouldContinue() { return false; }

}
