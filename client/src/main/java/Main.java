import cli.commands.*;
import cli.framework.Shell;
import api.TCFPublicAPI;

/**
 * An Interactive shell that interacts with a Cookie on Demand instance
 * Use -Dexec.args="IP_ADDRESS PORT_NUMBER" to change host/port parameters
 */
public class Main extends Shell<TCFPublicAPI> {

	public Main(String host, String port) {

		this.system  = new TCFPublicAPI(host, port);
		this.invite  = "CoD";

		// Registering the command available for the user
		register(
				// Getting out of here
				Bye.class,
				// Handling customer
				RegisterCustomer.class,
				// Cookie catalogue
				ListCatalogueContents.class,
				// Cart management
				ShowCart.class, OrderCookie.class, RemoveCookie.class, ProcessCart.class,
				// Order tracking
				TrackOrder.class,
				// Play capability
				Play.class
		);
	}

	public static void main(String[] args) {
		String host = ( args.length == 0 ? "localhost" : args[0] );
		String port = ( args.length < 2  ? "8080"      : args[1] );
		System.out.println("\n\nStarting Cookie on Demand by The Cookie Factory");
		System.out.println("  - Remote server: " + host);
		System.out.println("  - Port number:   " + port);
		Main main = new Main(host, port);
		main.run();
		System.out.println("Exiting Cookie on Demand by The Cookie Factory\n\n");
	}

}
