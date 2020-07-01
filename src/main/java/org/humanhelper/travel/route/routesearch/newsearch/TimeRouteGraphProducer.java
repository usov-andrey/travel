package org.humanhelper.travel.route.routesearch.newsearch;

import org.humanhelper.service.utils.TimerHelper;
import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.price.SimplePriceAgent;
import org.humanhelper.travel.route.resolver.PlaceSetRoutesResolver;
import org.humanhelper.travel.route.routesearch.graph.SimpleDirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;
import org.humanhelper.travel.route.routesearch.query.RouteQueryStop;
import org.humanhelper.travel.route.routesearch.road.PlaceRouteNetwork;
import org.humanhelper.travel.route.type.StayInPlaceActivity;
import org.humanhelper.travel.route.type.StayInPlaceForTransitActivity;
import org.humanhelper.travel.route.type.TimeActivity;
import org.humanhelper.travel.route.type.TimeRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Создаем обратный граф TimeActivity
 * <p>
 * Со следующими ограничениями
 * 1. Избегаем циклов: Если мы находимся в каком-то месте и при этом в текущем маршруте мы уже были в этом месте
 * и между этими посещениями нет места для остановки, то дальше не идем(вошли в цикл)
 * Если мы приходим в место, где уже были, то останавливаемся, если между предыдущим посещением не было места для остановки
 * <p>
 * 2. Между остановками недопускаем больше чем одной транзитной остановки(увеличивает скорость в 2.5 раза)
 * 3. Если мы пришли в какую-то остановку и ранее при обходе мы уже были здесь в этот же день и с таким же маршрутом остановок(транзиты не важны)
 * и если раньше стоимость была меньше, то дальше не идем(увеличивает скорость в 10 раз)
 * 4. Избегаем после остановки повторения маршрута только в другой последовательности:
 * Храним в маршруте хэш места и для каждого минимальное время + стоимость. Если приходим в место по времени не раньше
 * и дороже, то дальше не идем, иначе добавляем новое значение в этот Pareto Set.
 * 5. Избегаем повторения маршрута только в другое время
 *
 * @author Андрей
 * @since 20.10.15
 */
public class TimeRouteGraphProducer {

    private static final Logger log = LoggerFactory.getLogger(TimeRouteGraphProducer.class);

    private static final int MAX_WAIT_TIME_IN_MS = 1000 * 60 * 6;//6 часов
    private static final long MS_IN_ONE_DAY = (1000 * 60 * 60 * 24); // 24 hours in milliseconds

    private PlaceSetRoutesResolver routeProvider;
    private RouteQuery query;
    private SimpleDirectedOutgoingGraph<TimeActivity> graph;
    private Set<StayInPlaceActivity> stopVertexSet = new LinkedHashSet<>();

    private List<StartActivity> startActivities;
    private EndActivity endActivity;
    private long lastQueryDayInMS;

    public static long getDay(Date time) {
        return time.getTime() / MS_IN_ONE_DAY;
    }

    /**
     * @return Наполняет граф возвращая стартовую вершину для обхода
     */
    public TimeRouteGraphResult fillGraph(RouteQuery query, PlaceRouteNetwork places) {
        this.query = query;
        this.graph = new SimpleDirectedOutgoingGraph<>();
        this.routeProvider = new PlaceSetRoutesResolver();
        lastQueryDayInMS = getDay(query.getEnd().getPeriod().getEnd());
        log.info("Receiving routes");
        log.info("Route received in ms:" + TimerHelper.run(() ->
                routeProvider.fillRoutes(query, places)));
        log.info("New Graph created in ms:" + TimerHelper.run(() ->
                fillGraph(places.getAllPlaces())));
        log.debug("Graph:" + graph);
        log.debug("Stops:" + stopVertexSet.size());
        return new TimeRouteGraphResult(graph, stopVertexSet, startActivities, endActivity);
    }

    private void addEdge(TimeActivity source, TimeActivity target) {
        graph.addEdge(source, target);
    }

    private void fillGraph(Set<Place> places) {
        for (Place place : places) {
            if (place.equals(query.getStart().getPlace())) {
                createStartPlace(place);
                if (place.equals(query.getEnd().getPlace())) {
                    createEndPlace(place);
                }
            } else if (place.equals(query.getEnd().getPlace())) {
                createEndPlace(place);
            } else {
                processPlace(place);
            }
        }
    }

    protected void createStartPlace(Place place) {
        startActivities = new ArrayList<>();
        Queue<TimeRoute> outgoingQueue = getOutgoingQueue(place);
        for (Date day : query.getStart().getPeriod().dayIterator()) {
            long startTimeDay = day.getTime();
            StartActivity startActivity = new StartActivity(place, day);
            startActivities.add(startActivity);
            for (TimeRoute outgoingVertex : outgoingQueue) {
                long outgoingStartTime = outgoingVertex.getStartTime().getTime();
                //incomingWay приходит упорядоченным по времени, удаляем все outgoing из очереди, которые раньше чем текущий incomingWay
                if (outgoingStartTime < startTimeDay) {
                    //Удаляем outgoing из очереди, больше для него нет кандидатов из incoming
                    outgoingQueue.remove();
                    //System.out.println("Remove:"+outgoingWay);
                    continue;
                } else if (outgoingStartTime > startTimeDay + MS_IN_ONE_DAY) {
                    //Этот outgoing слишком поздный для этого incoming, дальнейшие outgoing рассматривать для этого incoming смысла нет
                    break;
                }
                addEdge(startActivity, outgoingVertex);
            }
        }


    }

    protected void createEndPlace(Place place) {
        endActivity = new EndActivity(place, query.getEnd().getPeriod().getEnd());
        List<TimeRoute> incomingWays = getSortedIncomingList(place);
        for (TimeRoute wayVertex : incomingWays) {
            addEdge(wayVertex, endActivity);
        }
    }

    protected void processPlace(Place place) {
        List<TimeRoute> incomingWays = getSortedIncomingList(place);
        Queue<TimeRoute> outgoingQueue = getOutgoingQueue(place);
        for (TimeRoute incoming : incomingWays) {
            processPlace(place, incoming, outgoingQueue);
        }
    }

    private void processPlace(Place place, TimeRoute incomingWay, Queue<TimeRoute> outgoingQueue) {
        //Оптимизация вычислений, чтобы не использовать медленный стандартный Calendar
        Date startDate = incomingWay.getEndTime();
        long startTime = startDate.getTime();//Время после которого можно отправляться
        long startTimeDays = startTime / MS_IN_ONE_DAY;
        long startTimeDay = startTimeDays * MS_IN_ONE_DAY;//День прилета время 00:00;
        Date startTimeDate = new Date(startTimeDay);

        Collection<StayInPlaceActivity> stops = createStops(place, startDate, startTimeDate);

        int maxNights = 1;
        //Соединяем вершины остановок с вершиной входящего маршрута(incomingWay)
        for (StayInPlaceActivity stop : stops) {
            //Для остановки предыдущей вершиной будет incomingWay
            addEdge(incomingWay, stop);
            stopVertexSet.add(stop);
            //самая последняя остановка создается самая длинная, поэтому проверки здесь не нужно
            maxNights = stop.getNights();
        }

        //Добавляем возможную транзитную остановку
        StayInPlaceForTransitActivity transit = createTransitRouteItem(place, startTimeDate);
        addEdge(incomingWay, transit);

        //Нужно соединить входящий маршрут со всевозможными исходящими
        for (TimeRoute outgoingWay : outgoingQueue) {

            long outgoingStartTime = outgoingWay.getSource().getLastArriveTime(outgoingWay.getStartTime()).getTime();
            //incomingWay приходит упорядоченным по времени, удаляем все outgoing из очереди, которые раньше чем текущий incomingWay
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
            for (StayInPlaceActivity stop : stops) {
                //Если между incomingWay и outgoingWay можно сделать остановку на stop.nights, то добавляем ребро
                if (canNextRouteItemAfterStay(startTimeDay, outgoingStartTime, stop.getNights())) {
                    //Для outgoingWay добавляем, что предыдущая активность - это остановка
                    addEdge(stop, outgoingWay);
                }
            }

            //Максимальное время ожидания 6 часов либо до 00:00 следующего дня
            if (canNextRouteItem(startTimeDay, startTimeDays, startTime, outgoingStartTime)
                //&& !incomingWay.getSourceId().equals(outgoingWay.getSourceId()) //Лететь назад сразу нет смысла, но по скорости эта проверка не нужна
            ) {
                addEdge(incomingWay, outgoingWay);
            }

            //Добавляем перемещение в транзитное место
            if (canNextRouteItemAfterStay(startTimeDay, outgoingStartTime, 1)) {
                addEdge(transit, outgoingWay);
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

    /**
     * Если мы проводим в отеле ночи, то ожидать можно до 00:00 следующего дня после проведенного в отеле
     */
    protected boolean canNextRouteItemAfterStay(long startTimeDay, long outgoingStartTime, int stayNights) {
        startTimeDay = startTimeDay + stayNights * MS_IN_ONE_DAY;
        long endTimeDay = startTimeDay + MS_IN_ONE_DAY;
        return startTimeDay <= outgoingStartTime && outgoingStartTime <= endTimeDay;
    }

    @NotNull
    protected Collection<StayInPlaceActivity> createStops(Place place, Date arriveTime, Date arriveDay) {
        RouteQueryStop stop = query.getStop(place);
        if (stop != null && stop.canArrive(arriveTime)) {
            Collection<StayInPlaceActivity> stops = new ArrayList<>();
            //Можно уcкорить, если не создавать остановки для каждого отправления, а создавать их только для каждого дня
            for (int i = stop.getNights().getStart(); i <= stop.getNights().getEnd(); i++) {
                StayInPlaceActivity stayInPlaceActivity = createPlaceRouteItem(place, arriveDay, i);
                if (getDay(stayInPlaceActivity.getEndTime()) <= lastQueryDayInMS) {
                    stops.add(stayInPlaceActivity);
                }
            }
            return stops;
        } else {
            return Collections.emptyList();
        }
    }

    protected StayInPlaceActivity createPlaceRouteItem(Place place, Date arriveDate, int nights) {
        //TODO
        //PriceResolver priceResolver = new PriceResolver(30f * 13500 * nights, CountryBuilder.ID.getCurrency(), SimplePriceAgent.INSTANCE);
        //place.getStayProvider().getPriceResolver(arriveDate, nights);
        PriceResolver priceResolver = new PriceResolver(1000f * nights, CountryBuilder.US.getCurrency(), SimplePriceAgent.INSTANCE);
        return new StayInPlaceActivity(place, priceResolver, arriveDate, nights);
    }

    protected StayInPlaceForTransitActivity createTransitRouteItem(Place place, Date arriveDate) {
        //TODO
        //PriceResolver priceResolver = new PriceResolver(30f * 13500, CountryBuilder.ID.getCurrency(), SimplePriceAgent.INSTANCE);
        //place.getStayProvider().getPriceResolver(arriveDate, nights);
        PriceResolver priceResolver = new PriceResolver(1000f, CountryBuilder.US.getCurrency(), SimplePriceAgent.INSTANCE);
        return new StayInPlaceForTransitActivity(place, priceResolver, arriveDate);
    }

    private Queue<TimeRoute> getOutgoingQueue(final Place place) {
        Queue<TimeRoute> outgoingQueue = new LinkedBlockingQueue<>();
        List<TimeRoute> ways = routeProvider.routes(place).getSortedOutgoing(place);
        if (ways.isEmpty()) {
            log.warn("Not found outgoing routes from " + place.nameOrId() + " in query period:" + query.getStart().getPeriod().getStart() + " - " + query.getEnd().getPeriod().getEnd());
            return outgoingQueue;
        }
        outgoingQueue.addAll(ways);
        return outgoingQueue;
    }

    private List<TimeRoute> getSortedIncomingList(final Place place) {
        List<TimeRoute> ways = routeProvider.routes(place).getSortedIncoming(place);
        if (ways.isEmpty()) {
            log.warn("Not found incoming routes to " + place.nameOrId() + " in query period:" + query.getStart().getPeriod().getStart() + " - " + query.getEnd().getPeriod().getEnd());
        }
        return ways;
    }
}
