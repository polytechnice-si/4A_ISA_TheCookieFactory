package fr.unice.polytech.isa.tcf.business;

// business imports
import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.CartModifier;
import fr.unice.polytech.isa.tcf.CartProcessor;
import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.CustomerRegistration;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;

import java.util.*;
// component test framework import
import fr.unice.polytech.isa.tcf.exceptions.EmptyCartException;
import org.jboss.arquillian.junit.Arquillian;
// java annotations
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
// static import to lighten test writing
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CartTest extends AbstractTCFTest {

    @EJB(name = "cart-stateless") private CartModifier cart;
    @EJB(name = "cart-stateless") private CartProcessor processor;
    @EJB private CustomerRegistration registry;
    @EJB private CustomerFinder finder;

    @PersistenceContext private EntityManager entityManager;
    @Inject private UserTransaction utx;

    private static final String NAME = "John";
    private Customer john;


    @Before
    public void setUpContext() {
        john = new Customer(NAME, "1234567890");
        entityManager.persist(john);
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();
          john = entityManager.merge(john);
          entityManager.remove(john);
          john = null;
        utx.commit();
    }

    @Test
    public void emptyCartByDefault() {
        Set<Item> data = processor.contents(john);
        assertArrayEquals(new Item[] {}, data.toArray());
    }

    @Test
    public void addItems() {
        cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
        cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
        Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 2), new Item(Cookies.DARK_TEMPTATION, 3)  };
        assertEquals(new HashSet<>(Arrays.asList(oracle)), processor.contents(john));
    }

    @Test
    public void removeItems() {
        cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
        cart.remove(john, new Item(Cookies.CHOCOLALALA, 2));
        assertArrayEquals(new Item[] {}, processor.contents(john).toArray());
        cart.add(john, new Item(Cookies.CHOCOLALALA, 6));
        cart.remove(john, new Item(Cookies.CHOCOLALALA, 5));
        assertArrayEquals(new Item[] {new Item(Cookies.CHOCOLALALA, 1)}, processor.contents(john).toArray());
    }

    @Test
    public void modifyQuantities() {
        cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
        cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
        cart.add(john, new Item(Cookies.CHOCOLALALA, 3));
        Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 5), new Item(Cookies.DARK_TEMPTATION, 3)  };
        assertEquals(new HashSet<>(Arrays.asList(oracle)), processor.contents(john));
    }

    @Test
    public void getTheRightPrice() {
        cart.add(john, new Item(Cookies.CHOCOLALALA, 2));
        cart.add(john, new Item(Cookies.DARK_TEMPTATION, 3));
        cart.add(john, new Item(Cookies.CHOCOLALALA, 3));
        assertEquals(12.20, processor.price(john), 0.01);
    }


    @Test(expected = EmptyCartException.class)
    public void cannotProcessEmptyCart() throws Exception {
        assertArrayEquals(new Item[] {}, processor.contents(john).toArray());
        processor.validate(john);
    }

}
