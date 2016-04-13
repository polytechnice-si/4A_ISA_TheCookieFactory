package cli.commands;

import api.TCFPublicAPI;
import cli.framework.Command;
import stubs.cart.Item;

import java.util.List;

public class ShowCart extends Command<TCFPublicAPI> {

	private String customerName;

	@Override
	public String identifier() { return "cart"; }

	@Override
	public void execute() throws Exception {
		List<Item> cart = shell.system.carts.getCustomerCartContents(customerName);
		if (cart.isEmpty()){
			System.out.println("  Empty cart");
		} else {
			for(Item i: cart) {
				System.out.println("  " + i.getQuantity() + "x" + i.getCookie());
			}
		}
	}

	@Override
	public void load(List<String> args) { customerName = args.get(0); }

	@Override
	public String describe() {
		return "show the cart contents for a given customer (cart CUSTOMER_NAME)";
	}
}
