package fr.unice.polytech.isa.tcf.interceptors;


import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import org.apache.openejb.util.LogCategory;

public class Logger implements Serializable {

    private static final LogCategory INTERCEPTORS = LogCategory.OPENEJB.createChild("interceptors");

	private static final org.apache.openejb.util.Logger log =
			org.apache.openejb.util.Logger.getInstance(INTERCEPTORS, Logger.class);

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
