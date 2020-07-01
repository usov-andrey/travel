package org.humanhelper.travel.route.routesearch.graph;

import java.util.Collection;

/**
 * Граф, где возможно движение только по направлению ребра
 *
 * @author Андрей
 * @since 22.10.15
 */
public interface DirectedOutgoingGraph<V> {

    boolean addVertex(V vertex);

    Collection<? extends V> getTargets(V source);
}
