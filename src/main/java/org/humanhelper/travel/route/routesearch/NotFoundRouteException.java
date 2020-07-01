package org.humanhelper.travel.route.routesearch;

import org.humanhelper.travel.place.Place;

/**
 * *
 *
 * @author Андрей
 * @since 12.12.14
 */
public class NotFoundRouteException extends IllegalStateException {

    private Place source;
    private Place target;

    public NotFoundRouteException(Place source, Place target) {
        super("Not found route from " + source + " to " + target);
        this.source = source;
        this.target = target;
    }

    public Place getSource() {
        return source;
    }

    public Place getTarget() {
        return target;
    }
}
