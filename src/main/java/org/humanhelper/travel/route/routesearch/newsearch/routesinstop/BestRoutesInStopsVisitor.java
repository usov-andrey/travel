package org.humanhelper.travel.route.routesearch.newsearch.routesinstop;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.sourcetarget.SourceTarget;
import org.humanhelper.travel.route.routesearch.graph.search.GraphPathVisitor;
import org.humanhelper.travel.route.routesearch.newsearch.ActivityVisitor;
import org.humanhelper.travel.route.routesearch.newsearch.EndActivity;
import org.humanhelper.travel.route.routesearch.newsearch.StartActivity;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceMap;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.StopActivityMap;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop.SingleRouteToStopActivity;
import org.humanhelper.travel.route.type.*;

import java.util.Map;

/**
 * Ищем оптимальные пути между всеми остановками
 *
 * @author Андрей
 * @since 03.11.15
 */
public class BestRoutesInStopsVisitor implements GraphPathVisitor<TimeActivity, RoutePath>, ActivityVisitor {


    protected RoutePath routePath;
    private StopActivity start;
    private StopActivityMap activityPlaceMap;
    //Ограничения при обходе
    //Как результат этого обхода нам необходимо посчитать минимальную оценку перемещения между всеми местами остановки
    private Map<SourceTarget, PriceWithDiscountScore> stopsScoreMap;
    private PriceMap<Place> priceMap = new PriceMap<>();
    private int realCount = 0;


    public BestRoutesInStopsVisitor(StopActivityMap activityPlaceMap, Map<SourceTarget, PriceWithDiscountScore> stopsScoreMap, StopActivity start) {
        this.activityPlaceMap = activityPlaceMap;
        this.stopsScoreMap = stopsScoreMap;
        this.start = start;
    }

    @Override
    public RoutePath visit(TimeActivity vertex, RoutePath path) {
        routePath = path;
        vertex.visit(this);
        return routePath;
    }

    @Override
    public RoutePath start(TimeActivity vertex) {
        return RoutePath.start(vertex);
    }

    /**
     * Пришли в остановку, необходимо сохранить результат
     */
    private void visitLastVertex(StopActivity vertex) {
        SourceTarget sourceTarget = new SourceTarget(start.getSource(), vertex.getSource());
        if (!sourceTarget.isSourceEqualsTarget()) {
            //Создаем новый stopScore, так как текущий score меняется
            PriceWithDiscountScore stopScore = new PriceWithDiscountScore(routePath.score);
            SingleRouteToStopActivity routeToStopActivity = new SingleRouteToStopActivity(stopScore, routePath);
			/*
			if (start.getSource().getName().equals("Manila") && vertex.getSource().getName().equals("Moscow")
					&& path.getValue().getSource().getName().equals("Kualumpur")) {
				log.debug(path.toString());
			} */

            if (activityPlaceMap.add(start, vertex, routeToStopActivity)) {
                //Так как в пути находится вершина start, то исключаем ее стоимость, для этого передаем, что обрабатывать нужно
                //в пути на один элемент меньше

				/*
				if (start.getSource().getName().equals("Moscow") && vertex.getSource().getName().equals("Seul")) {
					log.debug(path.toString()+" "+vertex);
				} */
                realCount++;
                stopScore.putToMap(stopsScoreMap, sourceTarget);
            }
        }
        routePath = null;
    }

    @Override
    public void anyRoute(AnyTimeRoute vertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void timeRoute(TimeRoute vertex) {
        routePath = routePath.add(vertex);
        Place target = vertex.getTarget();
        //Если мы пришли в какое-то место и ранее мы здесь были, то образовался цикл и идти далее не стои
        if (routePath.visitedPlaces.add(target)) {
            //Если мы ранее уже нашли путь в это место и текущая цена даже со скидкой больше чем найденная ранее цена без скидки
            //то смысла идти дальше нет
            float price = vertex.getPriceResolver().getPrice();
            float priceWithDiscount = vertex.getDiscountResolver() != null ? vertex.getDiscountResolver().getMinPossiblePriceWithDiscount() : price;
            PriceWithDiscountScore score = routePath.score;
            score.addPrice(price, priceWithDiscount);
            float oldPrice = priceMap.getScore(target);
            if (score.getPrice() < oldPrice) {
                //Нашли более оптимальную цену, сохраняем ее
                priceMap.setScore(target, score.getPrice());
                //Идем дальше
            } else if (score.getPriceWithDiscount() <= oldPrice) {
                //Цена со скидкой меньше, идем дальше
                //Идем дальше
            } else {
                routePath = null;
            }
        } else {
            //Дальше не идем
            routePath = null;
        }
    }

    @Override
    public void stop(StayInPlaceActivity vertex) {
        visitLastVertex(vertex);
    }

    @Override
    public void transit(StayInPlaceForTransitActivity vertex) {
        routePath = routePath.add(vertex);
        float price = vertex.getPriceResolver().getPrice();
        float priceWithDiscount = vertex.getDiscountResolver() != null ? vertex.getDiscountResolver().getMinPossiblePriceWithDiscount() : price;
        //Добавляем стоимость и идем дальше
        routePath.score.addPrice(price, priceWithDiscount);
    }

    @Override
    public void start(StartActivity vertex) {
        throw new IllegalStateException();
    }

    @Override
    public void end(EndActivity vertex) {
        visitLastVertex(vertex);
    }

    public int getRealCount() {
        return realCount;
    }
}
