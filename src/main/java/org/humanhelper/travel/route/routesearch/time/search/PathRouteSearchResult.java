package org.humanhelper.travel.route.routesearch.time.search;

import org.humanhelper.travel.route.routesearch.RouteSearchResult;
import org.humanhelper.travel.route.type.CompositeActivity;

/**
 * @author Андрей
 * @since 05.12.14
 */
public class PathRouteSearchResult extends RouteSearchResult {

    public void addRoute(Path path) {
        CompositeActivity route = path.createRoute();
        addRouteWithMinPriceWithoutStop(route, 0);
    }

}
