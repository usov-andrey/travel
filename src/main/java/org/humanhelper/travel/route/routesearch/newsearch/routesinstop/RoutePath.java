package org.humanhelper.travel.route.routesearch.newsearch.routesinstop;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.routesearch.graph.search.Path;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;
import org.humanhelper.travel.route.type.TimeActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Андрей
 * @since 07.11.15
 */
public class RoutePath extends Path<TimeActivity> {

    protected PriceWithDiscountScore score;
    protected Set<Place> visitedPlaces;

    public RoutePath() {
        this.score = new PriceWithDiscountScore();
        this.visitedPlaces = new HashSet<>();
    }

    public RoutePath(TimeActivity vertex, RoutePath previous) {
        super(vertex, previous);
        this.score = new PriceWithDiscountScore(previous.score);
        this.visitedPlaces = new HashSet<>();
        this.visitedPlaces.addAll(previous.visitedPlaces);
    }

    public static RoutePath start(TimeActivity vertex) {
        return new RoutePath(vertex, new RoutePath());
    }

    public RoutePath add(TimeActivity vertex) {
        return new RoutePath(vertex, this);
    }
}
