package org.humanhelper.travel.route.routesearch.graph.dijkstra;

import org.humanhelper.travel.route.routesearch.graph.DirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.graph.GraphSearch;

import java.util.Collection;
import java.util.Queue;

/**
 * @author Андрей
 * @since 22.10.15
 */
public abstract class QueueGraphSearch<V> implements GraphSearch<V> {

    private DirectedOutgoingGraph<V> graph;
    private Queue<V> vertexQueue;

    public QueueGraphSearch(DirectedOutgoingGraph<V> graph, Queue<V> vertexQueue) {
        this.graph = graph;
        this.vertexQueue = vertexQueue;
    }

    @Override
    public void search(V vertex) {
        vertexQueue.add(vertex);
        search();
    }

    protected Collection<? extends V> getTargets(V source) {
        return graph.getTargets(source);
    }

    private void search() {
        while (!vertexQueue.isEmpty()) {
            V source = vertexQueue.poll();
            for (V target : getTargets(source)) {
                if (canVisit(source, target)) {
                    //Добавляем target в очередь
                    vertexQueue.remove(target);
                    vertexQueue.add(target);
                }
            }
        }
    }

    abstract protected boolean canVisit(V source, V target);

}
