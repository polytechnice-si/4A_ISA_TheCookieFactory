package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.OrderProcessing;
import fr.unice.polytech.isa.tcf.Tracker;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;
import fr.unice.polytech.isa.tcf.exceptions.UnknownOrderId;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.openejb.util.LogCategory;
import org.apache.openejb.util.Logger;

@Stateless
public class KitchenBean implements OrderProcessing, Tracker {

	@PersistenceContext private EntityManager entityManager;

	@Override
	public void process(Order order) {
		order = entityManager.merge(order);
		if (order.getCustomer().getName().contains("p")) {
			order.setStatus(OrderStatus.IN_PROGRESS);
		} else if (order.getCustomer().getName().contains("r")) {
			order.setStatus(OrderStatus.READY);
		}
		sendToPrinter(order);
	}

	@Override
	public OrderStatus status(String orderId) throws UnknownOrderId {
		Order order = entityManager.find(Order.class, Integer.parseInt(orderId));
		if (order == null)
			throw new UnknownOrderId(orderId);
		return  order.getStatus();
	}

	/**
	 * Private method to send an asynchronous message to the KitchenPrinter
	 */

	@Resource private ConnectionFactory connectionFactory;
	@Resource(name = "KitchenPrinter") private Queue printerQueue;

	private static final LogCategory JMS = LogCategory.ACTIVEMQ.createChild("jms");

	private void sendToPrinter(Order o) {
		try {
			Connection connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer printer = session.createProducer(printerQueue);
			printer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			printer.send(session.createObjectMessage(o));
			printer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			Logger logger = Logger.getInstance(JMS, this.getClass());
			logger.error(e.getMessage());
		}
	}

}
