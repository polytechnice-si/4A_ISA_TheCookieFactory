package fr.unice.polytech.isa.tcf.asynchronous;

import fr.unice.polytech.isa.tcf.entities.Order;
import org.apache.openejb.util.LogCategory;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.*;
import javax.jms.IllegalStateException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@MessageDriven(activationConfig = {
				@ActivationConfigProperty( propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
				@ActivationConfigProperty( propertyName = "destination", propertyValue ="/queue/kitchen/printer") })
public class KitchenPrinter implements MessageListener {

	private static final org.apache.openejb.util.Logger log =
			org.apache.openejb.util.Logger.getInstance(LogCategory.ACTIVEMQ, KitchenPrinter.class);

	@Resource
	private MessageDrivenContext context;

	/**
	 * Message-based reception (automatically handled by the container)
	 * @param message a JMS message that contains an Order
	 * @throws RuntimeException
	 */
	@Override
	public void onMessage(Message message) {
		try {
			Order data = (Order) ((ObjectMessage) message).getObject();
			handle(data);
		} catch (JMSException e) {
			log.error("Java message service exception while handling " + message);
			log.error(e.getMessage(), e);
			context.setRollbackOnly();
		}
	}

	@PersistenceContext private EntityManager entityManager;
	/**
	 * Business logic to process an Order (basically sysout)
	 * @param data
	 * @throws IllegalStateException
	 */
	private void handle(Order data) throws IllegalStateException {
		Order d = entityManager.merge(data);
		try {
			log.info("KitchenPrinter:\n  Printing order #"+d.getId());
			Thread.sleep(4000); // it takes time ... 4 seconds actually
			log.info("\n    " + d);
			log.info("\n  done ["+d.getId()+"]");
			respond(d.getId());
		} catch (InterruptedException | JMSException e) {
			log.error(e.getMessage(), e);
			throw new IllegalStateException(e.toString());
		}
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
			if (session != null)
				session.close();
			if (connection != null)
				connection.close();
		}
	}
}

