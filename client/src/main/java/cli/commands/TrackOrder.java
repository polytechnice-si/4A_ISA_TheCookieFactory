package cli.commands;

import api.TCFPublicAPI;
import cli.framework.Command;
import stubs.cart.Item;
import stubs.customerCare.OrderStatus;

import java.util.List;

public class TrackOrder extends Command<TCFPublicAPI> {

	private String orderId;

	@Override
	public String identifier() { return "track"; }

	@Override
	public void execute() throws Exception {
		OrderStatus status = shell.system.ccs.track(orderId);
		System.out.println("  Status: " + status);
	}

	@Override
	public void load(List<String> args) { orderId = args.get(0); }

	@Override
	public String describe() {
		return "track order status (track ORDER_ID)";
	}

}
