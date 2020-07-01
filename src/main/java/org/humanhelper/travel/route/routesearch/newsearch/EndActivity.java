package org.humanhelper.travel.route.routesearch.newsearch;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.type.StopActivity;

import java.util.Date;

/**
 * @author Андрей
 * @since 23.10.15
 */
public class EndActivity extends StopActivity {

    public EndActivity(Place source) {
        super(source);
    }

    /**
     * Для создания вершины с указанием времени, когда будет закончен маршрут
     */
    public EndActivity(Place source, Date startTime) {
        super(source);
        setStartTime(startTime);
        setEndTime(startTime);
    }

    public int hashCode() {
        return EndActivity.class.hashCode();
    }

    @Override
    public String toString() {
        return "E " + source.nameOrId() + startTime;
    }

    @Override
    public void visit(ActivityVisitor visitor) {
        visitor.end(this);
    }

    @Override
    public PriceResolver getPriceResolver() {
        return PriceResolver.EMPTY;
    }

}
