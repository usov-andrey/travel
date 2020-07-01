package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;

import java.util.List;

/**
 * Маршрут до остановки
 * В списке activities нет перемещений с возможной скидкой
 *
 * @author Андрей
 * @since 04.11.15
 */
public interface RouteToStopActivity {

    List<Activity> getActivities();

    PriceWithDiscountScore getScore();

    RouteToStopActivity join(SingleRouteToStopActivity route);

    boolean haveDiscountActivities();

    SingleRouteToStopActivity getBestRouteWithoutDiscount();

    List<SingleRouteToStopActivity> getChildRoutes();

}
