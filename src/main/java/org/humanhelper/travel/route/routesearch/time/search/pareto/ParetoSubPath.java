package org.humanhelper.travel.route.routesearch.time.search.pareto;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.routesearch.time.FixedTimeWayFromAnyWayRoute;
import org.humanhelper.travel.route.routesearch.time.search.SubPath;
import org.humanhelper.travel.route.type.TimeRoute;

import java.util.HashMap;
import java.util.Map;

/**
 * Если нам нужно переместиться в какое-то место после последней остановки
 * то возможно мы уже находили путь к этому месту от остановки и этот путь был и дешевле и раньше,
 * тогда не стоит смысла далее идти по этому перемещению
 *
 * @author Андрей
 * @since 05.12.14
 */
public class ParetoSubPath extends SubPath {


    private Map<Place, ParetoOptimalSet> scoreForPlacesAfterStop = new HashMap<>();

    @Override
    public boolean canUseThisWay(TimeRoute way) {
        if (super.canUseThisWay(way)) {

            //AnyTime переходы делаем всегда, не взирая на время
            if (way instanceof FixedTimeWayFromAnyWayRoute) {
                return true;
            }
            Place target = way.getTarget();
            ParetoOptimalSet scoreForPlace = scoreForPlacesAfterStop.get(target);
            float cost = getPrice() + way.getPriceResolver().getPrice();

            if (scoreForPlace == null) {
                scoreForPlacesAfterStop.put(target, new ParetoOptimalSet(cost, way));
                return true;
            }
            return scoreForPlace.canComeToPlace(cost, way);
        } else {
            return false;
        }

    }

}
