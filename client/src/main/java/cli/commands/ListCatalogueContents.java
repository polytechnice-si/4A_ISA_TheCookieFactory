package cli.commands;

import api.TCFPublicAPI;
import cli.framework.Command;
import stubs.customerCare.Cookies;

import java.util.List;

public class ListCatalogueContents extends Command<TCFPublicAPI> {

	@Override
	public String identifier() { return "recipes"; }

	@Override
	public void execute() {
		List<Cookies> recipes = shell.system.ccs.listAllRecipes();
		if (recipes.isEmpty()) {
			System.out.println("  No recipes available");
		} else {
			for(Cookies c: recipes) {
			  System.out.println("  " + c.name());
			}
		}
	}

	@Override
	public String describe() {
		return "List all available recipes";
	}

}
