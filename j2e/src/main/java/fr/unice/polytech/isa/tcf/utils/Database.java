package fr.unice.polytech.isa.tcf.utils;

import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;

import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class Database {

	private Map<Customer, Set<Item>> carts = new HashMap<>();
	public Map<Customer, Set<Item>> getCarts() { return carts; }


	public void flush() {
		carts = new HashMap<>();
	}

}
