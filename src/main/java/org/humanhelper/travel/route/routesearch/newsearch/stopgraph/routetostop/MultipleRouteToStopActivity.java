package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.newsearch.score.BestPriceWithDiscountScoreList;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;

import java.util.List;

/**
 * @author Андрей
 * @since 04.11.15
 */
public class MultipleRouteToStopActivity extends BestPriceWithDiscountScoreList<SingleRouteToStopActivity> implements RouteToStopActivity {

    public MultipleRouteToStopActivity(SingleRouteToStopActivity singleRouteToStopActivity, SingleRouteToStopActivity route2) {
        super(singleRouteToStopActivity, singleRouteToStopActivity.getScore());
        if (!add(route2, route2.getScore())) {
            throw new IllegalStateException();
        }
    }

    @Override
    public List<Activity> getActivities() {
        //Данный метод может вызываться только у SingleRouteToStopActivity
        throw new IllegalStateException();
    }

    @Override
    public PriceWithDiscountScore getScore() {
        return minScore;
    }

    @Override
    public RouteToStopActivity join(SingleRouteToStopActivity route) {
        add(route, route.getScore());
        return this;
    }

    @Override
    public boolean haveDiscountActivities() {
        return true;//Раз у нас есть несколько вложенных маршрутов, то среди них по любому должен быть один с discountActivities
    }

    @Override
    public List<SingleRouteToStopActivity> getChildRoutes() {
        return getSortedKeys();
    }

    @Override
    public SingleRouteToStopActivity getBestRouteWithoutDiscount() {
        return getMinPriceKey();
    }
}
