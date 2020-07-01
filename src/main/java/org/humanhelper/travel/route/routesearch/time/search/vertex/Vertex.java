package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;

/**
 * Все вершины делятся на два типа PathVertex и SubPathVertex
 * Для каждой PathVertex вычисляются оптимальные пути достижения этих вершин
 *
 * @author Андрей
 * @since 05.12.14
 */
public abstract class Vertex<T extends Activity> {

    protected T routeItem;

    public Vertex(T routeItem) {
        this.routeItem = routeItem;
    }

    public T getRouteItem() {
        return routeItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;

        Vertex that = (Vertex) o;

        if (!routeItem.equals(that.routeItem)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return routeItem.hashCode();
    }

    @Override
    public String toString() {
        return routeItem.toString();
    }

    abstract public void visit(GraphVisitor visitor, Path path);
}
