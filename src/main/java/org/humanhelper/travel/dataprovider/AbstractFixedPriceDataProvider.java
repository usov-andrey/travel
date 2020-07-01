package org.humanhelper.travel.dataprovider;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.travel.dataprovider.task.FixedPriceRoutes;
import org.humanhelper.travel.dataprovider.task.UpdateRoutesWithFixedPriceDataProviderTask;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.SingleRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.humanhelper.travel.service.period.DatePeriod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Выделение общего кода для AnyTime и ScheduleTime в один класс
 *
 * @author Андрей
 * @since 24.02.15
 */
public abstract class AbstractFixedPriceDataProvider<T extends SingleRoute> extends DataProvider {

    protected SourceTargetRouteList<T> getWays(String source, String target) {
        return getWays(getPlaceById(source), getPlaceById(target));
    }

    /**
     * Список перемещений за день из source в target
     */
    protected abstract SourceTargetRouteList<T> getWays(Place source, Place target);

    protected abstract Collection<T> getExistsRoutes(Place source, Place target);

    @Override
    public void updateRoutes(Place source, Place target, float usageCoefficient, DatePeriod datePeriod) {
        Collection<T> anyTimeWayRoutes = getExistsRoutes(source, target);
        //Обновлять цены с фиксированной ценой нужно реже чем с плавающей, отсюда -1
        float priority = getExistsRouteCoefficient(isExistsRoute(anyTimeWayRoutes), usageCoefficient) - 1;
        addToQueue(source, target, priority);
    }

    private void addToQueue(Place source, Place target, float priority) {
        UpdateRoutesWithFixedPriceDataProviderTask task = Task.create(UpdateRoutesWithFixedPriceDataProviderTask.class);
        task.setRoutes(new FixedPriceRoutes(source.getId(), target.getId()));
        task.setDataProvider(this);
        taskRunner.run(task);
    }

    private boolean isExistsRoute(Collection<T> routes) {
        for (T route : routes) {
            if (route.getPriceResolver().getPriceAgent().getName().equals(getName())) {
                return true;
            }
        }
        return false;
    }

    public List<T> getRoutes() {
        List<T> routes = new ArrayList<>();
        Map<Place, Collection<Place>> places = getPlaces();
        for (Place source : places.keySet()) {
            for (Place target : places.get(source)) {
                routes.addAll(getWays(source, target).getWayRoutes());
            }
        }
        return routes;
    }

    abstract public void updateWays(String source, String target);
}
