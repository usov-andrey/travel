package org.humanhelper.travel.route.routesearch.newsearch;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.sourcetarget.SourceTarget;
import org.humanhelper.travel.route.routesearch.graph.search.Path;
import org.humanhelper.travel.route.routesearch.graph.search.recursion.DFSRecursionGraphSearch;
import org.humanhelper.travel.route.routesearch.graph.search.recursion.DFSRecursionPathGraphVisitor;
import org.humanhelper.travel.route.routesearch.newsearch.score.BestPriceWithDiscountScoreList;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Решения задачи GTSP:
 * https://www.ceid.upatras.gr/webpages/faculty/zaro/pub/conf/C40-atmos2004-rtsp.pdf
 * https://www.ac.tuwien.ac.at/files/pub/hu-08a.pdf
 * https://www.ac.tuwien.ac.at/files/pub/hu-08.pdf
 * http://www.researchgate.net/publication/282502699_A_Unifying_Survey_of_Agent-Based_Approaches_for_Equality-Generalized_Traveling_Salesman_Problem
 * <p>
 * <p>
 * Необходимо найти все гамильтоновы циклы у которых priceWithDiscount будет меньше минимальной price
 * Используем алгоритм branch & bound
 * <p>
 * 1. Branch
 * При выборе следующего места вначале выбираем место с минимальной стоимостью
 * (данная логика реализована в конструкторе StopPlaceGraph)
 * <p>
 * 2. Bound
 * Если priceWithDiscount текущего маршрута меньше лучше найденной price, то идем дальше
 *
 * @author Андрей
 * @since 29.10.15
 */
public class PlaceTSPGraphVisitor extends DFSRecursionPathGraphVisitor<Place, Place> {

    private Map<SourceTarget, PriceWithDiscountScore> priceMap;
    private Place endPlace;
    private Set<Place> availableForVisitStopPlaces;
    private BestPriceWithDiscountScoreList<Path<Place>> bestPathList = new BestPriceWithDiscountScoreList<>(new PriceWithDiscountScore(Float.MAX_VALUE));
    private PriceWithDiscountScore score = new PriceWithDiscountScore();

    public PlaceTSPGraphVisitor(Map<SourceTarget, PriceWithDiscountScore> priceMap, RouteQuery query) {
        this.priceMap = priceMap;
        availableForVisitStopPlaces = new HashSet<>();
        availableForVisitStopPlaces.addAll(query.stopMap().keySet());
        endPlace = query.getEnd().getPlace();

    }

    @Override
    public void start(DFSRecursionGraphSearch<Place> search, Place vertex) {
        //Нам не нужно обрабатывать первую вершину, так как это стартовая вершина
        //и решение идти дальше или нет, мы принимаем на основе цены ребра(две вершины)
        //и нам не нужно, чтобы обработка первой вершины попала в метод visit
        path = Path.empty();
        setSearch(search);
        visitWithAddToPath(vertex, path.add(vertex));
    }

    public BestPriceWithDiscountScoreList<Path<Place>> getBestPathList() {
        return bestPathList;
    }

    private void routeCompleted(Place vertex) {
        bestPathList.add(path.add(vertex), new PriceWithDiscountScore(score));
    }

    @Override
    public void visit(Place vertex) {
        PriceWithDiscountScore sourceTargetScore = priceMap.get(new SourceTarget(path.getValue(), vertex));
        score.addPrice(sourceTargetScore);
        if (vertex.equals(endPlace)) {
            //Пришли в конечную остановку
            if (availableForVisitStopPlaces.isEmpty()) {
                routeCompleted(vertex);
            }
        } else if (bestPathList.canAdd(score)) {
            if (availableForVisitStopPlaces.remove(vertex)) {
                //Идем дальше, если текущая стоимость уже не больше средней стоимости лучшего маршрута
                //Считаем, что с такой эвристикой мы всегда в последующем сможем найти временной маршрут
                visitWithAddToPath(vertex, path.add(vertex));
                availableForVisitStopPlaces.add(vertex);
            }
        }

        score.decPrice(sourceTargetScore);
    }
}
