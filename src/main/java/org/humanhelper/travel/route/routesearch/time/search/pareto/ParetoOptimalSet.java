package org.humanhelper.travel.route.routesearch.time.search.pareto;

import org.humanhelper.travel.route.type.TimeRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Андрей
 * @since 04.12.14
 */
public class ParetoOptimalSet {

    private List<ParetoOptimalEntry> entryList;

    public ParetoOptimalSet(float cost, TimeRoute wayRouteItem) {
        entryList = new ArrayList<>();
        entryList.add(new ParetoOptimalEntry(wayRouteItem, wayRouteItem.getEndTime().getTime(), cost));
    }


    public boolean canComeToPlace(float cost, TimeRoute wayRouteItem) {
        long time = wayRouteItem.getEndTime().getTime();

        for (ParetoOptimalEntry entry : entryList) {
            if (entry.equals(time, cost)) {
                return true;
            }
            if (entry.dominate(time, cost)) {
                return false;
            }
        }
        entryList.add(new ParetoOptimalEntry(wayRouteItem, time, cost));
        return true;
    }

}
