package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.type.StayInPlaceActivity;

/**
 * @author Андрей
 * @since 01.12.14
 */
public class StartPlaceVertex extends PathVertex<StayInPlaceActivity> {

    public StartPlaceVertex() {
        super(null);
    }

    @Override
    public void visit(GraphVisitor visitor, Path path) {
        path.setStopVertex(this);
        visitor.visit(this, path);
    }

    public int hashCode() {
        return StartPlaceVertex.class.hashCode();
    }

    @Override
    public String toString() {
        return "StartPlaceVertex{" +
                "place=" + getRouteItem() +
                '}';
    }
}
