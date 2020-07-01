package org.humanhelper.travel.route.routesearch.newsearch;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;

import java.util.Date;

/**
 * @author Андрей
 * @since 23.10.15
 */
public class StartActivity extends EndActivity {

    public StartActivity(Place source, Date time) {
        super(source, time);
    }

    public int hashCode() {
        return StartActivity.class.hashCode();
    }

    @Override
    public String toString() {
        return "S " + source.nameOrId() + " " + startTime;
    }

    @Override
    public void visit(ActivityVisitor visitor) {
        visitor.start(this);
    }

    @Override
    public PriceResolver getPriceResolver() {
        return PriceResolver.EMPTY;
    }

}
