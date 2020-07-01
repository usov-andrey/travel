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
 * Так как цена в зависимости от последующего пути может меняться, то мы
 * не можем оптимизировать цену только между остановками
 *
 * @author Андрей
 * @since 08.12.14
 */
public class EndDiscountWayVertex extends RouteItemPathVertex<TimeRoute> {

    private DiscountStrategy discountStrategy;
    private Integer code;

    public EndDiscountWayVertex(TimeRoute routeItem, DiscountStrategy discountStrategy) {
        super(routeItem);
        this.discountStrategy = discountStrategy;
        code = discountStrategy.getEndCodeForEndWay(routeItem);
    }

    @Override
    public void visit(GraphVisitor visitor, Path path) {
        if (path.removeVisitedPathVertex(code)) {
            super.visit(visitor, path);
            path.addPathVertexForVisit(code);
        }
    }

    @Override
    protected float getCurrentPrice() {
        return discountStrategy.getPriceForEndWay(routeItem);
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
        return super.toString() + "-EndDiscount";
    }

}
