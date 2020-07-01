package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.price.SimplePriceAgent;
import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.discount.DiscountResolver;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;
import org.humanhelper.travel.route.routesearch.newsearch.stopgraph.StopActivityVertex;
import org.humanhelper.travel.route.type.FixedTimeRoute;
import org.humanhelper.travel.route.type.StayInPlaceActivity;
import org.humanhelper.travel.route.type.StopActivity;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Андрей
 * @since 04.11.15
 */
public class BestRouteWithDiscountCalculatorTest {

    @Test
    public void calculate() {


        //Для каждой из этих активностей цена 1000
        FixedTimeRoute activity1 = activity("Activity1", 1000);
        FixedTimeRoute activity2 = activity("Activity2", 1000);
        FixedTimeRoute activity3 = activity("Activity3", 1000);
        FixedTimeRoute activity4 = activity("Activity4", 1000);

        //Activity1
        DiscountResolver discountResolver1 = new DiscountResolver();
        //Если в пути есть activity2, то цена будет 400
        //Если в пути есть activity3, то цена будет 300
        discountResolver1.addDiscountPrice(activity2, 400);
        discountResolver1.addDiscountPrice(activity3, 300);
        activity1.setDiscountResolver(discountResolver1);
        //Activity2
        DiscountResolver discountResolver2 = new DiscountResolver();
        //Если в пути есть activity1, то цена будет 700
        discountResolver2.addDiscountPrice(activity1, 700);
        //Если в пути будет activity4, то цена будет 500
        discountResolver2.addDiscountPrice(activity4, 500);
        activity2.setDiscountResolver(discountResolver2);
        //Activity3
        DiscountResolver discountResolver3 = new DiscountResolver();
        //Если в пути есть activity1, то цена будет 300
        discountResolver3.addDiscountPrice(activity1, 300);
        activity3.setDiscountResolver(discountResolver3);
        //Таким образом лучше выбрать activity1+activity3=300+300 нежели activity1+activity2=400+700


        DiscountResolver discountResolver4 = new DiscountResolver();
        discountResolver4.addDiscountPrice(activity2, 300f);
        activity4.setDiscountResolver(discountResolver4);
        //Если у пути будет выбрана activity2+activity4, то цена будет 500+300 иначе activity2+1000

        FixedTimeRoute noDiscountActivity = activity("NoDiscountActivity", 500);


        //Варианты путей:
        //activity1+NoDiscount, activity2, activity4 - 400(выбрана act2)+500, 500(выбрана act4), 300(выбрана act2)= 900 + 500 + 300=1700
        //activity1+NoDiscount, activity3, activity4 - 300(выбрана act3)+500, 300(выбрана act1), 1000(не выбрана act2) = 800 + 300 + 1000=2100
        //Результат должен быть выбором первого варианта


        StopActivityVertex vertex1 = new StopActivityVertex(stop("Vertex1"), createRoute(activity1, noDiscountActivity));
        StopActivityVertex vertex2 = new StopActivityVertex(stop("Vertex2"), new MultipleRouteToStopActivity(createRoute(activity2), createRoute(activity3)));
        StopActivityVertex vertex4 = new StopActivityVertex(stop("Vertex4"), createRoute(activity4));
        Set<StopActivityVertex> discountVertexSet = new HashSet<>();
        Collections.addAll(discountVertexSet, vertex1, vertex2, vertex4);

        BestRouteWithDiscountCalculator calculator = new BestRouteWithDiscountCalculator();
        calculator.calculate(discountVertexSet);
        for (SingleRouteToStopActivity routeToStopActivity : calculator.getBestRoute().values()) {
            System.out.println(routeToStopActivity);
        }

        assertEquals(400f + 500f + 500f + 300f, calculator.getPriceWithDiscount(), 0f);
    }

    private FixedTimeRoute activity(String name, float price) {
        return new FixedTimeRoute().sourceTarget(Region.build(name), Region.build(name + "-Target")).time(new Date(), new Date()).price(new PriceResolver(price, Currency.getInstance("USD"), SimplePriceAgent.INSTANCE));
    }

    private StopActivity stop(String name) {
        return new StayInPlaceActivity(Region.build(name), null, new Date(), 5);
    }

    private SingleRouteToStopActivity createRoute(Activity... activity) {
        return new SingleRouteToStopActivity(new PriceWithDiscountScore(getPrice(activity), getDiscountPrice(activity)),
                createActivities(activity), createDiscountActivities(activity));
    }

    private float getPrice(Activity... activities) {
        float price = 0;
        for (Activity activity : activities) {
            price += activity.getPriceResolver().getPrice();
        }
        return price;
    }

    private float getDiscountPrice(Activity... activities) {
        float price = 0;
        for (Activity activity : activities) {
            if (activity.getDiscountResolver() != null) {
                price += activity.getDiscountResolver().getMinPossiblePriceWithDiscount();
            } else {
                price += activity.getPriceResolver().getPrice();
            }
        }
        return price;
    }

    private List<Activity> createActivities(Activity... activities) {
        List<Activity> activityList = new ArrayList<>();
        Collections.addAll(activityList, activities);
        return activityList;
    }

    private Set<Activity> createDiscountActivities(Activity... activities) {
        Set<Activity> activityList = new HashSet<>();
        for (Activity activity : activities) {
            if (activity.getDiscountResolver() != null) {
                activityList.add(activity);
            }
        }
        return activityList;
    }


}