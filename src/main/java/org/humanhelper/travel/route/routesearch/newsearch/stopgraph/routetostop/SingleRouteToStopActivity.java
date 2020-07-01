package org.humanhelper.travel.route.routesearch.newsearch.stopgraph.routetostop;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.graph.search.Path;
import org.humanhelper.travel.route.routesearch.newsearch.score.PriceWithDiscountScore;
import org.humanhelper.travel.route.type.TimeActivity;

import java.util.*;

/**
 * @author Андрей
 * @since 04.11.15
 */
public class SingleRouteToStopActivity implements RouteToStopActivity {

    protected PriceWithDiscountScore score;
    private List<Activity> activities;
    private Set<Activity> discountActivities;

    public SingleRouteToStopActivity(Float price, List<Activity> activities) {
        this(new PriceWithDiscountScore(price), activities);
    }

    public SingleRouteToStopActivity(PriceWithDiscountScore score, List<Activity> activities) {
        this.score = score;
        this.activities = activities;
    }

    public SingleRouteToStopActivity(PriceWithDiscountScore score, List<Activity> activities, Set<Activity> discountActivities) {
        this(score, activities);
        this.discountActivities = discountActivities;
    }

    public SingleRouteToStopActivity(PriceWithDiscountScore score, Path<TimeActivity> path) {
        this.score = score;
        fillActivities(path);
    }

    private void fillActivities(Path<TimeActivity> path) {
        activities = new ArrayList<>();
        while (path.getPrevious().notEmpty()) {
            TimeActivity activity = path.getValue();
            if (activity.getDiscountResolver() != null) {
                if (discountActivities == null) {
                    discountActivities = new HashSet<>();
                }
                discountActivities.add(activity);
            }
            activities.add(0, activity);
            path = path.getPrevious();
        }
    }

    @Override
    public RouteToStopActivity join(SingleRouteToStopActivity route) {
        return new MultipleRouteToStopActivity(this, route);
    }

    @Override
    public boolean haveDiscountActivities() {
        return discountActivities != null;
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * @return left - маршрут со скидкой в текущем маршруте, right - маршрут со скидкой в routeInPath
     */
    public Pair<SingleRouteToStopActivity, SingleRouteToStopActivity> getBestRouteWithDiscount(SingleRouteToStopActivity routeInPath) {
        Pair<SingleRouteToStopActivity, SingleRouteToStopActivity> bestRoute = getRouteWithDiscountOrNull(routeInPath);
        if (bestRoute == null) {
            bestRoute = new ImmutablePair<>(this, routeInPath.getBestRouteWithoutDiscount());
        }
        return bestRoute;
    }

    public Pair<SingleRouteToStopActivity, SingleRouteToStopActivity> getRouteWithDiscountOrNull(SingleRouteToStopActivity routeInPath) {
        if (discountActivities == null) {
            return null;
        }

        SingleRouteToStopActivity bestRouteInPath = routeInPath.getBestRouteWithoutDiscount();
        //Слева activity в routeInPath, справа activity в текущме маршруте
        Pair<ActivityWithDiscountPrice, ActivityWithDiscountPrice> bestPrice = null;
        //Необходимо среди discountActivities выбрать лучшую для которой будет минимальная цена
        //со скидкой и для этой цены будет маршрут со стоимостью в routeInPath
        for (Activity discountActivity : discountActivities) {

            Pair<ActivityWithDiscountPrice, Float> priceWithDiscount = routeInPath.getBestPriceWithDiscount(discountActivity);
            //right - цена для discountActivity со скидкой, если будет использовано в routeInPath - left
            if (priceWithDiscount != null && (bestPrice == null || (priceWithDiscount.getRight() < bestPrice.getRight().getPrice()))) {
                bestPrice = new ImmutablePair<>(priceWithDiscount.getLeft(), new ActivityWithDiscountPrice(discountActivity, priceWithDiscount.getRight()));
            }
        }
        if (bestPrice == null) {
            return null;
        }
        float currentPriceWithDiscount = getScore().getPrice() + bestPrice.getRight().getPrice() - bestPrice.getRight().getActivity().getPriceResolver().getPrice();
        float routeInPathPriceWithDiscount = routeInPath.getScore().getPrice() + bestPrice.getLeft().getPrice() - bestPrice.getLeft().getActivity().getPriceResolver().getPrice();
        return new ImmutablePair<>(new SingleRouteToStopActivity(currentPriceWithDiscount, activities),
                new SingleRouteToStopActivity(routeInPathPriceWithDiscount, routeInPath.getActivities()));
    }

    /**
     * @return left - цена со скидкой при использовании текущего маршрута+используемая активность,
     * если где-то будет использована previousDiscountActivity
     * right - цена со скидкой для previsousDiscountActivity, если будет использован текущий маршрут
     */
    public Pair<ActivityWithDiscountPrice, Float> getBestPriceWithDiscount(Activity previousDiscountActivity) {
        if (discountActivities == null) {
            return null;
        }
        //Находим первую попавшуюся скидку
        for (Activity discountActivity : discountActivities) {
            Float priceWithDiscount = discountActivity.getDiscountResolver().getDiscountPrice(previousDiscountActivity);
            if (priceWithDiscount != null) {
                return new ImmutablePair<>(new ActivityWithDiscountPrice(discountActivity, priceWithDiscount), previousDiscountActivity.getDiscountResolver().getDiscountPrice(discountActivity));
            }
        }
        return null;
    }

    public PriceWithDiscountScore getScore() {
        return score;
    }

    @Override
    public List<SingleRouteToStopActivity> getChildRoutes() {
        return Collections.singletonList(this);
    }

    @Override
    public SingleRouteToStopActivity getBestRouteWithoutDiscount() {
        return this;
    }

    @Override
    public String toString() {
        return "score=" + score +
                ", activities=" + activities;
    }
	/*
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SingleRouteToStopActivity)) return false;

		SingleRouteToStopActivity that = (SingleRouteToStopActivity) o;

		return activities.equals(that.activities);

	}

	@Override
	public int hashCode() {
		return activities.hashCode();
	}*/
}
