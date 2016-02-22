package fr.unice.polytech.isa.tcf;


import fr.unice.polytech.isa.tcf.components.carts.CartStateFullBean;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;
import fr.unice.polytech.isa.tcf.utils.Database;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public abstract class AbstractTCFTest {

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addPackage(Database.class.getPackage())
				.addPackage(Customer.class.getPackage())
				.addPackage(CartModifier.class.getPackage())
				.addPackage(AlreadyExistingCustomerException.class.getPackage())
				.addPackage(CartStateFullBean.class.getPackage());
	}

}
