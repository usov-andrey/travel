package org.humanhelper.travel.route.routesearch.newsearch.stopgraph;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.graph.DirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.graph.search.Path;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Граф остановок(Start, End, StayInPlace) для определенного пути мест placePath
 * В графа одна вершина Start и много вершин End
 *
 * @author Андрей
 * @since 30.10.15
 */
public class StopActivityGraph implements DirectedOutgoingGraph<StopActivityVertex> {

    private StopActivityMap map;
    private Map<Place, Place> placeMap;

    public StopActivityGraph(StopActivityMap map, Path<Place> placePath) {
        this.map = map;
        placeMap = new HashMap<>();
        fillPlaceMap(placePath);
    }

    private void fillPlaceMap(Path<Place> placePath) {
        if (placePath.getPrevious().notEmpty()) {
            fillPlaceMap(placePath.getPrevious());
            placeMap.put(placePath.getPrevious().getValue(), placePath.getValue());
        }
    }

    @Override
    public boolean addVertex(StopActivityVertex vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<? extends StopActivityVertex> getTargets(StopActivityVertex source) {
        Activity activity = source.getActivity();
        return map.getTarget(activity, getNextPlace(activity));
    }


    private Place getNextPlace(Activity activity) {
        return placeMap.get(activity.getSource());
    }
}
