package fr.unice.polytech.isa.tcf.utils;

import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;

import javax.ejb.Singleton;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class Database {

	private int cartCounter = 0;
	public void incrementCarts() { cartCounter++; }
	public int howManyCarts() { return cartCounter; }

	public Database() {
		flush();
	}

	public void flush() { cartCounter = 0; }

}
