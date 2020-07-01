package org.humanhelper.travel.route.routesearch.newsearch;

import org.humanhelper.travel.route.type.AnyTimeRoute;
import org.humanhelper.travel.route.type.StayInPlaceActivity;
import org.humanhelper.travel.route.type.StayInPlaceForTransitActivity;
import org.humanhelper.travel.route.type.TimeRoute;

/**
 * @author Андрей
 * @since 27.10.15
 */
public interface ActivityVisitor {

    void anyRoute(AnyTimeRoute vertex);

    void timeRoute(TimeRoute vertex);

    void stop(StayInPlaceActivity vertex);

    void transit(StayInPlaceForTransitActivity vertex);

    void start(StartActivity vertex);

    void end(EndActivity vertex);
}
