package org.humanhelper.travel.route.routesearch.time.experiment;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.routesearch.time.Graph;
import org.humanhelper.travel.route.routesearch.time.search.vertex.StopPlaceVertex;
import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Андрей
 * @since 06.12.14
 */
public class GraphStatisticsVisitor {

    Graph graph;
    private int routesCount;
    private Map<Place, Map<Date, StopPlaceVertex>> stops = new HashMap<>();

    public GraphStatisticsVisitor(Graph graph) {
        this.graph = graph;
    }

    public void visit(Vertex vertex) {
        for (Vertex target : graph.getTargets(vertex)) {
            visit(target);
            if (target instanceof StopPlaceVertex) {
                StopPlaceVertex stop = (StopPlaceVertex) target;
                Place place = stop.getRouteItem().getSource();
                Map<Date, StopPlaceVertex> stopsByPlace = stops.get(place);
                if (stopsByPlace == null) {
                    stopsByPlace = new HashMap<>();
                    stops.put(place, stopsByPlace);
                } else {
                    StopPlaceVertex oldStop = stopsByPlace.get(stop.getRouteItem().getStartTime());
                    if (oldStop != null && (oldStop != stop)) {
                        if (Objects.equals(oldStop.getRouteItem().getNights(), stop.getRouteItem().getNights())) {
                            throw new IllegalStateException("Found duplicate stop:" + stop + " in stops:" + stopsByPlace);
                        }
                    }
                }
                stopsByPlace.put(stop.getRouteItem().getStartTime(), stop);
            }
            routesCount++;
        }
    }

    public void start(Vertex vertex) {
        for (Vertex target : graph.getTargets(vertex)) {
            visit(target);
            routesCount++;
            return;
        }
    }

    public void printStatistics() {
        System.out.println("Routes count:" + routesCount);
    }
}
