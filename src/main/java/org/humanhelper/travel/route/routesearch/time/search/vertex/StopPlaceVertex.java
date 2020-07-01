package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.routesearch.query.RouteQueryStop;
import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.type.StayInPlaceActivity;

/**
 * @author Андрей
 * @since 01.12.14
 */
public class StopPlaceVertex extends RouteItemPathVertex<StayInPlaceActivity> {

    private RouteQueryStop stop;

    public StopPlaceVertex(RouteQueryStop stop, StayInPlaceActivity placeRouteItem) {
        super(placeRouteItem);
        this.stop = stop;
    }

    public RouteQueryStop getStop() {
        return stop;
    }

    @Override
    public void visit(GraphVisitor visitor, Path path) {
        if (path.addStop(stop)) {
            super.visit(visitor, path);
            path.removeStop(stop);
        }
    }

    @Override
    public int getPathHashCode() {
        return 31 * super.getStopsHashCode() //+ placeRouteItem.hashCodeWithoutStartDate();
                + stop.hashCode();//Не важно сколько ночей оставаться
    }

    @Override
    public int getStopsHashCode() {
        return 31 * super.getStopsHashCode()
                + stop.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + "-StopPlace";
    }
}
