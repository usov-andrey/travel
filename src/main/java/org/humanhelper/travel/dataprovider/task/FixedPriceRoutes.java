package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.travel.place.Place;

/**
 * @author Андрей
 * @since 20.01.15
 */
public class FixedPriceRoutes {

    protected String source;//Чтобы не хранить в памяти весь Place
    protected String target;

    public FixedPriceRoutes() {
    }

    public FixedPriceRoutes(Place source, Place target) {
        this.source = source.getId();
        this.target = target.getId();
    }

    public FixedPriceRoutes(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixedPriceRoutes)) return false;

        FixedPriceRoutes that = (FixedPriceRoutes) o;

        if (!source.equals(that.source)) return false;
        if (!target.equals(that.target)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FixedPriceRoutes{" +
                "source=" + source +
                ", target=" + target +
                '}';
    }
}
