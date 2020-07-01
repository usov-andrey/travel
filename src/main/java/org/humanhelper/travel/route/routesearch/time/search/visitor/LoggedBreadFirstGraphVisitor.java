package org.humanhelper.travel.route.routesearch.time.search.visitor;

import org.humanhelper.travel.route.routesearch.time.Graph;
import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;

/**
 * @author Андрей
 * @since 04.09.15
 */
public class LoggedBreadFirstGraphVisitor extends BreadFirstGraphVisitor {

    private int level;

    public LoggedBreadFirstGraphVisitor(Graph graph) {
        super(graph);
    }

    public void visit(Vertex vertex, Path path) {
        level++;
        for (Vertex target : graph.getTargets(vertex)) {

            for (int i = 0; i < level; i++) {
                System.out.print("     ");
            }
            System.out.println(target);

            target.visit(this, path);
        }
        level--;
    }

    public void printStatistics() {

    }
}
