package fr.unice.polytech.isa.tcf.utils;

import fr.unice.polytech.isa.tcf.entities.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;


public class BankAPI {

	private String host;
	private String port;

	public BankAPI(String host, String port) {
		this.host = host;
		this.port = port;
	}

	public BankAPI() { this("localhost", "9090"); }

	public boolean performPayment(Customer customer, double value) {
		Integer id = pay(buildRequest(customer, value));
		return isValid(getPaymentStatus(id));
	}

	private JSONObject buildRequest(Customer customer, double value) {
		// Building payment request
		return new JSONObject().put("CreditCard", customer.getCreditCard()).put("Amount", value);
	}

	private WebClient client() {
		return WebClient.create("http://"+host+":"+port);
	}

	private Integer pay(JSONObject body) {
		// Sending a Payment request to the mailbox
		String str = client().path("/mailbox")
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.header("Content-Type", MediaType.APPLICATION_JSON)
				.post(body.toString(), String.class);
		return Integer.parseInt(str);
	}

	private JSONObject getPaymentStatus(Integer id) {
		// Retrieving the payment status
		String response = client().path("/payments/" + id)
				.get(String.class);
		JSONObject payment = new JSONObject(response);
		return payment;
	}

	private boolean isValid(JSONObject payment) {
		// Assessing the payment status
		return (payment.getInt("Status") == 0);
	}


}
