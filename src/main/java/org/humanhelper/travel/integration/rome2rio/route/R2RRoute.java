package org.humanhelper.travel.integration.rome2rio.route;

import org.humanhelper.travel.place.Place;

import java.util.List;

/**
 * @author Андрей
 * @since 17.01.15
 */
public class R2RRoute {

    private R2RRouteKind kind;
    private Place source;
    private Place target;
    private Double km;
    private int durationInMinutes;
    private List<R2RPrice> prices;
    private List<String> companies;

    public R2RRouteKind getKind() {
        return kind;
    }

    public void setKind(R2RRouteKind kind) {
        this.kind = kind;
    }

    public Place getSource() {
        return source;
    }

    public void setSource(Place source) {
        this.source = source;
    }

    public Place getTarget() {
        return target;
    }

    public void setTarget(Place target) {
        this.target = target;
    }

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    public List<R2RPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<R2RPrice> prices) {
        this.prices = prices;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public String toString() {
        return
                "kind=" + kind +
                        ", source={" + source +
                        "}, target={" + target +
                        "}, km=" + km +
                        ", durationInMinutes=" + durationInMinutes +
                        ", companies=" + companies;
    }
}
