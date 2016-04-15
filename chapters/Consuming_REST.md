# Consuming REST Services

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Exposing components as Web Services (SOAP)](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Exposing_SOAP.md)

## External Partner

The bank used by TCF to support payments in CoD implements its payment service as a REST one. This service exposes the following resources:

  * `request`, a singleton resource that accept `POST` requests used to post `PaymentRequest`s to the Bank;
  * `payments`, a list of all payment identifiers available in the system;
  * `payments/{id}`, the description of a given payment;

__Remark__: Contrarily to the previous service that was _SOAP-based_, this one is _resource-oriented_. The difference between the two paradigm is essential: the previous one exposes __procedures__ (_aka Remote Procedure Call_, RPC), where this one exposes __resources__ (_i.e._, nouns instead of verbs). 

### Implementing a REST service using Mono (.Net)

The Web Service is implemented in the `dotNet/src` directory. The compilation script generates a self-hosted server (in a file named `server.exe`) that starts a web server and binds the requested URIs to the defined operations.

The description of the service interface is straightforward:

```csharp
[ServiceContract]
public interface IPaymentService
{
  [OperationContract]
  [WebInvoke( Method = "POST", UriTemplate = "mailbox", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
  int ReceiveRequest(PaymentRequest request);

  [OperationContract]
  [WebInvoke( Method = "GET", UriTemplate = "payments/{identifier}", ResponseFormat = WebMessageFormat.Json)]
  Payment FindPaymentById(int identifier);

  [OperationContract]
  [WebInvoke( Method = "GET", UriTemplate = "payments", ResponseFormat = WebMessageFormat.Json)]
  List<int> GetAllPaymentIds();
}
```

The implementation is also trivial. We use a map instantiated as an instance variable to implement persistence. It makes the service stateful, which is an anti-pattern and only make sense as we are creating a Proof of Concept. 

To start the service hosting server, simply run `mono server.exe`. Command line parameters can be used to configure the port number for example.

### Invoking a REST service 

As the system relies on simple HTTP requests, one can invoke the PaymentService using any HTTP client, just by specifying the right headers and body for a given request. For example using _cURL_, here are the different commands one can use:

```
azrael:~ mosser$ REQUEST='{ "CreditCard": "1234-896983", "Amount": 12.09 }'
azrael:~ mosser$ BASE_URL="http://localhost:9090"
azrael:~ mosser$ HEADERS='Content-Type: application/json'
azrael:~ mosser$ curl -i -w "\n" -H "$HEADERS"  -X POST -d "$REQUEST" $BASE_URL/mailbox
HTTP/1.1 200 
Content-Type: application/json; charset=utf-8
Server: Mono-HTTPAPI/1.0
Date: Thu, 25 Feb 2016 09:24:41 GMT
Content-Length: 1
Keep-Alive: timeout=15,max=100

1
azrael:~ mosser$ curl -i -w "\n" -H "$HEADERS"  -X GET $BASE_URL/payments
HTTP/1.1 200 
Content-Type: application/json; charset=utf-8
Server: Mono-HTTPAPI/1.0
Date: Thu, 25 Feb 2016 13:24:15 GMT
Content-Length: 7
Keep-Alive: timeout=15,max=100

[1,2,3]
azrael:~ mosser$ curl -i -w "\n" -H "$HEADERS"  -X GET $BASE_URL/payments/1
HTTP/1.1 200 
Content-Type: application/json; charset=utf-8
Server: Mono-HTTPAPI/1.0
Date: Thu, 25 Feb 2016 13:24:29 GMT
Content-Length: 100
Keep-Alive: timeout=15,max=100

{"Amount":12.09,"CreditCard":"1234-896983","Date":"25\/02\/2016 10:24:41","Identifier":0,"Status":0}
azrael:~ mosser$
```

### Invoking a REST service from Java

The J2E system consumes the Bank service. As a consequence, our EJBs will act as _clients_ of this service. We rely on the _Apache CXF_ library to consume REST web services. We implement the methods that support the communication with the service in a utility class named `BankAPI`:

```java
private Integer pay(JSONObject body) {
	String str = client().path("/mailbox")
			.accept(MediaType.APPLICATION_JSON_TYPE)
			.header("Content-Type", MediaType.APPLICATION_JSON)
			.post(body.toString(), String.class);
	return Integer.parseInt(str);
}

private JSONObject getPaymentStatus(Integer id) {
	String response = client().path("/payments/" + id).get(String.class);
	JSONObject payment = new JSONObject(response);
	return payment;
}

private boolean isValid(JSONObject payment) {
	return (payment.getInt("Status") == 0);
}
```

### Configuring the Bank endpoint

The `CashierBean` class uses an instance of the `BankAPI` class to interact with the remote bank service. The endpoint cannot be hardcoded in its source code. As a consequence, we define a `bank.properties` file in the `resources` directory, which will defined the hostname and port number to be used when interacting with the Bank. In the `CashierBean`, we use a `@PostConstruct` annotation to load these properties from the resource file after the bean initialization:

```java
@PostConstruct
private void initializeRestPartnership() throws IOException {
	Properties prop = new Properties();
	prop.load(this.getClass().getResourceAsStream("/bank.properties"));
	bank = new BankAPI(	prop.getProperty("bankHostName"),
							prop.getProperty("bankPortNumber"));
}
```


  * Next: [Unit testing _versus_ Integration testing](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/IntegrationTesting.md)
