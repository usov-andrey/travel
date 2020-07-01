package org.humanhelper.travel.route.routesearch.time;

import org.humanhelper.service.utils.TimerHelper;
import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.price.SimplePriceAgent;
import org.humanhelper.travel.route.resolver.PlaceSetRoutesResolver;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.query.RouteQueryStop;
import org.humanhelper.travel.route.routesearch.road.PlaceRouteNetwork;
import org.humanhelper.travel.route.routesearch.time.search.PathRouteSearchResult;
import org.humanhelper.travel.route.routesearch.time.search.vertex.*;
import org.humanhelper.travel.route.type.StayInPlaceActivity;
import org.humanhelper.travel.route.type.StayInPlaceForTransitActivity;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.waydiscount.DiscountStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Создаем из модели time-expanded граф где вершины - это перемещения или нахождения в месте,
 * а ребра - это возможность использования этих перемещений друг за другом
 *
 * @author Андрей
 * @since 05.12.14
 */
public class ExpandedGraphCreator {
    private static final Logger log = LoggerFactory.getLogger(ExpandedGraphCreator.class);

    private static final int MAX_WAIT_TIME_IN_MS = 1000 * 60 * 6;//6 часов
    private static final long MS_IN_ONE_DAY = (1000 * 60 * 60 * 24); // 24 hours in milliseconds

    private RouteQuery query;
    private Graph graph;
    private Vertex startVertex;
    private PlaceSetRoutesResolver routeProvider;
    private Map<Vertex, Vertex> vertexMap = new HashMap<>();

    /**
     * @return Наполняет граф возвращая стартовую вершину для обхода
     */
    public Vertex fillGraph(RouteQuery query, Graph graph, PlaceRouteNetwork places, PathRouteSearchResult searchResult) {
        this.query = query;
        this.graph = graph;
        this.routeProvider = new PlaceSetRoutesResolver();
        log.info("Start graph creating");
        log.info("Graph created in ms:" + TimerHelper.run(
                () -> {
                    //Получение маршрутов может быть асинхронно, поэтому обьединяем это в единую фазу по времени
                    routeProvider.fillRoutes(query, places);
                    fillGraph(places.getAllPlaces(), searchResult);
                }));
        return startVertex;
    }

    private void fillGraph(Set<Place> places, PathRouteSearchResult searchResult) {
        for (Place place : places) {
            if (place.equals(query.getStart().getPlace())) {
                createStartPlace(place);
                if (place.equals(query.getEnd().getPlace())) {
                    createEndPlace(place, searchResult);
                }
            } else if (place.equals(query.getEnd().getPlace())) {
                createEndPlace(place, searchResult);
            } else {
                processPlace(place);
            }
        }
    }

    protected void createStartPlace(Place place) {
        startVertex = createVertex(new StartPlaceVertex());
        Queue<Vertex<? extends TimeRoute>> outgoingQueue = getOutgoingQueue(place);
        for (Vertex wayVertex : outgoingQueue) {
            addEdge(startVertex, wayVertex);
        }
    }

    protected void createEndPlace(Place place, PathRouteSearchResult searchResult) {
        EndVertex endVertex = createVertex(new EndVertex(searchResult));
        List<Vertex<? extends TimeRoute>> incomingWays = getSortedIncomingList(place);
        for (Vertex<? extends TimeRoute> wayVertex : incomingWays) {
            addEdge(wayVertex, endVertex);
        }
    }

    protected void processPlace(Place place) {
        List<Vertex<? extends TimeRoute>> incomingWays = getSortedIncomingList(place);
        Queue<Vertex<? extends TimeRoute>> outgoingQueue = getOutgoingQueue(place);

        for (Vertex<? extends TimeRoute> incoming : incomingWays) {
            //Каждую вершину incoming нужно соединить с вершиной outgoing в случае если между ними время ожидания не больше 6 часов
            //Если можно останавливаться в placeId, то нужно создать несколько вершин остановок(мин количество ночей-максимальное количество ночей)
            //соединить их с incoming и соединить их с нужными outgoing, когда можно переместиться в день checkout в любое время
            TimeRoute incomingWay = incoming.getRouteItem();

            //Оптимизация вычислений, чтобы не использовать медленный стандартный Calendar
            Date startDate = incomingWay.getEndTime();
            long startTime = startDate.getTime();//Время после которого можно отправляться
            long startTimeDays = startTime / MS_IN_ONE_DAY;
            long startTimeDay = startTimeDays * MS_IN_ONE_DAY;//День прилета время 00:00;
            Date startTimeDate = new Date(startTimeDay);

            Collection<StopPlaceVertex> stops = createStops(place, startDate, startTimeDate);

            int maxNights = 1;
            //Соединяем вершины остановок с вершиной входящего маршрута
            for (StopPlaceVertex stop : stops) {
                addEdge(incoming, stop);
                maxNights = stop.getRouteItem().getNights();
            }

            //Добавляем возможную транзитную остановку
            TransitVertex transit = createTransit(place, startTimeDate);
            addEdge(incoming, transit);

            //Нужно соединить входящий маршрут со всевозможными исходящими
            for (Vertex<? extends TimeRoute> outgoing : outgoingQueue) {

                TimeRoute outgoingWay = outgoing.getRouteItem();
                long outgoingStartTime = outgoingWay.getSource().getLastArriveTime(outgoingWay.getStartTime()).getTime();

                if (outgoingStartTime < startTime) {
                    //Удаляем outgoing из очереди, больше для него нет кандидатов из incoming
                    outgoingQueue.remove();
                    //System.out.println("Remove:"+outgoingWay);
                    continue;
                } else if (outgoingStartTime > startTimeDay + (maxNights + 1) * MS_IN_ONE_DAY) {
                    //Этот outgoing слишком поздный для этого incoming, дальнейшие outgoing рассматривать для этого incoming смысла нет
                    break;
                }

                //Обрабатываем остановки в начале, так как нам лучше переместиться в остановку, нежели перемещаться дальше
                for (StopPlaceVertex stop : stops) {
                    //Если между incomingWay и outgoingWay можно сделать остановку на stop.nights, то добавляем ребро
                    if (canNextRouteItemAfterStay(startTimeDay, outgoingStartTime, stop.getRouteItem().getNights())) {
                        addEdge(stop, outgoing);
                    }
                }

                //Если транзита нет, то это переход из AnyTimeWay
				/*
				if ((outgoingWay instanceof FixedTimeWayFromAnyWayRoute)) {
					if (startTime == outgoingStartTime) {
						addEdge(incoming, outgoing);
					}
				} else { */
                //Максимальное время ожидания 6 часов либо до 00:00 следующего дня
                if (canNextRouteItem(startTimeDay, startTimeDays, startTime, outgoingStartTime)
                    //&& !incomingWay.getSourceId().equals(outgoingWay.getSourceId()) //Лететь назад сразу нет смысла, но по скорости эта проверка не нужна
                ) {
                    addEdge(incoming, outgoing);
                }

                //Добавляем перемещение в транзитное место
                if (canNextRouteItemAfterStay(startTimeDay, outgoingStartTime, 1)) {
                    addEdge(transit, outgoing);
                }
                //}
            }
        }
    }

    /**
     * Ожидать можно либо MAX_WAIT_TIME минут либо до 00:00 следующего дня(если прилетаем после 6 утра-типо можно выйти в город)
     */
    protected boolean canNextRouteItem(long startTimeDay, long startTimeDays, long startTime, long outgoingStartTime) {
        long startTimeWithWait = startTime + MAX_WAIT_TIME_IN_MS;
        //Если startTimeWithWait - текущий день, то нужно использовать начало следующего дня после startTime
        long startTimeWithWaitDays = startTimeWithWait / MS_IN_ONE_DAY;                                   //6 часов
        long endTime = ((startTimeDays == startTimeWithWaitDays) && (startTime > startTimeDay + MAX_WAIT_TIME_IN_MS)) ? startTimeDay + MS_IN_ONE_DAY : startTimeWithWait;
        return startTime <= outgoingStartTime && outgoingStartTime <= endTime;
    }

    private Queue<Vertex<? extends TimeRoute>> getOutgoingQueue(final Place place) {
        Queue<Vertex<? extends TimeRoute>> outgoingQueue = new LinkedBlockingQueue<>();
        List<TimeRoute> ways = routeProvider.routes(place).getSortedOutgoing(place);
        if (ways.isEmpty()) {
            log.warn("Not found outgoing routes from " + place.nameOrId() + " in query period:" + query.getStart().getPeriod().getStart() + " - " + query.getEnd().getPeriod().getEnd());
            return outgoingQueue;
        }

        fillVertexCollection(ways, outgoingQueue);
        return outgoingQueue;
    }


    private List<Vertex<? extends TimeRoute>> getSortedIncomingList(final Place place) {
        List<Vertex<? extends TimeRoute>> incomingList = new ArrayList<>();
        List<TimeRoute> ways = routeProvider.routes(place).getSortedIncoming(place);
        if (ways.isEmpty()) {
            log.warn("Not found incoming routes to " + place.nameOrId() + " in query period:" + query.getStart().getPeriod().getStart() + " - " + query.getEnd().getPeriod().getEnd());
            return incomingList;
        }

        fillVertexCollection(ways, incomingList);
        return incomingList;
    }

    private void fillVertexCollection(List<TimeRoute> ways, Collection<Vertex<? extends TimeRoute>> vertexList) {
        for (TimeRoute way : ways) {
            vertexList.add(createVertex(new WayVertex(way)));
            DiscountStrategy discountStrategy = way.getDiscountStrategy();
            if (discountStrategy != null) {
                //Для каждого перелета мы создаем в зависимости от типа скидки для transport две новые вершины в графе.
                //Вершина начало скидки и вершина конец скидки
                //Текущий путь может быть как концом скидки, так и началом
                vertexList.add(createVertex(new StartDiscountWayVertex(way, discountStrategy)));
                vertexList.add(createVertex(new EndDiscountWayVertex(way, discountStrategy)));
            }
        }
    }


    /**
     * Если мы проводим в отеле ночи, то ожидать можно до 00:00 следующего дня после проведенного в отеле
     */
    protected boolean canNextRouteItemAfterStay(long startTimeDay, long outgoingStartTime, int stayNights) {
        startTimeDay = startTimeDay + stayNights * MS_IN_ONE_DAY;
        long endTimeDay = startTimeDay + MS_IN_ONE_DAY;
        return startTimeDay <= outgoingStartTime && outgoingStartTime <= endTimeDay;
    }

    @NotNull
    protected Collection<StopPlaceVertex> createStops(Place place, Date arriveTime, Date arriveDay) {
        RouteQueryStop stop = query.getStop(place);
        if (stop != null && stop.canArrive(arriveTime)) {
            Collection<StopPlaceVertex> stops = new ArrayList<>();
            //Можно уcкорить, если не создавать остановки для каждого отправления, а создавать их только для каждого дня
            for (int i = stop.getNights().getStart(); i <= stop.getNights().getEnd(); i++) {
                StopPlaceVertex stopPlaceVertex = createVertex(new StopPlaceVertex(stop, createPlaceRouteItem(place, arriveDay, i)));
                stops.add(stopPlaceVertex);
            }
            return stops;
        } else {
            return Collections.emptyList();
        }
    }

    protected TransitVertex createTransit(Place place, Date day) {
        //Возможно стоит остановиться в этом месте для транзита на одну ночь
        return createVertex(new TransitVertex(createTransitRouteItem(place, day)));
    }

    protected StayInPlaceActivity createPlaceRouteItem(Place place, Date arriveDate, int nights) {
        //TODO
        //PriceResolver priceResolver = new PriceResolver(30f * 13500 * nights, CountryBuilder.ID.getCurrency(), SimplePriceAgent.INSTANCE);
        //place.getStayProvider().getPriceResolver(arriveDate, nights);
        PriceResolver priceResolver = new PriceResolver(1000f * nights, CountryBuilder.US.getCurrency(), SimplePriceAgent.INSTANCE);
        return new StayInPlaceActivity(place, priceResolver, arriveDate, nights);
    }

    protected StayInPlaceActivity createTransitRouteItem(Place place, Date arriveDate) {
        //TODO
        //PriceResolver priceResolver = new PriceResolver(30f * 13500, CountryBuilder.ID.getCurrency(), SimplePriceAgent.INSTANCE);
        //place.getStayProvider().getPriceResolver(arriveDate, nights);
        PriceResolver priceResolver = new PriceResolver(1000f, CountryBuilder.US.getCurrency(), SimplePriceAgent.INSTANCE);
        return new StayInPlaceForTransitActivity(place, priceResolver, arriveDate);
    }

    public <T extends Vertex> T createVertex(T vertex) {
        //Вершины нужно создавать в единичном экземпляре, чтобы для двух разных source не создавались два разных target, означающие тоже самое
        T oldVertex = (T) vertexMap.get(vertex);
        if (oldVertex == null) {
            vertexMap.put(vertex, vertex);
            graph.addVertex(vertex);
            oldVertex = vertex;
        }
        return oldVertex;
    }

    protected void addEdge(Vertex source, Vertex target) {
        //System.out.println("Add edge:"+source+ " to "+target);
        graph.addEdge(source, target);
    }

}
