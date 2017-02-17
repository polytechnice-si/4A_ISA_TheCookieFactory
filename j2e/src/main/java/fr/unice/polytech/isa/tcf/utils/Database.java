package fr.unice.polytech.isa.tcf.utils;

import javax.ejb.Singleton;

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
