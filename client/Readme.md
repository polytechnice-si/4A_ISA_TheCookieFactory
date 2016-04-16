# TCF Command-Line Interface (CLI)

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016


The Cookie On Demand system is shipped with a _Command-Line Interface_ (CLI) client that connects to the web services exposed by the business back-end.

To start it, simply run (assuming the code was previously compiled):

    azrael:client mosser$ mvn exec:java


## Client Architecture

The client is bundled as a maven project. The `src` directory contains all the source code. The `demos` directory contains examples of command scripts one can use to interact with the system.

From a source code point of view:
  
  * The `resources` directory contains the WSDL contracts uses to describe the SOAP-based web services exposed by The Cookie Factory;
  * The `stubs` package contains the Java code automatically generated from the WSDL contract previously mentioned;
  * The `api` package define a `TCFPublicAPI` class that instantiates the web services proxies, supporting a way to change the endpoint location (host and port);
  * The `cli` package defines the commands used to interact with the services from a shell-like interface:
    * `framework` defines what is a `Command` and what is a `Shell` in a domain-independent way;
    * `commands` implements the different commands specific to out TCF case study.
  * The `Main` class start a shell with the registered commands;
    * The `CartWSDemo` is an alternative mains class that runs a non-interactive demonstration.

## Available Commands

```
CoD > ?
  - bye: Exit Cookie on Demand
  - recipes: List all available recipes
  - order: Order some cookies for a given customer (order CUSTOMER QUANTITY RECIPE)
  - play: Play commands stored in a given file (play FILENAME)
  - process: Process a given cart into an order (process CUSTOMER_NAME)
  - register: Register a customer in the CoD backend (register CUSTOMER_NAME CREDIT_CARD_NUMBER)
  - remove: Remove some cookies for a given customer (remove CUSTOMER QUANTITY RECIPE)
  - cart: show the cart contents for a given customer (cart CUSTOMER_NAME)
  - track: track order status (track ORDER_ID)
```

## Extending the client

One can extend the given client by defining customized commands. Creating a new command is a two-step process: (i) implementing the class that defines the expected behavior and (ii) registering it inside the shell.

### Creating a new Command class

A custom command that interact with the `TCFPublicAPI` is basically a class that extends `Command<TCFPublicAPI>`. To create the command, one must override the following methods:

   * `identifier()`: returns the keyword to be used to invoke this command from the shell;
   * `describe()`: returns a description of the semantics of the command, and how to invoke it;
   * `execute()`: implements the business logic associated to this command. To interact with the system, a `Command` gains automatically access to `shell.system` that is an instance of `TCFPublicAPI`.

Optionally, if the command relies on arguments (_e.g._, customer's name, cookie to order), one can override the `load(List<String> args)` method that provides a way to access to the arguments provided inside the user input.

```java
public class MyCommand extends Command<TCFPublicAPI> {

	@Override
	public String identifier() { return "command"; }

	@Override
	public String describe() { return "Custom Command (command ARG_1)"; }
	
	@Override
	public void execute() throws Exception { 
		/** call to the TCF system goes here through `shell.system` **/
	}

	private String arg1;
	
	@Override
	public void load(List<String> args) { arg1 = args.get(0); }
}
```

### Registering the Command

In the class that starts the shell (in our example, the `Main` class), one simply `register` this command with the others:

```java

public Main(String host, String port) {
	this.system  = new TCFPublicAPI(host, port);
	this.invite  = "CoD";

	// Registering the command available for the user
	register(
		/** other commands go here **/
		MyCommand.class
	);
}
```	

## Perspectives

  - [ ] Implement an history pattern to remember the last command and replay it
  - [ ] tests, tests, tests.