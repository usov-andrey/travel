package org.humanhelper.travel.route.routesearch.time.experiment;

import org.humanhelper.travel.route.routesearch.time.Graph;
import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Андрей
 * @since 09.12.14
 */
public class GraphComparer {

    private Graph graph1;
    private Graph graph2;

    public GraphComparer(Graph graph1, Graph graph2) {
        this.graph1 = graph1;
        this.graph2 = graph2;
    }

    public void visit(Vertex v1, Vertex v2) {
        System.out.println("Visit1:" + v1);
        System.out.println("Visit2:" + v2);
        Collection<Vertex> targets1 = graph1.getTargets(v1);
        Collection<Vertex> targets2 = graph2.getTargets(v2);
        if (targets1.size() != targets2.size()) {
            throw new IllegalStateException("Different Target counts for Vertexs:\n" + v1 + "\n" + v2);
        }
        Iterator<Vertex> it1 = targets1.iterator();
        Iterator<Vertex> it2 = targets2.iterator();
        while (it1.hasNext()) {
            Vertex target1 = it1.next();
            if (!it2.hasNext()) {
                throw new IllegalStateException("Different targets for vertexs:" + v1 + " " + v2);
            }
            Vertex target2 = it2.next();
            if (!target1.equals(target2)) {
                throw new IllegalStateException("Different Vertexs:\n" + target1 + "\n" + target2);
            }
            visit(target1, target2);
        }
        System.out.println("Go back");
    }
}
