package api;

import stubs.cart.CartWebService;
import stubs.cart.CartWebServiceImplService;
import stubs.customerCare.CustomerCareService;
import stubs.customerCare.CustomerCareServiceImplService;

import javax.xml.ws.BindingProvider;
import java.net.URL;

public class TCFPublicAPI {

	public CartWebService carts;
	public CustomerCareService ccs;

	public TCFPublicAPI(String host, String port) {
		initCart(host, port);
		initCCS(host, port);
	}

	private void initCart(String host, String port) {
		URL wsdlLocation = TCFPublicAPI.class.getResource("/CartWS.wsdl");
		CartWebServiceImplService factory = new CartWebServiceImplService(wsdlLocation);
		this.carts = factory.getCartWebServiceImplPort();
		String address = "http://" + host + ":" + port + "/tcf-backend/webservices/CartWS";
		((BindingProvider) carts).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
	}

	private void initCCS(String host, String port) {
		URL wsdlLocation = TCFPublicAPI.class.getResource("/CustomerCareWS.wsdl");
		CustomerCareServiceImplService factory = new CustomerCareServiceImplService(wsdlLocation);
		this.ccs = factory.getCustomerCareServiceImplPort();
		String address = "http://" + host + ":" + port + "/tcf-backend/webservices/CustomerCareWS";
		((BindingProvider) ccs).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
	}

}
