package cli.commands;

import api.TCFPublicAPI;
import cli.framework.Command;
import stubs.cart.Item;
import stubs.cart.Cookies;

import java.util.List;

public class OrderCookie extends Command<TCFPublicAPI> {

	protected String customerName;
	protected int quantity;
	protected Cookies cookie;

	@Override
	public String identifier() { return "order"; }

	@Override
	public void load(List<String> args) {
		customerName = args.get(0);
		quantity = Integer.parseInt(args.get(1));
		cookie   = Cookies.valueOf(args.get(2));
	}

	@Override
	public void execute() throws Exception {
		Item item = new Item();
		item.setCookie(cookie);
		item.setQuantity(quantity);
		system.carts.addItemToCustomerCart(customerName, item);
	}

	@Override
	public String describe() {
		return "Order some cookies for a given customer (order CUSTOMER QUANTITY RECIPE)";
	}
}
