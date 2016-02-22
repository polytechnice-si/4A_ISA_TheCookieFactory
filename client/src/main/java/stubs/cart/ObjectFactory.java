
package stubs.cart;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the stubs.cart package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetCustomerCartContentsResponse_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "getCustomerCartContentsResponse");
    private final static QName _AddItemToCustomerCartResponse_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "addItemToCustomerCartResponse");
    private final static QName _UnknownCustomerException_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "UnknownCustomerException");
    private final static QName _GetCustomerCartContents_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "getCustomerCartContents");
    private final static QName _AddItemToCustomerCart_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "addItemToCustomerCart");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: stubs.cart
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddItemToCustomerCartResponse }
     * 
     */
    public AddItemToCustomerCartResponse createAddItemToCustomerCartResponse() {
        return new AddItemToCustomerCartResponse();
    }

    /**
     * Create an instance of {@link GetCustomerCartContents }
     * 
     */
    public GetCustomerCartContents createGetCustomerCartContents() {
        return new GetCustomerCartContents();
    }

    /**
     * Create an instance of {@link UnknownCustomerException }
     * 
     */
    public UnknownCustomerException createUnknownCustomerException() {
        return new UnknownCustomerException();
    }

    /**
     * Create an instance of {@link AddItemToCustomerCart }
     * 
     */
    public AddItemToCustomerCart createAddItemToCustomerCart() {
        return new AddItemToCustomerCart();
    }

    /**
     * Create an instance of {@link GetCustomerCartContentsResponse }
     * 
     */
    public GetCustomerCartContentsResponse createGetCustomerCartContentsResponse() {
        return new GetCustomerCartContentsResponse();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomerCartContentsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "getCustomerCartContentsResponse")
    public JAXBElement<GetCustomerCartContentsResponse> createGetCustomerCartContentsResponse(GetCustomerCartContentsResponse value) {
        return new JAXBElement<GetCustomerCartContentsResponse>(_GetCustomerCartContentsResponse_QNAME, GetCustomerCartContentsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddItemToCustomerCartResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "addItemToCustomerCartResponse")
    public JAXBElement<AddItemToCustomerCartResponse> createAddItemToCustomerCartResponse(AddItemToCustomerCartResponse value) {
        return new JAXBElement<AddItemToCustomerCartResponse>(_AddItemToCustomerCartResponse_QNAME, AddItemToCustomerCartResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnknownCustomerException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "UnknownCustomerException")
    public JAXBElement<UnknownCustomerException> createUnknownCustomerException(UnknownCustomerException value) {
        return new JAXBElement<UnknownCustomerException>(_UnknownCustomerException_QNAME, UnknownCustomerException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCustomerCartContents }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "getCustomerCartContents")
    public JAXBElement<GetCustomerCartContents> createGetCustomerCartContents(GetCustomerCartContents value) {
        return new JAXBElement<GetCustomerCartContents>(_GetCustomerCartContents_QNAME, GetCustomerCartContents.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddItemToCustomerCart }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "addItemToCustomerCart")
    public JAXBElement<AddItemToCustomerCart> createAddItemToCustomerCart(AddItemToCustomerCart value) {
        return new JAXBElement<AddItemToCustomerCart>(_AddItemToCustomerCart_QNAME, AddItemToCustomerCart.class, null, value);
    }

}
