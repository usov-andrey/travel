package org.humanhelper.travel.route;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.humanhelper.data.bean.id.Id;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.discount.DiscountResolver;
import org.humanhelper.travel.route.routesearch.newsearch.ActivityVisitor;
import org.humanhelper.travel.route.type.*;

/**
 * Это какое-то время провождение за определенную цену в каком-то месте
 *
 * @author Андрей
 * @since 22.12.14
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = ActivityBase.KIND_FIELD)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FixedTimeRoute.class, name = ActivityBase.FIXED_KIND),
        @JsonSubTypes.Type(value = CompositeFixedTimeRoute.class, name = "composite"),
        @JsonSubTypes.Type(value = AnyTimeRoute.class, name = ActivityBase.ANY_KIND),
        @JsonSubTypes.Type(value = ScheduleTimeRoute.class, name = ActivityBase.SCHEDULE_KIND),
        @JsonSubTypes.Type(value = StayInPlaceActivity.class, name = "stay")
})
public interface Activity extends Id<String> {

    PriceResolver getPriceResolver();

    void setPriceResolver(PriceResolver priceResolver);

    Place getSource();

    void visit(ActivityVisitor visitor);

    DiscountResolver getDiscountResolver();
}
