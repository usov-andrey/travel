package org.humanhelper.travel.route.routesearch.graph.search;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Андрей
 * @since 24.10.15
 */
public class VisitedSet<V> {

    private Set<V> visited = new HashSet<>();

    public boolean canVisit(V vertex) {
        //Если вершина новая, то идем дальше
        return visited.add(vertex);
    }

    public Set<V> getVisited() {
        return visited;
    }

}
