package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.time.search.SubPath;
import org.humanhelper.travel.route.routesearch.time.search.pareto.ParetoSubPath;

import java.util.Collection;

/**
 * @author Андрей
 * @since 05.12.14
 */
public abstract class PathVertex<T extends Activity> extends Vertex<T> {

    protected SubPath subPath;//Подпуть после текущей вершины


    public PathVertex(T routeItem) {
        super(routeItem);
        subPath = new ParetoSubPath();
    }

    public SubPath getSubPath() {
        return subPath;
    }

    /**
     * Хэш код пути включая перемещения туда-обратно
     */
    public int getPathHashCode() {
        return 0;
    }

    /**
     * @return Хэш код только RouteQueryStop
     */
    public int getStopsHashCode() {
        return 0;
    }

    public void fillRoute(Collection<Activity> route) {
        subPath.fillRoute(route);
    }

    public String pathToString() {
        return subPath.toString();
    }

    public float getPrice() {
        return subPath.getPrice();
    }

    public float getSubPrice() {
        return subPath.getPrice();
    }
}
