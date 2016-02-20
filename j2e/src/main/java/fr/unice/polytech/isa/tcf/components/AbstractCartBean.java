package fr.unice.polytech.isa.tcf.components;


import fr.unice.polytech.isa.tcf.Cart;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;

import javax.ejb.EJB;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractCartBean implements Cart {

	@Override
	public final boolean remove(Customer c, Item item) {
		return add(c, new Item(item.getCookie(), -item.getQuantity()));
	}

	/**
	 * Protected method to update the cart of a given customer, shared by both statefull and stateless beans
	 */
	protected Set<Item> updateCart(Customer c, Item item) {
		Set<Item> items = contents(c);
		Optional<Item> existing = items.stream().filter(e -> e.getCookie().equals(item.getCookie())).findFirst();
		if(existing.isPresent()) {
			items.remove(existing.get());
			Item toAdd = new Item(item.getCookie(), item.getQuantity() + existing.get().getQuantity());
			if(toAdd.getQuantity() > 0) { items.add(toAdd); }
		} else {
			items.add(item);
		}
		return items;
	}
}
