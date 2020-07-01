package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.dataprovider.FixedTimeDataProvider;
import org.humanhelper.travel.place.Place;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Андрей
 * @since 19.12.14
 */
public abstract class AbstractRoutesByDateDataProviderTask extends AbstractDataProviderTask<FixedTimeDataProvider> {

    @Autowired
    private TaskRunner taskRunner;

    @Override
    protected void doRun() {
        Set<DayRoutes> routes = new HashSet<>();
        Map<Place, Collection<Place>> places = getDataProvider().getPlaces();
        for (Place source : places.keySet()) {
            for (Place target : places.get(source)) {
                addRoutes(routes, source.getId(), target.getId());
            }
        }
        for (DayRoutes dayRoutes : routes) {
            UpdateRoutesByPlacesDateDataProviderTask task = Task.create(UpdateRoutesByPlacesDateDataProviderTask.class);
            task.setDataProvider(getDataProvider());
            task.setDayRoutes(dayRoutes);
            taskRunner.run(task);
        }
    }

    protected abstract void addRoutes(Set<DayRoutes> routes, String source, String target);
}
