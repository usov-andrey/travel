package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.humanhelper.travel.route.Activity;

/**
 * @author Андрей
 * @since 05.11.15
 */
public class ActivityWithDiscountPrice {

    private Activity activity;
    private float price;

    public ActivityWithDiscountPrice(Activity activity, float price) {
        this.activity = activity;
        this.price = price;
    }

    public Activity getActivity() {
        return activity;
    }

    public float getPrice() {
        return price;
    }
}
