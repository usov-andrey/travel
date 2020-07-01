package org.humanhelper.travel.route.resolver;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.TimeRoute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Андрей
 * @since 04.10.15
 */
public class PlaceTimeRoutes {

    private List<TimeRoute> incoming = new ArrayList<>();
    private List<TimeRoute> outgoing = new ArrayList<>();

    public void addOutgoing(TimeRoute route) {
        outgoing.add(route);
    }

    public void addIncoming(TimeRoute route) {
        incoming.add(route);
    }

    public void addOutgoing(Collection<TimeRoute> routes) {
        outgoing.addAll(routes);
    }

    public void addIncoming(Collection<TimeRoute> routes) {
        incoming.addAll(routes);
    }

    public List<TimeRoute> getIncoming() {
        return incoming;
    }

    public List<TimeRoute> getSortedIncoming(Place place) {
        List<TimeRoute> routes = getIncoming();
        Collections.sort(routes, (o1, o2) -> place.getFirstDepartTime(o1.getEndTime()).compareTo(place.getFirstDepartTime(o2.getEndTime())));
        return routes;
    }

    public List<TimeRoute> getOutgoing() {
        return outgoing;
    }

    public List<TimeRoute> getSortedOutgoing(Place place) {
        List<TimeRoute> routes = getOutgoing();
        Collections.sort(routes, (o1, o2) -> place.getLastArriveTime(o1.getStartTime()).compareTo(place.getLastArriveTime(o2.getStartTime())));
        return routes;
    }

}
