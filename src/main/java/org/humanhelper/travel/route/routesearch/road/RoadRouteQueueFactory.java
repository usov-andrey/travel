package org.humanhelper.travel.route.routesearch.road;

import org.humanhelper.service.spring.DelegateFactory;
import org.humanhelper.travel.place.Place;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**
 * Определяем очередь, которая используется при поиске путей
 *
 * @author Андрей
 * @since 10.10.15
 */
@Service
public class RoadRouteQueueFactory extends DelegateFactory<Set<Place>, Queue<Place>> {

    @Override
    protected Function<Set<Place>, Queue<Place>> createDefaultImplementation() {
        return this::createPriorityLocationQueue;
    }

    private Queue<Place> createNotSortedQueue(Set<Place> targets) {
        return new LinkedBlockingQueue<>();
    }

    private Queue<Place> createPriorityLocationQueue(Set<Place> targets) {
        return new PriorityQueue<>(new LocationPlaceComparator(targets));
    }

}
