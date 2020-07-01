package org.humanhelper.travel.transport;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.humanhelper.travel.country.BeanWithCountry;
import org.humanhelper.travel.transport.air.Airline;

/**
 * @author Андрей
 * @since 05.09.15
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = TransportCompany.KIND_FIELD)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Airline.class, name = "airline")
        //@JsonSubTypes.Type(value = Town.class, name = "town"),
        //@JsonSubTypes.Type(value = Village.class, name = "village")
})
public class TransportCompany extends BeanWithCountry {

    public static final String KIND_FIELD = "kind";

    public TransportCompany name(String name) {
        setName(name);
        return this;
    }

}
