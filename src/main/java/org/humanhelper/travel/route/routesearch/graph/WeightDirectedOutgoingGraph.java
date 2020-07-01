package org.humanhelper.travel.route.routesearch.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Задается вес ребер
 * <p>
 * getTargets возвращает targets в случайном порядке
 *
 * @author Андрей
 * @since 28.10.15
 */
public class WeightDirectedOutgoingGraph<V, E> extends AbstractDirectedOutgoingGraph<V, Map<V, E>> {

    protected Map<V, E> createTargets() {
        return new HashMap<>();
    }

    @Override
    protected Set<V> toSet(Map<V, E> targets) {
        return targets.keySet();
    }

    public boolean addEdge(V source, V target, E edge) {
        Map<V, E> targets = getOrCreateTargets(source);
        return addEdge(targets.put(target, edge) == null);
    }

    public E getEdge(V source, V target) {
        Map<V, E> targets = targets(source);
        return targets != null ? targets.get(target) : null;
    }


}
