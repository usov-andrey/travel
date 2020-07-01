package org.humanhelper.travel.place.sourcetarget;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.Route;

/**
 * @author Андрей
 * @since 27.09.15
 */
public class SourceTarget {

    private Place source;
    private Place target;

    public SourceTarget() {
    }

    public SourceTarget(Place source, Place target) {
        this.source = source;
        this.target = target;
    }

    public static SourceTarget get(Route route) {
        return new SourceTarget(route.getSource(), route.getTarget());
    }

    public boolean isSourceEqualsTarget() {
        return source.equals(target);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SourceTarget)) return false;

        SourceTarget that = (SourceTarget) o;

        if (!source.equals(that.source)) return false;
        return target.equals(that.target);

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "source=" + source +
                ", target=" + target;
    }
}
