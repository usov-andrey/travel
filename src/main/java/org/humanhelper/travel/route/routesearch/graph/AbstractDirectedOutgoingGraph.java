package org.humanhelper.travel.route.routesearch.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Общий targets граф
 *
 * @author Андрей
 * @since 28.10.15
 */
public abstract class AbstractDirectedOutgoingGraph<V, T> implements DirectedOutgoingGraph<V> {

    protected Map<V, T> graph = new HashMap<>();
    protected int edgesCount = 0;

    abstract protected T createTargets();

    @Override
    public boolean addVertex(V vertex) {
        if (!graph.containsKey(vertex)) {
            graph.put(vertex, createTargets());
            return true;
        }
        return false;
    }

    @Override
    public Set<V> getTargets(V source) {
        T targets = targets(source);
        return targets != null ? toSet(targets) : Collections.emptySet();
    }

    protected final T targets(V source) {
        return graph.get(source);
    }

    protected boolean addEdge(boolean isAdded) {
        if (isAdded) {
            edgesCount++;
        }
        return isAdded;
    }

    protected final T getOrCreateTargets(V source) {
        return graph.computeIfAbsent(source, v -> createTargets());
    }

    abstract protected Set<V> toSet(T targets);


    @Override
    public String toString() {
        return "Vertexs:" + graph.keySet().size() + " Edges:" + edgesCount;
    }
}
