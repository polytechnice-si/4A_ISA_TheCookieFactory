package fr.unice.polytech.isa.tcf.asynchronous;

import fr.unice.polytech.isa.tcf.entities.Order;

import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.jms.IllegalStateException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@MessageDriven
public class KitchenPrinter implements MessageListener {

	private static Logger log = Logger.getLogger(KitchenPrinter.class.getName());

	/**
	 * Message-based reception (automatically handled by the container)
	 * @param message a JMS message that contains an Order
	 * @throws RuntimeException
	 */
	public void onMessage(Message message) {
		try {
			Order data = (Order) ((ObjectMessage) message).getObject();
			handle(data);
		} catch (JMSException e) {
			throw new RuntimeException("Cannot print something that is not an Order!");
		}
	}

	@PersistenceContext private EntityManager entityManager;
	/**
	 * Business logic to process an Order (basically sysout)
	 * @param data
	 * @throws IllegalStateException
	 */
	private void handle(Order data) throws IllegalStateException {
		data = entityManager.merge(data);
		try {
			log.info("KitchenPrinter:\n  Printing order #"+data.getId());
			Thread.sleep(4000); // it takes time ... 4 seconds actually
			log.info("\n    " + data);
			log.info("\n  done ["+data.getId()+"]");
			respond(data.getId());
		} catch (InterruptedException e) { throw new IllegalStateException(e.toString()); }
		catch (JMSException e) { throw new IllegalStateException(e.toString()); }
	}

	/**
	 ** Resources necessary to support asynchronous responses
	 */

	@Resource private ConnectionFactory connectionFactory;
	@Resource(name = "KitchenPrinterAck") private Queue acknowledgmentQueue;

	/**
	 * Send the processed order ID to the response Queue (as text: "#{ID};PRINTED")"
	 * @param orderId
	 */
	private void respond(int orderId) throws JMSException {
		Connection connection = null;
		Session session = null;
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
}

