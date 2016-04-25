# Asynchronous Communication using Messages 

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Component-based Web UI](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/UI_JSF.md)


## Synchronous versus Asynchronous communication

Until now, we defined a _synchronous_ system. Each time a _caller_ calls a _callee_, its execution flow is blocked until it receives the needed answer to continue. This behavior is _classical_, but can be problematic when the _callee_ is a slow process that is not necessary to pursue the _caller_'s business logic.

For example, let's consider the processing of a given order by the kitchen. The TCF bakeries are equipped with physical printer that print orders received from the cashier (or by Cookie on Demand). This process is slow with respect to the others operations involved in cookies ordering. And concretely, the order is already payed, so a customer should not wait for the ticket to be printed ...

<p align="center">
	<img src="https://raw.githubusercontent.com/polytechnice-si/4A_ISA_TheCookieFactory/develop/docs/asynch.png"/>
</p>

In the J2E ecosystem, calling beans in an asynchronous way is implemented on top of the _Java Message Service_ (JMS), through _Message-driven Beans_ (MDB). These beans will be started by the container, and react to the reception of messages on dedicated _message queues_.

## A bean to receive text messages

### Creating a simple message-driven bean

We consider here the `KitchenPrinterAck` bean, which basically display the identifiers associated to printed orders. An MDB is annotated as `@MessageBean`, and must implement the `MessageListener` interface. It defines a method named `onMessage`, that accept a `Message` as single argument.

```java
@MessageDriven
public class KitchenPrinterAck implements MessageListener {

	// ...

	public void onMessage(Message message) {
		try {
			String data = ((TextMessage) message).getText();
			System.out.println("\n\n****\n** ACK: " + data + "\n****\n");
		} catch (JMSException e) {
			throw new RuntimeException("Cannot read the received message!");
		}
	}
}
```

As the received `Message` is known to be a text-based one, we have to cast its content into a `TextMessage` before being able to access to its content.

### Invoking a message-driven bean

Each MDB is associated to a message queue, named after the bean class. Invoking an MDB is equivalent to sending a message to this queue. In a contained environment, the queue is obtained thanks to dependency injection, as it is maintained by the container. There is no need to activate anything in the container, maintaining (_e.g._, starting the queues, stopping it, reading messages, passivating elements) the queues is fully automated.

```java
@Resource private ConnectionFactory connectionFactory;
@Resource(name = "KitchenPrinterAck") private Queue acknowledgmentQueue;

private void acknowledge(int orderId) throws JMSException {
	Connection connection = null; Session session = null;
	try {
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(acknowledgmentQueue);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		producer.send(session.createTextMessage(orderId + ";PRINTED"));
	} finally {
		if (session != null) session.close();
		if (connection != null) connection.close();
	}
}
```

Obviously, as it is plain JMS under the hood, one can connect to the queue manually from the outside of the container.

## A bean to print orders (as objects)


As the printer is dedicated to TCF order, we'd like to send `Order` instances instead of plain text to the printers. According to the JMS specification, a `Message` can be a `TextMessage`, or an `ObjectMessage` (among others). In order to be contained in such a message, an object must be `Serializable`.

We decide to clean the business logic (_i.e._, printing an order) from the technical reception and object casting (_i.e._, the `onMessage` method body).

```java
@MessageDriven
public class KitchenPrinter implements MessageListener {

	// ...

	// technical concern
	public void onMessage(Message message) {
		try {
			Order data = (Order) ((ObjectMessage) message).getObject();
			handle(data);
		} catch (JMSException e) {
			throw new RuntimeException("Cannot print something that is not an Order!");
		}
	}
	
	// business logic
	private void handle(Order data) throws IllegalStateException {
		data = entityManager.merge(data);
		try {
			System.out.println("KitchenPrinter:\n  Printing order #"+data.getId());
			Thread.sleep(4000); // it takes time ... 4 seconds actually
			System.out.println("\n    " + data);
			System.out.println("\n  done ["+data.getId()+"]");
			acknowledge(data.getId());
		} catch (InterruptedException e) { 
			throw new IllegalStateException(e.toString()); 
		} catch (JMSException e) { 
			throw new IllegalStateException(e.toString()); 
		}
	}
	
}
```

  * Next: [Conclusions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Conclusions.md)
