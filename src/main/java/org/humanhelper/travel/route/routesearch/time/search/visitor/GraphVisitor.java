package org.humanhelper.travel.route.routesearch.time.search.visitor;

import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;

/**
 * @author Андрей
 * @since 04.10.15
 */
public interface GraphVisitor {

    void visit(Vertex vertex, Path path);

    void printStatistics();
}
