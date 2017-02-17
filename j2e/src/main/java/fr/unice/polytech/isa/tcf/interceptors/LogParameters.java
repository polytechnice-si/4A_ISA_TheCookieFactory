package fr.unice.polytech.isa.tcf.interceptors;


import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;


public class LogParameters {

    private static final Logger log = Logger.getLogger(LogParameters.class.getName());

	@AroundInvoke
	public Object methodLogger(InvocationContext ctx) throws Exception {
		String id = ctx.getTarget().getClass().getSimpleName() + "::" + ctx.getMethod().getName();
		log.info("*** LogParameters intercepts " + id);
		try {
			return ctx.proceed();
		} finally {
			log.info("*** End of interception for " + id);
		}
	}

}
