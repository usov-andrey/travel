package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.SubPath;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.type.StayInPlaceActivity;

/**
 * Посещать транзитное место после остановки можно только один раз, это задается в SubPath
 *
 * @author Андрей
 * @since 03.12.14
 */
public class TransitVertex extends SubPathVertex<StayInPlaceActivity> {

    public TransitVertex(StayInPlaceActivity placeRouteItem) {
        super(placeRouteItem);
    }

    public void visit(GraphVisitor visitor, Path path) {
        SubPath subPath = path.getSubPath();
        if (subPath.canTransit()) {
            subPath.addTransit(this);
            visitor.visit(this, path);
            subPath.removeTransit(this);
        }
    }

    public int hashCode() {
        return 31 * TransitVertex.class.hashCode() + routeItem.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + " Transit";
    }
}
