package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.SubPath;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.type.TimeRoute;

/**
 * @author Андрей
 * @since 05.12.14
 */
public class WayVertex extends SubPathVertex<TimeRoute> {


    public WayVertex(TimeRoute wayRouteItem) {
        super(wayRouteItem);
    }

    @Override
    public void visit(GraphVisitor visitor, Path path) {
        SubPath subPath = path.getSubPath();
        if (subPath.canUseThisWay(routeItem)) {
            subPath.addWay(this);
            visitor.visit(this, path);
            subPath.removeWay(this);
        }
    }

    public int hashCode() {
        return 31 * WayVertex.class.hashCode() + routeItem.hashCode();
    }


}
