package org.humanhelper.travel.service.period;

/**
 * @author Андрей
 * @since 08.05.14
 */
public class Period<T> {

    protected T start;
    protected T end;

    public Period() {
    }

    public Period(T startAndEnd) {
        this.start = startAndEnd;
        this.end = startAndEnd;
    }

    public Period(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(T end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Period{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

}
