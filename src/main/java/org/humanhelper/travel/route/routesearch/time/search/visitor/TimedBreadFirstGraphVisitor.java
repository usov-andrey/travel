package org.humanhelper.travel.route.routesearch.time.search.visitor;

import org.humanhelper.travel.route.routesearch.time.Graph;
import org.humanhelper.travel.route.routesearch.time.search.Path;
import org.humanhelper.travel.route.routesearch.time.search.vertex.Vertex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Андрей
 * @since 08.01.15
 */
public class TimedBreadFirstGraphVisitor extends BreadFirstGraphVisitor {

    private Map<Class, Long> counter = new HashMap<>();
    private Map<Class, Integer> no = new HashMap<>();

    public TimedBreadFirstGraphVisitor(Graph graph) {
        super(graph);
    }

    public void visit(Vertex vertex, Path path) {
        long time1 = System.nanoTime();
        for (Vertex target : graph.getTargets(vertex)) {
            long time = System.nanoTime();
            target.visit(this, path);
            calculate(target, time);
        }
        decTime(vertex, time1);
    }

    private void decTime(Vertex target, long startTime) {
        Long ms = counter.get(target.getClass());
        if (ms != null) {
            ms -= (System.nanoTime() - startTime);
            counter.put(target.getClass(), ms);
        }
    }

    private void calculate(Vertex target, long startTime) {
        Long ms = counter.get(target.getClass());
        if (ms == null) ms = 0L;
        ms += (System.nanoTime() - startTime);
        counter.put(target.getClass(), ms);
        Integer n = no.get(target.getClass());
        if (n == null) {
            n = 1;
        } else {
            n++;
        }
        no.put(target.getClass(), n);
    }

    public void printStatistics() {
        long allTime = 0;
        for (Class cl : counter.keySet()) {
            long time = counter.get(cl);
            allTime += time;
            System.out.println(cl + " count:" + no.get(cl) + " time(ms):" + (TimeUnit.NANOSECONDS.toMillis(time)) + " for one(ns):" + (time / no.get(cl)));
        }
        System.out.println("time:" + TimeUnit.NANOSECONDS.toMillis(allTime));
    }
}
