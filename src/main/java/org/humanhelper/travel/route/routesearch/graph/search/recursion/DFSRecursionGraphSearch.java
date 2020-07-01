package org.humanhelper.travel.route.routesearch.graph.search.recursion;

import org.humanhelper.travel.route.routesearch.graph.DirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.graph.GraphSearch;

/**
 * @author Андрей
 * @since 28.10.15
 */
public class DFSRecursionGraphSearch<V> implements GraphSearch<V> {

    protected DirectedOutgoingGraph<V> graph;
    private DFSRecursionGraphVisitor<V> visitor;

    public DFSRecursionGraphSearch(DirectedOutgoingGraph<V> graph, DFSRecursionGraphVisitor<V> visitor) {
        this.graph = graph;
        this.visitor = visitor;
    }

    /**
     * Начинаем поиск
     */
    public final void search(V vertex) {
        visitor.start(this, vertex);
    }

    protected final void visit(V vertex) {
        visitor.visit(vertex);
    }

    public void searchInTargets(V vertex) {
        graph.getTargets(vertex).forEach(
                this::visit);
    }
}
