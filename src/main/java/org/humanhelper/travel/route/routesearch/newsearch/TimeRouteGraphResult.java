package org.humanhelper.travel.route.routesearch.newsearch;

import org.humanhelper.travel.route.routesearch.graph.SimpleDirectedOutgoingGraph;
import org.humanhelper.travel.route.type.StayInPlaceActivity;
import org.humanhelper.travel.route.type.TimeActivity;

import java.util.List;
import java.util.Set;

/**
 * Результат
 *
 * @author Андрей
 * @since 16.12.15
 */
public class TimeRouteGraphResult {

    private SimpleDirectedOutgoingGraph<TimeActivity> graph;
    private Set<StayInPlaceActivity> stopVertexSet;
    private List<StartActivity> startActivities;
    private EndActivity endActivity;

    public TimeRouteGraphResult(SimpleDirectedOutgoingGraph<TimeActivity> graph, Set<StayInPlaceActivity> stopVertexSet, List<StartActivity> startActivities, EndActivity endActivity) {
        this.graph = graph;
        this.stopVertexSet = stopVertexSet;
        this.startActivities = startActivities;
        this.endActivity = endActivity;
    }

    public SimpleDirectedOutgoingGraph<TimeActivity> getGraph() {
        return graph;
    }

    public Set<StayInPlaceActivity> getStopVertexSet() {
        return stopVertexSet;
    }

    public List<StartActivity> getStartActivities() {
        return startActivities;
    }

    public EndActivity getEndActivity() {
        return endActivity;
    }
}
