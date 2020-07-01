package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.StopActivityVertex;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Андрей
 * @since 04.11.15
 */
public class BestRouteWithDiscountCalculator {

    private float bestRoutePrice = Float.MAX_VALUE;
    private Map<StopActivityVertex, SingleRouteToStopActivity> bestRoute;

    /**
     * Для списка остановок ищет оптимальный маршрут между ними с учетом скидов
     */

    public void calculate(Set<StopActivityVertex> discountVertexSet) {
        //Для каждой вершины заполняем список возможных маршрутов до нее
        Map<StopActivityVertex, Collection<SingleRouteToStopActivity>> routes = new HashMap<>();
        for (StopActivityVertex vertex : discountVertexSet) {
            Collection<SingleRouteToStopActivity> vertexRoutes = vertex.getRoute().getChildRoutes();
            if (vertexRoutes.size() > 200) {
                System.out.println(vertexRoutes.size() + " routes to " + vertex.getActivity());
            }
            routes.put(vertex, vertexRoutes);
        }

        BestScoreSetReducer<StopActivityVertex, SingleRouteToStopActivity> bestScoreSetReducer = new BestScoreSetReducer<StopActivityVertex, SingleRouteToStopActivity>() {
            @Override
            protected Pair<Float, Float> getScorePair(SingleRouteToStopActivity key1, SingleRouteToStopActivity key2) {
                Pair<SingleRouteToStopActivity, SingleRouteToStopActivity> routesPair = key1.getRouteWithDiscountOrNull(key2);
                if (routesPair == null) {
                    return null;
                }
                return new ImmutablePair<>(routesPair.getLeft().getScore().getPrice(), routesPair.getRight().getScore().getPrice());
            }
        };
        bestScoreSetReducer.reduce(routes);

        BestScoreSetCalculator<StopActivityVertex, SingleRouteToStopActivity> bestScoreSetCalculator = new BestScoreSetCalculator<StopActivityVertex, SingleRouteToStopActivity>() {
            @Override
            protected Float getScore(SingleRouteToStopActivity key, SingleRouteToStopActivity forKey) {
                return bestScoreSetReducer.getScore(key, forKey);
            }
        };
        bestScoreSetCalculator.calculateBestPath(bestScoreSetReducer.getTargetKeys());
        bestRoutePrice = bestScoreSetCalculator.getBestScore();
        bestRoute = bestScoreSetCalculator.getBestKeys();
        if (bestRoute == null) {
            //Значит не нашли ничего со скидками
            bestRoute = new HashMap<>();
        }
        for (StopActivityVertex vertex : discountVertexSet) {
            if (bestRoute.get(vertex) == null) {
                SingleRouteToStopActivity routeWithoutDiscount = vertex.getRoute().getBestRouteWithoutDiscount();
                bestRoutePrice += routeWithoutDiscount.getScore().getPrice();
                bestRoute.put(vertex, routeWithoutDiscount);
            }
        }
    }

    public Map<StopActivityVertex, SingleRouteToStopActivity> getBestRoute() {
        return bestRoute;
    }

    public float getPriceWithDiscount() {
        return bestRoutePrice;
    }
}
