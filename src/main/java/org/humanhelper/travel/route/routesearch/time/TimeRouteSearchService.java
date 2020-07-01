package org.humanhelper.travel.route.routesearch.time;

import org.humanhelper.service.utils.TimerHelper;
import org.humanhelper.travel.route.routesearch.RouteSearchResult;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.road.PlaceRouteNetwork;
import org.humanhelper.travel.route.routesearch.time.graph.SimpleGraph;
import org.humanhelper.travel.route.routesearch.time.search.BestPriceRouteSearchResult;
import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.PathRouteSearchResult;
import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitorFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Важное условия работы алгоритма:
 * Из стартового места можно добраться только с помощью рейсового маршрута!!!
 * <p>
 * 1. Вначале формируем time expanded граф: вершина - это время провождение(или перемешение или остановка в месте)
 * ребро - это связь между возможными времяпровождениями
 * Добавляем вначале граф из маршрутов по расписанию
 * Для каждого места получаем список входящих и исходящих маршрутов, если между ними возможна пересадка(не более 6 часов),
 * то добавляем связь. Если в месте возможны остановки, то для каждого дня для входящих маршрутов создаем остановки и
 * добавляем связь с возможными исходящими
 * Для маршрутов
 * <p>
 * 2. Обходим полностью граф и формируем результаты
 * Обход производим с помощью поиска в глубину
 * Ограничения(класс Route):
 * 1. Избегаем циклов: Если мы находимся в каком-то месте и при этом в текущем маршруте мы уже были в этом месте
 * и между этими посещениями нет места для остановки, то дальше не идем(вошли в цикл)
 * 2. Между остановками недопускаем больше чем одной транзитной остановки(увеличивает скорость в 2.5 раза)
 * 3. Если мы пришли в какую-то остановку и ранее при обходе мы уже были здесь в этот же день и с таким же маршрутом остановок(транзиты не важны)
 * и если раньше стоимость была меньше, то дальше не идем(увеличивает скорость в 10 раз)
 * 4. Избегаем после остановки повторения маршрута только в другой последовательности:
 * Храним в маршруте хэш места и для каждого минимальное время + стоимость. Если приходим в место по времени не раньше
 * и дороже, то дальше не идем, иначе добавляем новое значение в этот Pareto Set.
 * 5. Избегаем повторения маршрута только в другое время
 * <p>
 * Возможно стоит применить алгоритм отсюда: http://libtreasures.utdallas.edu/xmlui/bitstream/handle/10735.1/2637/ECS-TR-EE-Vardhan-310316.85.pdf?sequence=7
 * Возможно стоит поискать: http://link.springer.com/article/10.1007/BF01939836
 * Много ссылок здесь:https://lirias.kuleuven.be/bitstream/123456789/397303/1/rsugg2_0_short.pdf
 * A Personal Tourism Navigation System to Support Traveling Multiple
 * Destinations with Time Restrictions
 *
 * @author Андрей
 * @since 28.11.14
 */
public class TimeRouteSearchService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TimeRouteSearchService.class);

    @Autowired
    private GraphVisitorFactory graphVisitorFactory;

    private RouteQuery query;
    private Graph graph;
    private Vertex startPlaceRouteItem;

    public RouteSearchResult getResultRouteList(RouteQuery query, PlaceRouteNetwork places) {
        this.query = query;
        PathRouteSearchResult searchResult = new BestPriceRouteSearchResult();
        createGraph(places, searchResult);
        search();

        return searchResult;
    }

    private void createGraph(PlaceRouteNetwork places, PathRouteSearchResult searchResult) {
        graph = new SimpleGraph();
        ExpandedGraphCreator graphCreator = new ExpandedGraphCreator();
        startPlaceRouteItem = graphCreator.fillGraph(query, graph, places, searchResult);
        log.info("Graph optimized in ms:" + TimerHelper.run(graph::optimize));
        log.info("Graph:" + graph);
    }

    private void search() {
        Path path = new Path(query);
        GraphVisitor visitor = graphVisitorFactory.apply(graph);
        log.info("Starting visit the graph");
        log.info("Graph visited in ms:" + TimerHelper.run(() -> startPlaceRouteItem.visit(visitor, path)));
        visitor.printStatistics();
    }

}
