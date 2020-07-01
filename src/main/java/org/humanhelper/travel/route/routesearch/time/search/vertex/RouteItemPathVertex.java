package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;

import java.util.Collection;

/**
 * @author Андрей
 * @since 08.12.14
 */
public class RouteItemPathVertex<T extends Activity> extends PathVertex<T> {

    //Данные для обхода
    protected PathVertex lastStop;

    public RouteItemPathVertex(T routeItem) {
        super(routeItem);
    }

    @Override
    public void fillRoute(Collection<Activity> route) {
        lastStop.fillRoute(route);
        fillCurrentRoute(route);
        super.fillRoute(route);
    }

    protected void fillCurrentRoute(Collection<Activity> route) {
        route.add(routeItem);
    }

    @Override
    public void visit(GraphVisitor visitor, Path path) {
        lastStop = path.setStopVertex(this);
        if (path.isBestRouteToThisPlace()) {
            subPath.clear();
            visitor.visit(this, path);
            //subPath.clear();//Для уменьшения обьема памяти
        }
        path.setStopVertex(lastStop);
    }

    public int getStopsHashCode() {
        return lastStop.getStopsHashCode();
    }

    public int getPathHashCode() {
        return lastStop.getPathHashCode();
    }

    @Override
    public float getPrice() {
        return super.getPrice() + getCurrentPrice() + lastStop.getPrice();
    }

    @Override
    public float getSubPrice() {
        return super.getSubPrice() + lastStop.getSubPrice();
    }

    protected float getCurrentPrice() {
        return routeItem.getPriceResolver().getPrice();
    }

    public String pathToString() {
        return lastStop.pathToString() + " " + toString() + " " + super.pathToString();
    }
}
