package fr.unice.polytech.isa.tcf.interceptors;

import fr.unice.polytech.isa.tcf.entities.Item;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

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
