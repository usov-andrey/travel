package org.humanhelper.travel.route.routesearch.graph.search;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Реализация путя, не через список, а через хранения ссылки на предыдущий путь
 *
 * @author Андрей
 * @since 22.10.15
 */
public class Path<V> {

    protected V value;
    protected Path<V> previous;

    public Path() {
    }

    public Path(V value, Path<V> previous) {
        this.value = value;
        this.previous = previous;
    }

    public static <V> Path<V> empty() {
        return new Path<>();
    }


    public Path<V> getPrevious() {
        return previous;
    }

    public V getValue() {
        return value;
    }

    /**
     * Возвращаем новый путь с добавленной вершиной
     */
    public Path<V> add(V vertex) {
        return new Path<>(vertex, this);
    }

    public boolean notEmpty() {
        return value != null;
    }

    @Override
    public String toString() {
        return value != null ? (
                previous.notEmpty() ? previous + ", " + value : value.toString()) : "";
    }

    public String toString(Function<V, String> function) {
        return value != null ? (
                previous.notEmpty() ? previous.toString(function) + ", " + function.apply(value) : function.apply(value)) : "";
    }

    public String toStringInverted() {
        return value != null ? (
                previous.notEmpty() ? value + ", " + previous.toStringInverted() : value.toString()) : "";
    }

    /**
     * Обработывает путь с начала до конца
     */
    public void startToEnd(Consumer<V> consumer) {
        if (previous.notEmpty()) {
            previous.startToEnd(consumer);
        }
        consumer.accept(value);
    }

    /**
     * Обрабатывает пусть с конца до начала
     */
    public void endToStart(Consumer<V> consumer) {
        consumer.accept(value);
        if (previous.notEmpty()) {
            previous.endToStart(consumer);
        }
    }

    public int size() {
        return previous != null ? 1 + previous.size() : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Path)) return false;

        Path<?> path = (Path<?>) o;

        if (value != null ? !value.equals(path.value) : path.value != null) return false;
        return !(previous != null ? !previous.equals(path.previous) : path.previous != null);

    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (previous != null ? previous.hashCode() : 0);
        return result;
    }
}
