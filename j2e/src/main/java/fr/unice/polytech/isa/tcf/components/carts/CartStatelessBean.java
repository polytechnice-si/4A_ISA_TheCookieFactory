package fr.unice.polytech.isa.tcf.components.carts;

import fr.unice.polytech.isa.tcf.CartModifier;
import fr.unice.polytech.isa.tcf.CartProcessor;
import fr.unice.polytech.isa.tcf.components.CartBean;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Stateless(name = "cart-stateless")
public class CartStatelessBean extends CartBean {

	@PersistenceContext private EntityManager entityManager;

	@Override
	public boolean add(Customer customer, Item item) {
		Customer c = entityManager.merge(customer);
		c.setCart(updateCart(c, item));
		return true;
	}

	@Override
	public Set<Item> contents(Customer customer) {
		Customer c = entityManager.merge(customer);
		return c.getCart();
	}

}
