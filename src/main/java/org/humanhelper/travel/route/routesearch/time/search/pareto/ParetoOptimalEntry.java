package org.humanhelper.travel.route.routesearch.time.search.pareto;

import org.humanhelper.travel.route.type.TimeRoute;

/**
 * Лучша оценка: либо приходим раньше по времени, либо дешевле цена
 *
 * @author Андрей
 * @since 04.12.14
 */
public class ParetoOptimalEntry {

    private TimeRoute wayRouteItem;
    private long time;
    private float price;

    public ParetoOptimalEntry(TimeRoute wayRouteItem, long time, float price) {
        this.wayRouteItem = wayRouteItem;
        this.time = time;
        this.price = price;
    }

    public boolean equals(long time, float price) {
        return this.time == time && this.price == price;
    }

    public boolean dominate(long time, float price) {
        return this.time <= time && this.price <= price;
    }

}
