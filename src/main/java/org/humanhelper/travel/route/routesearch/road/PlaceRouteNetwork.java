package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.routesearch.graph.SimpleDirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.graph.search.VisitedSet;
import org.humanhelper.travel.route.routesearch.graph.search.recursion.DFSRecursionGraphSearch;
import org.humanhelper.travel.route.routesearch.graph.search.recursion.DFSRecursionPathGraphVisitor;

import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Сеть маршрутов
 *
 * @author Андрей
 * @since 14.10.15
 */
public class PlaceRouteNetwork {

    private SimpleDirectedOutgoingGraph<Place> graph = new SimpleDirectedOutgoingGraph<>();

    public void addRoute(Place source, Place target) {
        graph.addEdge(source, target);
    }

    public Set<Place> getAllPlaces() {
        return graph.getVertexSet();
    }

    public void process(Place startPlace, BiConsumer<Place, Place> sourceTargetConsumer) {
        SourceTargetVisitor visitor = new SourceTargetVisitor(sourceTargetConsumer);
        new DFSRecursionGraphSearch<>(graph, visitor).search(startPlace);
    }

    public SimpleDirectedOutgoingGraph<Place> getGraph() {
        return graph;
    }

    private class SourceTargetVisitor extends DFSRecursionPathGraphVisitor<Place, Place> {
        private BiConsumer<Place, Place> consumer;
        private VisitedSet<Place> visitedSet = new VisitedSet<>();

        public SourceTargetVisitor(BiConsumer<Place, Place> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void visit(Place vertex) {
            if (path.notEmpty()) {
                consumer.accept(path.getValue(), vertex);
            }
            if (visitedSet.canVisit(vertex)) {
                visitWithAddToPath(vertex, path.add(vertex));
            }
        }

    }

}
