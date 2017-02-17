package fr.unice.polytech.isa.tcf.entities;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class Item implements Serializable {

	@Enumerated(EnumType.STRING)
	@NotNull
	private Cookies cookie;

	@NotNull
	private int quantity;

	public Item() {
		// Necessary for JPA instantiation process
	}

	public Item(Cookies c, int q) {
	    cookie = c;
		quantity = q;
	}


	public Cookies getCookie() {
		return cookie;
	}
	public void setCookie(Cookies cookie) {
		this.cookie = cookie;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() { return quantity + "x" + cookie.toString(); }

	@Override
	public boolean equals(Object o) {
		if (this == o)
		    return true;
		if (!(o instanceof Item))
		    return false;
		Item item = (Item) o;
		if (getQuantity() != item.getQuantity())
		    return false;
		return getCookie() == item.getCookie();

	}

	@Override
	public int hashCode() {
		int result = getCookie().hashCode();
		result = 31 * result + getQuantity();
		return result;
	}
}
