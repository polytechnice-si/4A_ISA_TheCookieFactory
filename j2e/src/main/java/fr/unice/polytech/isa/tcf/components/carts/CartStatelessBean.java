package fr.unice.polytech.isa.tcf.components.carts;

import fr.unice.polytech.isa.tcf.components.CartBean;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.utils.Database;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;


@Stateless(name = "cart-stateless")
public class CartStatelessBean extends CartBean {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public boolean add(Customer c, Item item) {
		c = entityManager.merge(c);
		c.setCart(updateCart(c, item));
		return true;
	}

	@Override
	public Set<Item> contents(Customer c) {
		c = entityManager.merge(c);
		return c.getCart();
	}

}
