package org.humanhelper.travel.route.routesearch.time.search.visitor;

import org.humanhelper.travel.route.routesearch.time.Graph;
import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;

/**
 * Используем поиск в глубину
 *
 * @author Андрей
 * @since 02.12.14
 */
public class BreadFirstGraphVisitor implements GraphVisitor {

    Graph graph;
    //private int level;

    public BreadFirstGraphVisitor(Graph graph) {
        this.graph = graph;
    }

    public void visit(Vertex vertex, Path path) {
        //level++;
        for (Vertex target : graph.getTargets(vertex)) {

            //for (int i=0; i<level; i++) {
            //	System.out.print("     ");
            //}
            //System.out.println(target);

            target.visit(this, path);
        }
        //level--;
    }

    public void printStatistics() {

    }

}
