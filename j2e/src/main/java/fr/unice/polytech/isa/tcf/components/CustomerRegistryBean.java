package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.CustomerRegistration;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Stateless
public class CustomerRegistryBean implements CustomerRegistration, CustomerFinder {

	private static final Logger log = Logger.getLogger(Logger.class.getName());

	@PersistenceContext private EntityManager manager;

	/******************************************
	 ** Customer Registration implementation **
	 ******************************************/

	@Override
	public void register(String name, String creditCard) throws AlreadyExistingCustomerException {

		if(findByName(name).isPresent())
			throw new AlreadyExistingCustomerException(name);

		Customer c = new Customer();
		c.setName(name);
		c.setCreditCard(creditCard);
		c.setCart(new HashSet<>());

		manager.persist(c);

	}

	/************************************
	 ** Customer Finder implementation **
	 ************************************/

	@Override
	public Optional<Customer> findByName(String name) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
		Root<Customer> root =  criteria.from(Customer.class);
		criteria.select(root).where(builder.equal(root.get("name"), name));
		TypedQuery<Customer> query = manager.createQuery(criteria);
		try {
			return Optional.of(query.getSingleResult());
		} catch (NoResultException nre){
            log.log(Level.FINEST, "No result for ["+name+"]", nre);
			return Optional.empty();
		}
	}
}

