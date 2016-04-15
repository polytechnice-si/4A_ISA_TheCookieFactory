# Interceptors 

  * Author: Sébastien Mosser [mosser@i3s.unice.fr](mosser@i3s.unice.fr)
  * Reviewer: Anne-Marie Déry [pinna@polytech.unice.fr](pinna@polytech.unice.fr)
  * Version: 02.2016
  * [Back to The Cookie Factory Home Page](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/Readme.md)

  * Prev.: [Complete architecture overview](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/VolatileOverview.md)
  
Interceptors are used inside the application server to process the messages exchanged between the different components. But the EJB framework also allows one to develop business-oriented interceptors as a support for cross-cutting features that does not fit easily inside components (e.g., code duplication).

## Counting the number of processed carts

For statistical purpose, TCF wants to count the number if processed carts. Each time the `validate` operation is invoked on a `Cart`, a counter will be incremented is the cart processing process went well.

One can implement this feature directly inside the `validate` method. However, it will pollute the business-orientation if this implementation with a very technical concern. Thus, we decide to implement this feature as an interception. The interceptor will be located _around_ the _invocation_ of `validate`, and will implement the following algorithm: (i) proceed to your normal behavior, (ii) if it does not throw any exception, increment the statistics counter in the database.

```java
public class CartCounter implements Serializable {

	@EJB private Database memory;

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {
		Object result = ctx.proceed();  // do what you're supposed to do
		memory.incrementCarts();
		System.out.println("  #Cart processed: " + memory.howManyCarts());
		return result;
	}

}
```

To put the interceptor around our business code, we only need to annotate the expected method:

```java
@Override
@Interceptors({CartCounter.class})
public String validate(Customer c) throws PaymentException {
	return cashier.payOrder(c, contents(c));
}
```

One can put multiple interceptors on the very same method, they will be executed in sequence from left to right. If the bean is stateful, the interceptor must be passivation-compliant, _i.e._, implement the Serializable interface.

## Fault detection

Another interesting usage of interceptors is to implement fault detection. For example, adding or removing to a given cart an `Item` with a negative or null amount of cookies does not make any sense. We can then intercept the invocation of the `CartWebService` operations and check the `item` parameter:

```java
public class ItemVerifier {
	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {
		Item it = (Item) ctx.getParameters()[1];
		if (it.getQuantity() <= 0) {
			throw new RuntimeException("Inconsistent quantity!");
		}
		return ctx.proceed();
	}
}
```

In the `CartWebService` interface description, we annotate the `add` and `remove` operations to declare the interception.

```java
@WebMethod
@Interceptors({ItemVerifier.class})
void addItemToCustomerCart(@WebParam(name = "customer_name") String customerName,
						   @WebParam(name = "item") Item it)
		throws UnknownCustomerException;

@WebMethod
@Interceptors({ItemVerifier.class})
void removeItemToCustomerCart(@WebParam(name = "customer_name") String customerName,
							  @WebParam(name = "item") Item it)
		throws UnknownCustomerException;
```

__Remark__: The invocation context can be modified by an interceptor, _e.g._, parameters can be modified.

## Using non-invasive interceptions

We consider here a _tracer_ that will log each operation invoked inside the system. This logger is very simple to implement.

```java
public class Logger implements Serializable {

	@AroundInvoke
	public Object methodLogger(InvocationContext ctx) throws Exception {
		String id = ctx.getTarget().getClass().getSimpleName() + "::" + ctx.getMethod().getName();
		System.out.println("*** Logger intercepts " + id);
		try {
			return ctx.proceed();
		} finally {
			System.out.println("*** End of interception for " + id);
		}
	}
}
```

It does not make any sense to manually annotate all the operations designed inside our system. In a file named `ejb-jar.xml` (in the `resources` directory), we simply define a regular expression associated to this very interceptor. The container will map the interceptor to any bean that match the given regular expression. In our case, we want to catch _all_ the method, and the regular expression is quite simple: `*`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ejb-jar PUBLIC '-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans2.0//EN' 'http://java.sun.com/dtd/ejb-jar_2_0.dtd'>
<ejb-jar>
    <assembly-descriptor>
        <interceptor-binding>
            <ejb-name>*</ejb-name>
            <interceptor-class>fr.unice.polytech.isa.tcf.interceptors.Logger</interceptor-class>
        </interceptor-binding>
    </assembly-descriptor>
</ejb-jar>
```

  * Next: [Making things persistent](https://github.com/polytechnice-si/4A_ISA_TheCookieFactory/blob/develop/chapters/Persistence.md) 