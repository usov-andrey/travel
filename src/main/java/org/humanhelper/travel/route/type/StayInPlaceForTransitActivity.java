package org.humanhelper.travel.route.type;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.routesearch.newsearch.ActivityVisitor;

import java.util.Date;

/**
 * @author Андрей
 * @since 04.10.15
 */
public class StayInPlaceForTransitActivity extends StayInPlaceActivity {

    public StayInPlaceForTransitActivity(Place place) {
        super(place);
    }

    public StayInPlaceForTransitActivity(Place place, PriceResolver priceResolver, Date startDate) {
        super(place, priceResolver, startDate, 1);
    }

    @Override
    public String toString() {
        return "Transit in " + source.nameOrId() +
                (startTime != null ?
                        " at " + startTime : "") +
                (priceResolver != null ? " with price:" + getPriceResolver() : "");
    }

    @Override
    public void visit(ActivityVisitor visitor) {
        visitor.transit(this);
    }
}
