package org.humanhelper.travel.route.type;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.routesearch.newsearch.ActivityVisitor;

import java.util.Date;
import java.util.List;

/**
 * Набор перемещений из Source в Target за определенное время
 *
 * @author Андрей
 * @since 21.01.15
 */
public class CompositeFixedTimeRoute extends RouteBase implements TimeRoute {

    protected List<TimeRoute> routeItems;//Маршрут

    public CompositeFixedTimeRoute(List<TimeRoute> routeItems) {
        if (routeItems.isEmpty()) {
            throw new IllegalArgumentException("Wrong size of composite route:" + routeItems);
        }
        this.routeItems = routeItems;
    }

    @Override
    public TimeRoute copy() {
        return new CompositeFixedTimeRoute(routeItems);
    }

    public List<? extends TimeRoute> getRouteItems() {
        return routeItems;
    }

    public void setRouteItems(List<TimeRoute> routeItems) {
        this.routeItems = routeItems;
    }

    public void addRouteItem(TimeRoute item) {
        this.routeItems.add(item);
    }

    @Override
    public Place getSource() {
        return getFirst().getSource();
    }

    @Override
    public Date getStartTime() {
        return getFirst().getStartTime();
    }

    @Override
    public void setStartTime(Date startTime) {
        getFirst().setStartTime(startTime);
    }

    @Override
    public Place getTarget() {
        return getLast().getTarget();
    }

    @Override
    public Date getEndTime() {
        return getLast().getEndTime();
    }

    @Override
    public void setEndTime(Date endTime) {
        getLast().setEndTime(endTime);
    }

    private TimeRoute getFirst() {
        return routeItems.get(0);
    }

    private TimeRoute getLast() {
        return routeItems.get(routeItems.size() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompositeFixedTimeRoute)) return false;

        CompositeFixedTimeRoute that = (CompositeFixedTimeRoute) o;

        if (!routeItems.equals(that.routeItems)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return routeItems.hashCode();
    }

    @Override
    public String toString() {
        return "Route{ " + routeItems + '}';
    }

    @Override
    public void visit(ActivityVisitor visitor) {
        visitor.timeRoute(this);
    }
}
