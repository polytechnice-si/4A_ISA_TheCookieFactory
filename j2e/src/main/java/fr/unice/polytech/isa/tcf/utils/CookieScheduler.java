package fr.unice.polytech.isa.tcf.utils;

import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.entities.OrderStatus;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.WRITE)
public class CookieScheduler {

    private static final Logger log = Logger.getLogger(CookieScheduler.class.getName());

    @PersistenceContext private EntityManager entityManager;

    private Set<Order> orders = new HashSet<>();

    public CookieScheduler() {}

    public void add(Order o) {
        Order order = entityManager.merge(o);
        log.log(Level.INFO, "Scheduling order ["+order.getId()+"]");
        orders.add(order);
    }

    @Schedule(hour = "*", minute = "*", second = "*/5")
    public void process() {
        Set<Order> toKeep = new HashSet<>();
        for(Iterator<Order> it = orders.iterator(); it.hasNext(); ) {
            Order order = entityManager.merge(it.next());
            Optional<OrderStatus> next = OrderStatus.next(order.getStatus());
            if(next.isPresent()) {
                log.log(Level.INFO, "Moving order ["+order.getId()+"] to next step");
                order.setStatus(next.get());
                toKeep.add(order);
            } else {
                log.log(Level.INFO, "No more scheduled operation for Order ["+order.getId()+"]");
            }
        }
        this.orders = toKeep;
    }
}
