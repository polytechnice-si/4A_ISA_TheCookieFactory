
package stubs.customerCare;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour cookies.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="cookies">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CHOCOLALALA"/>
 *     &lt;enumeration value="DARK_TEMPTATION"/>
 *     &lt;enumeration value="SOO_CHOCOLATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cookies")
@XmlEnum
public enum Cookies {

    CHOCOLALALA,
    DARK_TEMPTATION,
    SOO_CHOCOLATE;

    public String value() {
        return name();
    }

    public static Cookies fromValue(String v) {
        return valueOf(v);
    }

}
