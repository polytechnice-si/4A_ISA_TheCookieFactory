package api;

import stubs.cart.CartWebService;
import stubs.cart.CartWebServiceImplService;

import javax.xml.ws.BindingProvider;
import java.net.URL;

public class TCFPublicAPI {

	public CartWebService carts;

	public TCFPublicAPI(String host, String port) {
		initCart(host, port);
	}

	private void initCart(String host, String port) {
		URL wsdlLocation = TCFPublicAPI.class.getResource("/CartWS.wsdl");
		CartWebServiceImplService factory = new CartWebServiceImplService(wsdlLocation);
		this.carts = factory.getCartWebServiceImplPort();
		String address = "http://" + host + ":" + port + "/tcf-backend/webservices/CartWS";
		((BindingProvider) carts).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
	}

}
