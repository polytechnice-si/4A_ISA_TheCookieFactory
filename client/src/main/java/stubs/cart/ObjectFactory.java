
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

    private final static QName _RemoveItemToCustomerCartResponse_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "removeItemToCustomerCartResponse");
    private final static QName _GetCustomerCartContentsResponse_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "getCustomerCartContentsResponse");
    private final static QName _Validate_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "validate");
    private final static QName _AddItemToCustomerCartResponse_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "addItemToCustomerCartResponse");
    private final static QName _EmptyCartException_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "EmptyCartException");
    private final static QName _UnknownCustomerException_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "UnknownCustomerException");
    private final static QName _PaymentException_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "PaymentException");
    private final static QName _GetCustomerCartContents_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "getCustomerCartContents");
    private final static QName _RemoveItemToCustomerCart_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "removeItemToCustomerCart");
    private final static QName _AddItemToCustomerCart_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "addItemToCustomerCart");
    private final static QName _ValidateResponse_QNAME = new QName("http://webservice.tcf.isa.polytech.unice.fr/", "validateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: stubs.cart
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EmptyCartException }
     * 
     */
    public EmptyCartException createEmptyCartException() {
        return new EmptyCartException();
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
     * Create an instance of {@link PaymentException }
     * 
     */
    public PaymentException createPaymentException() {
        return new PaymentException();
    }

    /**
     * Create an instance of {@link AddItemToCustomerCart }
     * 
     */
    public AddItemToCustomerCart createAddItemToCustomerCart() {
        return new AddItemToCustomerCart();
    }

    /**
     * Create an instance of {@link ValidateResponse }
     * 
     */
    public ValidateResponse createValidateResponse() {
        return new ValidateResponse();
    }

    /**
     * Create an instance of {@link RemoveItemToCustomerCart }
     * 
     */
    public RemoveItemToCustomerCart createRemoveItemToCustomerCart() {
        return new RemoveItemToCustomerCart();
    }

    /**
     * Create an instance of {@link RemoveItemToCustomerCartResponse }
     * 
     */
    public RemoveItemToCustomerCartResponse createRemoveItemToCustomerCartResponse() {
        return new RemoveItemToCustomerCartResponse();
    }

    /**
     * Create an instance of {@link GetCustomerCartContentsResponse }
     * 
     */
    public GetCustomerCartContentsResponse createGetCustomerCartContentsResponse() {
        return new GetCustomerCartContentsResponse();
    }

    /**
     * Create an instance of {@link Validate }
     * 
     */
    public Validate createValidate() {
        return new Validate();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveItemToCustomerCartResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "removeItemToCustomerCartResponse")
    public JAXBElement<RemoveItemToCustomerCartResponse> createRemoveItemToCustomerCartResponse(RemoveItemToCustomerCartResponse value) {
        return new JAXBElement<RemoveItemToCustomerCartResponse>(_RemoveItemToCustomerCartResponse_QNAME, RemoveItemToCustomerCartResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Validate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "validate")
    public JAXBElement<Validate> createValidate(Validate value) {
        return new JAXBElement<Validate>(_Validate_QNAME, Validate.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link EmptyCartException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "EmptyCartException")
    public JAXBElement<EmptyCartException> createEmptyCartException(EmptyCartException value) {
        return new JAXBElement<EmptyCartException>(_EmptyCartException_QNAME, EmptyCartException.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link PaymentException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "PaymentException")
    public JAXBElement<PaymentException> createPaymentException(PaymentException value) {
        return new JAXBElement<PaymentException>(_PaymentException_QNAME, PaymentException.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveItemToCustomerCart }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "removeItemToCustomerCart")
    public JAXBElement<RemoveItemToCustomerCart> createRemoveItemToCustomerCart(RemoveItemToCustomerCart value) {
        return new JAXBElement<RemoveItemToCustomerCart>(_RemoveItemToCustomerCart_QNAME, RemoveItemToCustomerCart.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddItemToCustomerCart }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "addItemToCustomerCart")
    public JAXBElement<AddItemToCustomerCart> createAddItemToCustomerCart(AddItemToCustomerCart value) {
        return new JAXBElement<AddItemToCustomerCart>(_AddItemToCustomerCart_QNAME, AddItemToCustomerCart.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.tcf.isa.polytech.unice.fr/", name = "validateResponse")
    public JAXBElement<ValidateResponse> createValidateResponse(ValidateResponse value) {
        return new JAXBElement<ValidateResponse>(_ValidateResponse_QNAME, ValidateResponse.class, null, value);
    }

}
