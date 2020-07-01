package org.humanhelper.travel.route.routesearch.time.search.vertex;

import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.route.Activity;
import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.visitor.GraphVisitor;
import org.humanhelper.travel.route.type.Route;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.waydiscount.DiscountStrategy;

import java.util.Collection;

/**
 * Начало движения по составному пути
 *
 * @author Андрей
 * @since 08.12.14
 */
public class StartDiscountWayVertex extends RouteItemPathVertex<TimeRoute> {

    private DiscountStrategy discountStrategy;
    private Integer endDiscountCode;

    public StartDiscountWayVertex(TimeRoute routeItem, DiscountStrategy discountStrategy) {
        super(routeItem);
        this.discountStrategy = discountStrategy;
        endDiscountCode = discountStrategy.getEndCodeForStartWay(routeItem);
    }

    @Override
    public void visit(GraphVisitor visitor, Path path) {
        if (path.addPathVertexForVisit(endDiscountCode)) {
            super.visit(visitor, path);
            path.removeVisitedPathVertex(endDiscountCode);
        }
    }

    @Override
    protected float getCurrentPrice() {
        return discountStrategy.getPriceForStartWay(routeItem);
    }

    @Override
    protected void fillCurrentRoute(Collection<Activity> route) {
        Route copy = routeItem.copy();
        copy.setPriceResolver(new PriceResolver(getCurrentPrice(), routeItem.getPriceResolver().getCurrency(), routeItem.getPriceResolver().getPriceAgent()));
        route.add(copy);
    }

    @Override
    public int getPathHashCode() {
        return (31 * super.getPathHashCode() + getClass().hashCode()) * 31 + routeItem.hashCode();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getClass().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "-StartDiscount";
    }
}
