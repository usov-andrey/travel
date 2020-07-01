package org.humanhelper.travel.service.frequency;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;

/**
 * Частота
 *
 * @author Андрей
 * @since 21.01.15
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "kind")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DailyFrequency.class, name = "dayly"),
        @JsonSubTypes.Type(value = DayFrequency.class, name = "day")
})
public abstract class Frequency {

    abstract public boolean inDay(Date day);
}
