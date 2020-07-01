package org.humanhelper.travel.route.routesearch.newsearch;

import org.humanhelper.data.bean.name.ProxyNameIdBean;
import org.humanhelper.service.utils.TimerHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.sourcetarget.SourceTarget;
import org.humanhelper.travel.route.routesearch.RouteSearchResult;
import org.humanhelper.travel.route.routesearch.graph.SimpleDirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.graph.search.BFSPathGraphSearch;
import org.humanhelper.travel.route.routesearch.graph.search.Path;
import org.humanhelper.travel.route.routesearch.graph.search.recursion.DFSRecursionGraphSearch;
import org.humanhelper.travel.route.routesearch.newsearch.routesinstop.BestRoutesInStopsVisitor;
import org.humanhelper.travel.route.routesearch.newsearch.score.BestPriceWithDiscountScoreList;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.StopActivityGraph;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.StopActivityGraphVisitor;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.StopActivityMap;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.StopActivityVertex;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.road.PlaceRouteNetwork;
import org.humanhelper.travel.route.type.StayInPlaceActivity;
import org.humanhelper.travel.route.type.TimeActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Алгоритм:
 * 1. Создаем time-expanded граф - TimeRouteGraphProducer
 * 2.
 * <p>
 * Некоторые эвристики:
 * Считаем, что расписание формируется в зависимости от дня недели. Поэтому
 * найдя оптимальную последовательность остановок для каждого дня недели для последующих дней
 * можно считать, что последотельность остановок будет такая же.
 * 2. Вычисляем оптимальные пути между всеми созданными Stop вершинами(StayInPlaceActivity)
 * Из этого создаем новый граф
 * 3. Получаем k-best путей
 * <p>
 * Нашли мы последовательность мест
 * считаем стоимость между
 *
 * @author Андрей
 * @since 23.10.15
 */
public class NewTimeRouteSearchService {
    private static final Logger log = LoggerFactory.getLogger(NewTimeRouteSearchService.class);

    private void fillStopGraph(StopActivityMap activityPlaceMap, Map<SourceTarget, PriceWithDiscountScore> priceMap, TimeRouteGraphResult graphResult) {
        //Ищем пути из начальной точки во все остановки
        List<StartActivity> startActivityList = graphResult.getStartActivities();
        log.debug("Start vertex processed in ms:" + TimerHelper.run(() -> {
            for (StartActivity startActivity : startActivityList) {
                fillStopGraph(activityPlaceMap, graphResult.getGraph(), startActivity, new BestRoutesInStopsVisitor(activityPlaceMap, priceMap, startActivity) {
                    @Override
                    public void end(EndActivity vertex) {
                        //Начальную и конечную вершину не соединяем
                        routePath = null;
                    }
                });
            }
        }));
        //Из каждой остановки в другие
        log.debug("Stop vertex processed in ms:" + TimerHelper.run(() -> {
            for (StayInPlaceActivity stop : graphResult.getStopVertexSet()) {
                fillStopGraph(activityPlaceMap, graphResult.getGraph(), stop, new BestRoutesInStopsVisitor(activityPlaceMap, priceMap, stop));
                //log.debug("Graph:"+stopGraph);
            }
        }));
    }

    private void fillStopGraph(StopActivityMap activityPlaceMap, SimpleDirectedOutgoingGraph<TimeActivity> graph, TimeActivity startVertex, BestRoutesInStopsVisitor bestTransitRoutesVisitor) {
        //log.debug("Search by "+startVertex);
        activityPlaceMap.setStepCount(0);
        new BFSPathGraphSearch<>(graph, bestTransitRoutesVisitor).search(startVertex);
        //log.debug("Searched:"+bestTransitRoutesVisitor.getRealCount()+" of "+activityPlaceMap.getStepCount());
    }

    private Path<Place> getPath(RouteQuery query) {
        //String[] names = {"Seul", "Bangkok", "Pnompenh", "Hanoi", "Hongkong", "Manila"};
        //String[] names = {"Hanoi", "Hongkong", "Manila", "Bangkok", "Pnompenh", "Seul"};
        String[] names = {"Manila", "Pnompenh", "Seul", "Bangkok", "Hanoi", "Hongkong"};
        Path<Place> path = Path.empty();
        path = path.add(query.getStart().getPlace());
        for (String name : names) {
            for (Place place : query.getPlaces()) {
                if (place.getName().equals(name)) {
                    path = path.add(place);
                }
            }
        }
        path = path.add(query.getEnd().getPlace());
        return path;
    }

    public RouteSearchResult getResultRouteList(RouteQuery query, PlaceRouteNetwork places) {

        //1. Создаем time expanded граф
        TimeRouteGraphResult graphResult = new TimeRouteGraphProducer().fillGraph(query, places);

        log.debug("Creating stop graph");

        StopActivityMap activityPlaceMap = new StopActivityMap();
        Map<SourceTarget, PriceWithDiscountScore> priceMap = new HashMap<>();
        log.info("Stop graph created in ms:" + TimerHelper.run(() ->
                fillStopGraph(activityPlaceMap, priceMap, graphResult)
        ));
        //TODO для более быстрого поиска минимальных путей между остановками, нам необходимо,
        //чтобы для каждой вершины рассматировались переходы в следующие
        // вначале в остановки, потом route в порядке сортировки по стоимости
        //или в порядке сортировки по времени перемещений, в зависимости от того, чего мы ищем
        //и только уже потом шли транзитные остановки
        //Необходимо добавить сортировку TODO
        activityPlaceMap.log(log);
		/*
		log.debug("Best prices:");
		for (SourceTarget sourceTarget : priceMap.keySet()) {
			log.debug(sourceTarget.getSource().simple() + "-" + sourceTarget.getTarget().simple() + ": " + priceMap.get(sourceTarget));
		} */
        StopPlaceGraph stopPlaceGraph = new StopPlaceGraph(query, priceMap);
        PlaceTSPGraphVisitor placeTSPGraphVisitor = new PlaceTSPGraphVisitor(priceMap, query);
        log.debug("TSP searching");
        log.info("TSP calculated in ms:" + TimerHelper.run(() ->
                new DFSRecursionGraphSearch<>(stopPlaceGraph, placeTSPGraphVisitor).search(query.getStart().getPlace())
        ));
        BestPriceWithDiscountScoreList<Path<Place>> bestPaths = placeTSPGraphVisitor.getBestPathList();
        log.info("TSP found " + bestPaths.size() + " best paths, min score:" + bestPaths.getMinScore());

        Collection<Path<Place>> bestPathList = bestPaths.getSortedKeys();

        log.debug("Best paths:");
        for (Path<Place> path : bestPathList) {
            log.debug(path.toString(ProxyNameIdBean::nameOrId) + " price:" + bestPaths.getScore(path));
        }

		/*
		//Если нужно запустить именно с какой-то ручной последовательностью для тестов
		bestPathList.clear();
		bestPathList.add(getPath(query));
        */
        log.debug("Finding best routes");

        Map<StartActivity, RouteSearchResult> results = new HashMap<>();
        StopActivityGraphVisitor activityWithPriceGraphVisitor = new StopActivityGraphVisitor(graphResult.getEndActivity());
        //StopActivityVertex startActivityWithPrice = new StopActivityVertex(timeRouteGraphProducer.getStartActivity());
        //Для каждого дня начала мы находим по 3 лучших маршрута
        log.info("Best routes calculated in ms:" + TimerHelper.run(() -> {
            for (StartActivity startActivity : graphResult.getStartActivities()) {
                RouteSearchResult resultForDay = new RouteSearchResult();
                results.put(startActivity, resultForDay);
                for (Path<Place> path : bestPathList) {
                    //log.debug("Try find with path:" + path.toString(NameBean::simple) + " price:" + bestPaths.getScore(path));
                    PriceWithDiscountScore score = bestPaths.getScore(path);
                    //Если мы уже нашли решение лучше, чем path может предложить со скидкой, то смысла смотреть дальше нет
                    //В этом случае все же смотрим, чтобы получить хотя бы 5 решений, при этом точно оптимальным будет только первое
                    if (resultForDay.size() < 5 || score.getPriceWithDiscount() < resultForDay.getMinPriceWithoutStop()) {
                        StopActivityGraph activityWithPriceGraph = new StopActivityGraph(activityPlaceMap, path);
                        //new BFSPathGraphSearch<>(activityWithPriceGraph, activityWithPriceGraphVisitor).search(startActivityWithPrice);
                        new DFSRecursionGraphSearch<>(activityWithPriceGraph, activityWithPriceGraphVisitor).search(new StopActivityVertex(startActivity));
                        activityWithPriceGraphVisitor.addToResult(resultForDay);
                    } else {
                        //log.debug("Best route finded with price:"+resultForDay.getMinPriceWithoutStop());
                    }
                }
            }
        }));
        //Пока что выбираем случайный результат
        RouteSearchResult result = results.values().iterator().next();
        return result;
    }


}
