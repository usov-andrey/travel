package org.humanhelper.travel.route.routesearch.newsearch.stopgraph;

import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.RouteSearchResult;
import org.humanhelper.travel.route.routesearch.graph.search.Path;
import org.humanhelper.travel.route.routesearch.graph.search.recursion.DFSRecursionGraphSearch;
import org.humanhelper.travel.route.routesearch.graph.search.recursion.DFSRecursionPathGraphVisitor;
import org.humanhelper.travel.route.routesearch.newsearch.EndActivity;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop.BestRouteWithDiscountCalculator;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop.RouteToStopActivity;
import org.humanhelper.travel.route.type.CompositeActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Андрей
 * @since 03.11.15
 */
public class StopActivityGraphVisitor extends DFSRecursionPathGraphVisitor<StopActivityVertex, StopActivityVertex> {

    private EndActivity endActivity;
    private int count;
    //Данные заполняемые при обходе графа
    private float routesPrice;//цена перемещений, для которых нету скидки
    private Set<StopActivityVertex> discountVertexSet;//вершины, для которых возможна скидка
    //Лучший найденный результат
    private Result bestResult;

    public StopActivityGraphVisitor(EndActivity endActivity) {
        this.endActivity = endActivity;
    }

    @Override
    public void start(DFSRecursionGraphSearch<StopActivityVertex> search, StopActivityVertex vertex) {
        count = 0;
        path = Path.empty();
        routesPrice = 0;
        discountVertexSet = new HashSet<>();
        bestResult = null;
        setSearch(search);
        visitWithoutAddToPath(vertex);
    }

    @Override
    public void visit(StopActivityVertex vertex) {
        //Идем дальше
        if (vertex.haveDiscountRoute()) {
            //Если есть маршруты со скидками, то нужно добавить их в список, для посчета
            //итоговой стоимости, когда полный маршрут будет сформирован
            discountVertexSet.add(vertex);
        } else {
            //Если внутри нет скидок на маршруты, то там один маршрут с минимальной ценой
            routesPrice += vertex.getRoute().getScore().getPrice();
        }

        if (vertex.getActivity().equals(endActivity)) {
            count++;
            //Дошли до конца, вычисляем результат и обновляем лучший результат
            Result result = new Result(path.add(vertex), routesPrice, discountVertexSet);
            if (bestResult == null || result.better(bestResult)) {
                bestResult = result;
            }
        } else {
            visitWithAddToPath(vertex, path.add(vertex));
        }

        if (vertex.haveDiscountRoute()) {
            discountVertexSet.remove(vertex);
        } else {
            routesPrice -= vertex.getRoute().getScore().getPrice();
        }

    }

    public void addToResult(RouteSearchResult result) {
        //log.debug("Count:"+count);
        if (bestResult != null) {
            bestResult.addToResult(result);
        }
    }


    public class Result extends BestRouteWithDiscountCalculator {
        private float routesPrice;//стоимость маршрутов между остановками
        private Path<StopActivityVertex> path;

        public Result(Path<StopActivityVertex> path, float routesPrice, Set<StopActivityVertex> discountVertexSet) {
            calculate(discountVertexSet);
            this.routesPrice = routesPrice + getPriceWithDiscount();
            this.path = path;
        }

        public boolean better(Result result) {
            return routesPrice < result.routesPrice;
        }

        public CompositeActivity addToResult(RouteSearchResult result) {
            CompositeActivity compositeActivity = new CompositeActivity();
            result.addRouteWithMinPriceWithoutStop(compositeActivity, routesPrice);
            compositeActivity.incPrice(routesPrice);
            path.startToEnd(vertex -> {
                Activity stop = vertex.getActivity();
                RouteToStopActivity route = getBestRoute().get(vertex);
                if (route == null) {
                    //Если маршрута со скидкой не найдено, то в activityWithPriceRoute содержится единственный маршрут
                    route = vertex.getRoute();
                }
                compositeActivity.addActivities(route.getActivities());
                //Увеличиваем цену за остановку
                if (!stop.equals(endActivity)) {
                    compositeActivity.addActivity(stop);
                    compositeActivity.incPrice(stop.getPriceResolver().getPrice());
                }
            });
            return compositeActivity;
        }
    }
}
