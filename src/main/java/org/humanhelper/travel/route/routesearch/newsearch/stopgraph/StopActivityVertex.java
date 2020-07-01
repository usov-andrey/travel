package org.humanhelper.travel.route.routesearch.newsearch.stopgraph;

import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop.RouteToStopActivity;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop.SingleRouteToStopActivity;
import org.humanhelper.travel.route.type.StopActivity;

/**
 * Вершина в графе остановок
 *
 * @author Андрей
 * @since 30.10.15
 */
public class StopActivityVertex {

    private StopActivity activity;
    private RouteToStopActivity routeToActivity;

    /**
     * Для стартовой вершины пути до нее нет
     */
    public StopActivityVertex(StopActivity activity) {
        this.activity = activity;
    }

    public StopActivityVertex(StopActivity activity, RouteToStopActivity routeToActivity) {
        this(activity);
        this.routeToActivity = routeToActivity;
    }

    /**
     * Добавляем еще один маршрут до текущей activity
     */
    public boolean addPriceRoute(SingleRouteToStopActivity route) {
        //Текущий routeToActivity может быть как простым маршрутом, так и списком маршрутов со скидкой
        //или старый маршрут лучше
        if (isRoute1Better(routeToActivity, route)) {
            return false;
        }
        //или новый маршрут лучше
        if (isRoute1Better(route, routeToActivity)) {
            routeToActivity = route;
        } else {
            //или оба маршрута остаются
            routeToActivity = routeToActivity.join(route);
        }
        return true;
    }

    private boolean isRoute1Better(RouteToStopActivity route1, RouteToStopActivity route2) {
        //Нужно выкинуть route2, если его цена со скидкой будет больше, чем цена route1 без скидки
        return route2.getScore().getPriceWithDiscount() > route1.getScore().getPrice();
    }

    public boolean haveDiscountRoute() {
        return routeToActivity.haveDiscountActivities();
    }

    public StopActivity getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return "activity=" + activity +
                ", route=" + routeToActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StopActivityVertex)) return false;

        StopActivityVertex that = (StopActivityVertex) o;

        return activity.equals(that.activity);

    }

    @Override
    public int hashCode() {
        return activity.hashCode();
    }

    public RouteToStopActivity getRoute() {
        return routeToActivity;
    }


}
