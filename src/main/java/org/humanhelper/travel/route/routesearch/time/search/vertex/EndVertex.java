package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.PathRouteSearchResult;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.type.StayInPlaceActivity;

/**
 * @author Андрей
 * @since 01.12.14
 */
public class EndVertex extends Vertex<StayInPlaceActivity> {

    private PathRouteSearchResult resultRouteList;

    public EndVertex(PathRouteSearchResult resultRouteList) {
        super(null);
        this.resultRouteList = resultRouteList;
    }

    public int hashCode() {
        return EndVertex.class.hashCode();
    }

    @Override
    public void visit(GraphVisitor visitor, Path path) {
        if (path.isVisitedAllPathVertexs()) {
            resultRouteList.addRoute(path);
        }
    }

    @Override
    public String toString() {
        return "End";
    }
}
