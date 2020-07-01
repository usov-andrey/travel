package org.humanhelper.travel.route.type;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.waydiscount.DiscountStrategy;

/**
 * Маршрут из source в target
 *
 * @author Андрей
 * @since 21.01.15
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = ActivityBase.KIND_FIELD)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FixedTimeRoute.class, name = "way"),
        @JsonSubTypes.Type(value = CompositeFixedTimeRoute.class, name = "composite"),
        @JsonSubTypes.Type(value = AnyTimeRoute.class, name = ActivityBase.ANY_KIND),
        @JsonSubTypes.Type(value = ScheduleTimeRoute.class, name = "sched"),
})
public interface Route extends Activity {

    Place getTarget();

    DiscountStrategy getDiscountStrategy();

}
