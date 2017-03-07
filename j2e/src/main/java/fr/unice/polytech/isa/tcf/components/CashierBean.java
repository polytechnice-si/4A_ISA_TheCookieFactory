package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.ControlledPayment;
import fr.unice.polytech.isa.tcf.OrderProcessing;
import fr.unice.polytech.isa.tcf.Payment;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.exceptions.ExternalPartnerException;
import fr.unice.polytech.isa.tcf.exceptions.PaymentException;
import fr.unice.polytech.isa.tcf.exceptions.UncheckedException;
import fr.unice.polytech.isa.tcf.utils.BankAPI;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CashierBean implements Payment, ControlledPayment {

    private static final Logger log = Logger.getLogger(Logger.class.getName());

	@EJB private OrderProcessing kitchen;
	@PersistenceContext private EntityManager entityManager;

	private BankAPI bank;

	@Override
	public void useBankReference(BankAPI bank) {
		this.bank = bank;
	}

	@Override
	public String payOrder(Customer c, Set<Item> items) throws PaymentException {

		Customer customer = entityManager.merge(c);

		Order order = new Order(customer, items);
		double price = order.getPrice();

		boolean status = false;
		try {
			status = bank.performPayment(customer, price);
		} catch (ExternalPartnerException e) {
            log.log(Level.INFO, "Error while exchanging with external partner", e);
			throw new PaymentException(customer.getName(), price, e);
		}

		if (!status) {
			throw new PaymentException(customer.getName(), price);
		}

		customer.add(order);
		entityManager.persist(order);
		kitchen.process(order);
		return Integer.toString(order.getId());
	}

	@PostConstruct
	private void initializeRestPartnership() {
	    try {
            Properties prop = new Properties();
            prop.load(this.getClass().getResourceAsStream("/bank.properties"));
            bank = new BankAPI(prop.getProperty("bankHostName"),
                    prop.getProperty("bankPortNumber"));
        } catch(IOException e) {
            log.log(Level.INFO, "Cannot read bank.properties file", e);
            throw new UncheckedException(e);
        }
	}

}
