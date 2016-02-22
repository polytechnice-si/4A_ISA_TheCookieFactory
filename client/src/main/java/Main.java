import cli.commands.*;
import cli.framework.Shell;
import api.TCFPublicAPI;

/**
 * An Interactive shell that interacts with a Cookie on Demand instance
 * Use -Dexec.args="127.0.0.1 8090" to change host/port parameters
 */
public class Main extends Shell<TCFPublicAPI> {

	public Main(String host, String port) {

		this.system  = new TCFPublicAPI(host, port);
		this.invite  = "CoD";

		// Registering the command available for the user
		register(
				Bye.class,
				RegisterCustomer.class,
				ShowCart.class,
				OrderCookie.class
		);

	}

	public static void main(String[] args) {
		String host = ( args.length == 0 ? "localhost" : args[0] );
		String port = ( args.length < 2  ? "8080"      : args[1] );
		System.out.println("\nStarting Cookie on Demand by The Cookie Factory ("+host+":"+port+")");
		Main main = new Main(host, port);
		main.run();
		System.out.println("Exiting Cookie on Demand by The Cookie Factory\n");
	}

}
