# Components as Web Services 

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)


  * Prev.: [Business components with EJB Sessions](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/BusinessComponents.md)
  
The Cookie Factory system must be exposed to remote clients, so that a customer can order some cookies through the service. Two options: (i) exposing the EJBs as remote ones, or (ii) expose these beans through a Web Service. The first option implies for the clients to be J2E-compliant, when the second option will allows clients to be developed in any language.

The other advantage of exposing the internal system thanks to a set of satellite web services is the decoupling ensured by this approach. The operations exposed by the web service are not the exact reflect of the associated bean, a web service can combine multiple beans to add value to the system. The Web Service layer is the _public API_ of our architecture. 

## Web service as interoperable layer

__Warning__: Implementing web services implies to respect a set of constraints:

  * Web services are stateless, according to the WS standard. As a consequence, any beans exposed to the outside world through a Web Service must be stateless. 
  * Business objects exposed through the public API must be serializable, and must define an empty constructor and get/set methods to be properly populated by the WE framework
  * The TomEE container must 

Here, we decide to expose two operations in the public API:  adding a cookie to a cart and getting cart contents. We exposed the `Item` business object in these methods as it exactly models what we are using in these methods. But the `Customer` business class is purely internal, it does not make any sense to expose to the outside world our complete representation of a Customer. Thus, we use a plain String to identify the customer in the public API.

A Web service contract is defined by an annotated interface. The annotations are straightforward: 

  * A `WebMethod` annotation tags the methods to expose as service operations;
  * A `WebParam` annotation tags the parameters to change their name, or handle xml namespace manually;
  * A `WebResult` annotation tags the returned value, like `@WebParam`.

```java
@WebService
public interface CartWebService {

	@WebMethod
	void addItemToCustomerCart(@WebParam(name = "customer_name") String customerName,
							   @WebParam(name = "item") Item it);

	@WebMethod
	@WebResult(name = "cart_contents")
	Set<Item> getCustomerCartContents(@WebParam(name = "customer_name") String customerName);

}
``` 

The implementation of these operations is simply done by a concrete class that implements the interface and delegates business work to the bean. This concrete class is actually a stateless bean that consumes another bean.

```java
@WebService(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/tcf")
@Stateless(name = "CartWS")
public class CartWebServiceImpl implements CartWebService {

	@EJB(name="stateless-cart") Cart cart;

	@Override
	public void addItemToCustomerCart(String customerName, Item it) {
		cart.add(new Customer(customerName), it);
	}

	@Override
	public Set<Item> getCustomerCartContents(String customerName) {
		return cart.contents(new Customer(customerName));
	}
}
```

The application container will support the exposition of this class according the the Web Service standard. To start the container in server mode, use the `mvn tomee:run` 

## Invoking a web service from a Java client

We create a remote client for the service, in a new maven project (`client`) directory.

The contract of the service, exposed using the _Web Service Description Language_ (WSDL), is automatically exposed by TomEE: [http://localhost:8080/tcf-backend/webservices/CartWS?wsdl](http://localhost:8080/tcf-backend/webservices/CartWS?wsdl).

Copy paste the contents of this contract (use the _view source_ option in your browser) into a local file located inside the client project, for example in `src/main/resources/CartWS.wsdl`. Then, use the _web services capabilities_ of your IDE to generate the Java code that will support the interactions with the service. For IntelliJ Ultimate users (the community edition does not contains the web service stack), _right-click_ on the WSDL contract, then select _Web Services_ and _Generate Java from WSDL_. The Java files generated from the WSDL contract will be stored inside a given package (_e.g._, `stubs.cart`). 

__Warning__: Obviously, each time the service contract will change, you'll have to rerun the stub generation process on the latest WSDL contract.

To instantiate the stub that will support the communication between the client and the service, we simply ask the generated stub code to do so (in the class `CartWSDemo`):

```java
private static CartWebService initialize() {
	System.out.println("#### Instantiating the WS Proxy");
	CartWebServiceImplService factory = new CartWebServiceImplService();
	CartWebService ws = factory.getCartWebServiceImplPort();
	return ws;
}
```	

From the client perspective, the code to be used to consume the web service is pretty simple: it is a Java version of the exposed contract. One can remark that some information is lost during the _Java to WSDL to Java_ contract generation then stub generation process. For example, the cart is modeled as a _Set_ on the server side, and was translated into a _List_ on the client side.

```java
public static void main() {
	CartWebService ws = initialize();

	List<Item> cart = ws.getCustomerCartContents("john");
	System.out.println("Cart is empty: " + cart.isEmpty());

	Item i = new Item();
	i.setCookie(Cookies.CHOCOLALALA); i.setQuantity(3);
	ws.addItemToCustomerCart("john", i);
	i.setCookie(Cookies.DARK_TEMPTATION); i.setQuantity(2);
	ws.addItemToCustomerCart("john", i);
	i.setCookie(Cookies.CHOCOLALALA); i.setQuantity(4);
	ws.addItemToCustomerCart("john", i);

	cart = ws.getCustomerCartContents("john");
	System.out.println("John's cart: " +cart);
}

```

## Removing runtime location dependencies

The previously described code works well, but rely on a very string assumption: the service will always be located at the very same location (on localhost). Moreover, the client will load at runtime the WSDL contract, so if one moves the contract elsewhere, the client code does not work anymore. And it is anyway not reasonable to package a distributed application that will only run on localhost. We need to _clean_ our default implementation to be more _aware_ of the server location.

First point, look at the WSDL contract. Even if stored locally as a resource, it refers to a remote file located at [http://localhost:8080/tcf-backend/webservices/CartWS?wsdl=CartWebService.wsdl](http://localhost:8080/tcf-backend/webservices/CartWS?wsdl=CartWebService.wsdl). We store this file as a local one, side by side with the initial contract, in a file named `CartWSType.wsdl` (as it basically defines the data types associated to our contract). Then, we edit the `CartWS.wsdl` file to point to this local file instead of the remote one (in the `wsdlLocation` attribute). We should now edit the initialization code to refer to our local file instead of the remote one.

```java
private static CartWebService initialize() {
	System.out.println("#### Loading the WSDL contract");
	URL wsdlLocation = CartWSDemo.class.getResource("/CartWS.wsdl");
	System.out.println("#### Instantiating the WS Proxy");
	CartWebServiceImplService factory = new CartWebServiceImplService(wsdlLocation);
	CartWebService ws = factory.getCartWebServiceImplPort();
	return ws;
}		
```

It only solves half of the problem. Now, the client code will load at runtime the local WSDL contract, but it will always try to consume a service deployed on localhost. We need to configure the _endpoint_ at runtime to point anywhere the service will be deployed. We apply the following assumption: by default it is deployed in development mode on _localhost_, and if command line arguments are provided, the first one is the hostname and the second one, if provided, is the port number.

```java
private static CartWebService initialize(String host, String port) {
	System.out.println("#### Loading the WSDL contract");
	URL wsdlLocation = CartWSDemo.class.getResource("/CartWS.wsdl");
	System.out.println("#### Instantiating the WS Proxy");
	CartWebServiceImplService factory = new CartWebServiceImplService(wsdlLocation);
	CartWebService ws = factory.getCartWebServiceImplPort();
	System.out.println("#### Updating the endpoint address dynamically");
	String address = "http://"+host+":"+port+"/tcf-backend/webservices/CartWS";
	((BindingProvider) ws).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
	return ws;
}
```

  * Next: [Consuming external Web Services (REST)](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Consuming_REST.md)
