package org.humanhelper.travel.route.type.sourcetarget;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.sourcetarget.SourceTarget;

/**
 * Путь между двумя местами без направления
 *
 * @author Андрей
 * @since 27.09.15
 */
public class UndirectedSourceTarget extends SourceTarget {

    private int hash;

    public UndirectedSourceTarget(Place source, Place target) {
        super(source, target);
        hash = source.hashCode() + target.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UndirectedSourceTarget)) return false;
        if (!super.equals(o)) return false;

        UndirectedSourceTarget that = (UndirectedSourceTarget) o;

        return hash == that.hash;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + hash;
        return result;
    }
}
