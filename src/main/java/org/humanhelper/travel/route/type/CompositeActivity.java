package org.humanhelper.travel.route.type;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.Activity;

import java.util.*;

/**
 * список перемещений или местонахождений
 *
 * @author Андрей
 * @since 08.05.14
 */
public class CompositeActivity {

    private float price;
    private List<Activity> routeItems;//Маршрут

    public CompositeActivity() {
        this.price = 0;
        this.routeItems = new ArrayList<>();
    }


    public CompositeActivity(List<Activity> activities, float price) {
        this.price = price;
        this.routeItems = activities;
    }

    public void addActivity(Activity activity) {
        routeItems.add(activity);
    }

    public void addActivity(Activity activity, float price) {
        addActivity(activity);
        this.price += price;
    }

    public void addActivities(Collection<Activity> activities) {
        for (Activity activity : activities) {
            addActivity(activity);
        }
    }

    public List<Activity> getRouteItems() {
        return routeItems;
    }

    /**
     * Для передачи данных о местах на клиент в виде отдельного метода
     */
    public Map<String, Place> getPlaces() {
        Map<String, Place> places = new HashMap<>();
        for (Activity route : routeItems) {
            places.put(route.getSource().getId(), route.getSource());
            if (route instanceof Route) {
                Place target = ((Route) route).getTarget();
                places.put(target.getId(), target);
            }
        }
        return places;
    }

    public float getPrice() {
        return price;
    }

    public void incPrice(float price) {
        this.price += price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompositeActivity)) return false;

        CompositeActivity that = (CompositeActivity) o;

        if (!routeItems.equals(that.routeItems)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return routeItems.hashCode();
    }

    @Override
    public String toString() {
        return "Route{ " + routeItems + '}';
    }

    public String getQuickInfo() {
        List<String> places = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        float price = 0f;
        for (Activity item : getRouteItems()) {
            if (item instanceof StayInPlaceActivity) {
                StayInPlaceActivity placeRouteItem = (StayInPlaceActivity) item;
                places.add(placeRouteItem.getSource().nameOrId() + (item instanceof StayInPlaceForTransitActivity ? "(T)" : ""));
            }
            if (item instanceof FixedTimeRoute) {
                FixedTimeRoute fixedTimeWayRouteItem = (FixedTimeRoute) item;
                if (startDate == null || startDate.after(fixedTimeWayRouteItem.getStartTime())) {
                    startDate = fixedTimeWayRouteItem.getStartTime();
                }
                if (endDate == null || endDate.before(fixedTimeWayRouteItem.getEndTime())) {
                    endDate = fixedTimeWayRouteItem.getEndTime();
                }
            }
            price += item.getPriceResolver().getPrice();
        }
        return "---Price with discount:" + getPrice() + /*" Start:" + DateHelper.getDateTime(startDate) +*/
                " Price without discount:" + price +
                (startDate != null && endDate != null ? " Days:" + DateHelper.daysBetweenDates(endDate, DateHelper.setTime(startDate, 0, 0)) : "") +
                " Places:" + places;
    }

}
