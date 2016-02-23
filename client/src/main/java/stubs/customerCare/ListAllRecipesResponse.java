
package stubs.customerCare;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour listAllRecipesResponse complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="listAllRecipesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipes" type="{http://webservice.tcf.isa.polytech.unice.fr/}cookies" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listAllRecipesResponse", propOrder = {
    "recipes"
})
public class ListAllRecipesResponse {

    @XmlSchemaType(name = "string")
    protected List<Cookies> recipes;

    /**
     * Gets the value of the recipes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recipes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecipes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Cookies }
     * 
     * 
     */
    public List<Cookies> getRecipes() {
        if (recipes == null) {
            recipes = new ArrayList<Cookies>();
        }
        return this.recipes;
    }

}
