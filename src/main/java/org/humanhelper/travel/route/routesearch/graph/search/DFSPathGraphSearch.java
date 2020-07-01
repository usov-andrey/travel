package org.humanhelper.travel.route.routesearch.graph.search;

import org.humanhelper.travel.route.routesearch.graph.DirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.graph.GraphSearch;

/**
 * @author Андрей
 * @since 27.10.15
 */
public class DFSPathGraphSearch<V, P extends Path<V>> implements GraphSearch<V> {

    private DirectedOutgoingGraph<V> graph;
    private GraphPathVisitor<V, P> visitor;

    public DFSPathGraphSearch(DirectedOutgoingGraph<V> graph, GraphPathVisitor<V, P> visitor) {
        this.graph = graph;
        this.visitor = visitor;
    }

    @Override
    public void search(V vertex) {
        search(vertex, visitor.start(vertex));
    }

    private void search(V vertex, P path) {
        if (path != null) {
            for (V target : graph.getTargets(vertex)) {
                search(target, visitor.visit(target, path));
            }
        }
    }

}
