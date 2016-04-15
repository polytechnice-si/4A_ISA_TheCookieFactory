package fr.unice.polytech.isa.tcf.managed;

import fr.unice.polytech.isa.tcf.Tracker;
import fr.unice.polytech.isa.tcf.exceptions.UnknownOrderId;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean
public class TrackerBean {

	@EJB private Tracker tracker;

	@ManagedProperty("#{param.orderId}")  // Will be automatically injected from the GET parameter
	private String orderId;
	public String getOrderId() { return orderId; }
	public void setOrderId(String orderId) { this.orderId = orderId; }

	public String getStatus() {
		if(orderId == null) { return "No orderId given!"; }
		try {
			return tracker.status(orderId).name();
		} catch (UnknownOrderId uoid) {
			return "Unknown Order [" + orderId + "]";
		}
	}

}
