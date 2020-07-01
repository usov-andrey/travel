package org.humanhelper.travel.route.routesearch.newsearch;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.sourcetarget.SourceTarget;
import org.humanhelper.travel.route.routesearch.graph.SimpleDirectedOutgoingGraph;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScoreEntry;
import org.humanhelper.travel.route.routesearch.query.RouteQuery;

import java.util.*;

/**
 * Для каждого места остановки хранится список мест, куда можно попасть из него
 * отсортированный в порядке увеличения стоимости перемещения
 *
 * @author Андрей
 * @since 29.10.15
 */
public class StopPlaceGraph extends SimpleDirectedOutgoingGraph<Place> {

    public StopPlaceGraph(RouteQuery query, Map<SourceTarget, PriceWithDiscountScore> priceMap) {
        Set<Place> places = query.getPlaces();
        for (Place source : places) {
            List<PriceWithDiscountScoreEntry<Place>> list = new ArrayList<>();
            for (Place target : places) {
                PriceWithDiscountScore score = priceMap.get(new SourceTarget(source, target));
                if (score != null) {
                    list.add(new PriceWithDiscountScoreEntry<>(target, score));
                }
            }
            //Сортируем в порядке увеличения минимальной стоимости
            Collections.sort(list, PriceWithDiscountScoreEntry.getPriceWithDiscountComparator());
            for (PriceWithDiscountScoreEntry<Place> entry : list) {
                addEdge(source, entry.getKey());
            }
        }
    }

    @Override
    protected Set<Place> createTargets() {
        return new LinkedHashSet<>();
    }

}
