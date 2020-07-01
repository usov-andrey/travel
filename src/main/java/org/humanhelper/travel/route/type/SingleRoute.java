package org.humanhelper.travel.route.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.sourcetarget.SourceTarget;
import org.humanhelper.travel.transport.Transport;

/**
 * Перемещение из Source в Target с помощью Transport
 *
 * @author Андрей
 * @since 21.01.15
 */
public abstract class SingleRoute extends RouteBase {

    public static final String TARGET_ID_FIELD = "targetId";
    public static final String TRANSPORT_FIELD = "transport";

    protected Place target;
    protected Transport transport;

    @JsonIgnore
    public Place getTarget() {
        return target;
    }

    @JsonIgnore
    public void setTarget(Place target) {
        this.target = target;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FixedTimeRoute)) return false;
        if (!super.equals(o)) return false;

        SingleRoute that = (SingleRoute) o;

        if (!target.equals(that.target)) return false;
        if (transport != null ? !transport.equals(that.transport) : that.transport != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + (transport != null ? transport.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                ",target=" + target +
                (transport != null ? ", transport=" + transport : "");
    }

    public SingleRoute sourceTarget(Place source, Place target) {
        setSource(source);
        setTarget(target);
        return this;
    }

    public SingleRoute transport(Transport transport) {
        setTransport(transport);
        return this;
    }

    public SourceTarget sourceTarget() {
        return new SourceTarget(getSource(), getTarget());
    }
}
