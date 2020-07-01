package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.Activity;

/**
 * @author Андрей
 * @since 05.12.14
 */
public abstract class SubPathVertex<T extends Activity> extends Vertex<T> {

    public SubPathVertex(T routeItem) {
        super(routeItem);
    }
}
