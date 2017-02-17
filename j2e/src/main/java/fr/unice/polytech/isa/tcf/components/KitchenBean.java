package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.OrderProcessing;
import fr.unice.polytech.isa.tcf.Tracker;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;
import fr.unice.polytech.isa.tcf.exceptions.UnknownOrderId;
import fr.unice.polytech.isa.tcf.utils.CookieScheduler;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.logging.Level;
import java.util.logging.Logger;
@Stateless
public class KitchenBean implements OrderProcessing, Tracker {

    private static final Logger log = Logger.getLogger(KitchenBean.class.getName());

	@PersistenceContext private EntityManager entityManager;
    @Resource private ConnectionFactory connectionFactory;
    @Resource(name = "KitchenPrinter") private Queue printerQueue;
    @EJB CookieScheduler scheduler;

	@Override
	public void process(Order o) {
        scheduler.add(o);
		sendToPrinter(o);
	}

	@Override
	public OrderStatus status(String orderId) throws UnknownOrderId {
		Order order = entityManager.find(Order.class, Integer.parseInt(orderId));
		if (order == null)
			throw new UnknownOrderId(orderId);
		return  order.getStatus();
	}


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
			log.log(Level.WARNING, "Unable to send Order [" + o.getId() + "]to printer", e);
		}
	}
}
