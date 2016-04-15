package fr.unice.polytech.isa.tcf.managed;

import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
public class OrderBean implements Serializable {

	private String customerName;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
