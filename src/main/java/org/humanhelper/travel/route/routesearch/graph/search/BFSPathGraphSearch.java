package org.humanhelper.travel.route.routesearch.graph.search;

import org.humanhelper.travel.route.routesearch.graph.DirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.graph.GraphSearch;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Обход в ширину
 *
 * @author Андрей
 * @since 27.10.15
 */
public class BFSPathGraphSearch<V, P extends Path<V>> implements GraphSearch<V> {

    private DirectedOutgoingGraph<V> graph;
    private GraphPathVisitor<V, P> visitor;
    private Queue<P> vertexQueue;

    public BFSPathGraphSearch(DirectedOutgoingGraph<V> graph, GraphPathVisitor<V, P> visitor) {
        this.graph = graph;
        this.visitor = visitor;
        this.vertexQueue = new LinkedList<>();
    }

    @Override
    public void search(V vertex) {
        P path = visitor.start(vertex);
        if (path != null) {
            vertexQueue.add(path);
        }
        while (!vertexQueue.isEmpty()) {
            path = vertexQueue.poll();
            V source = path.getValue();
            for (V target : graph.getTargets(source)) {
                //Добавляем target в очередь
                P newPath = visitor.visit(target, path);
                if (newPath != null) {
                    vertexQueue.add(newPath);
                }
            }
        }
    }

}
