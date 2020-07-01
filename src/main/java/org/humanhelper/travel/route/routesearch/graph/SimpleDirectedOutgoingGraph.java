package org.humanhelper.travel.route.routesearch.graph;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Храниние графа в виде Map
 * Порядок getTargets будет такой же в котром добавлялись ребра
 * Вес ребер не задан
 *
 * @author Андрей
 * @since 22.10.15
 */
public class SimpleDirectedOutgoingGraph<V> extends AbstractDirectedOutgoingGraph<V, Set<V>> {

    public boolean addEdge(V source, V target) {
        Set<V> targets = getOrCreateTargets(source);
        return addEdge(targets.add(target));
    }

    @Override
    protected Set<V> createTargets() {
        return new LinkedHashSet<>();
    }

    @Override
    protected Set<V> toSet(Set<V> targets) {
        return targets;
    }

    public Set<V> getVertexSet() {
        Set<V> result = new HashSet<>();
        for (V source : graph.keySet()) {
            result.add(source);
            for (V target : getTargets(source)) {
                result.add(target);
            }
        }
        return result;
    }
}
