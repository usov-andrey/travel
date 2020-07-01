package org.humanhelper.travel.route;

import java.util.ArrayList;

/**
 * Необходим для правильной сериализации в json
 *
 * @author Андрей
 * @since 27.02.15
 */
public class ActivityList extends ArrayList<Activity> {

    public float getPrice() {
        float cost = 0;
        for (Activity item : this) {
            cost += item.getPriceResolver().getPrice();
        }
        return cost;
    }

}
