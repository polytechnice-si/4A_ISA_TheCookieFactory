package fr.unice.polytech.isa.tcf.interceptors;

import fr.unice.polytech.isa.tcf.asynchronous.KitchenPrinter;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

public class Logger implements Serializable {


	private static java.util.logging.Logger log = java.util.logging.Logger.getLogger(Logger.class.getName());

	@AroundInvoke
	public Object methodLogger(InvocationContext ctx) throws Exception {
		String id = ctx.getTarget().getClass().getSimpleName() + "::" + ctx.getMethod().getName();
		log.info("*** Logger intercepts " + id);
		try {
			return ctx.proceed();
		} finally {
			log.info("*** End of interception for " + id);
		}
	}

}
