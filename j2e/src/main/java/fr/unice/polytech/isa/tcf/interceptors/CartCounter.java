package fr.unice.polytech.isa.tcf.interceptors;


import fr.unice.polytech.isa.tcf.utils.Database;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartCounter implements Serializable {

	private static final Logger log = Logger.getLogger(CartCounter.class.getName());

	@EJB private transient Database memory;

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {
		Object result = ctx.proceed();  // do what you're supposed to do
		memory.incrementCarts();
		log.log(Level.INFO, "  #Cart processed: " + memory.howManyCarts());
		return result;
	}

}
