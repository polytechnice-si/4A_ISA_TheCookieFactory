package fr.unice.polytech.isa.tcf.asynchronous;


import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Logger;

@MessageDriven
public class KitchenPrinterAck implements MessageListener {

	private static Logger log = Logger.getLogger(KitchenPrinterAck.class.getName());

	public void onMessage(Message message) {
		try {
			String data = ((TextMessage) message).getText();
			log.info("\n\n****\n** ACK: " + data + "\n****\n");
		} catch (JMSException e) {
			throw new RuntimeException("Cannot read the received message!");
		}
	}

}
