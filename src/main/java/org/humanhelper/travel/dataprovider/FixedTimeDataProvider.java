package org.humanhelper.travel.dataprovider;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.dataprovider.task.DayRoutes;
import org.humanhelper.travel.dataprovider.task.UpdateRoutesByPlacesDateDataProviderTask;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.type.sourcetarget.SourceTargetRouteList;
import org.humanhelper.travel.service.period.DatePeriod;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Цена в зависимости от времени перемещения меняется. Обычно цена меняется
 * каждый день и обновлять такие цены необходимо с большим приоритетом(как можно чаще)
 * <p>
 * Здесь определена логика обновления маршрутов в такого типа провайдерах
 *
 * @author Андрей
 * @since 20.01.15
 */
public abstract class FixedTimeDataProvider extends DataProvider {

    private int updateDays = 365;//Загружаем цены на год вперед

    public SourceTargetRouteList<TimeRoute> getWays(String source, String target, Date day) {
        return getWays(getPlaceById(source), getPlaceById(target), day);
    }

    /**
     * Список перемещений за день из source в target
     */
    protected abstract SourceTargetRouteList<TimeRoute> getWays(Place source, Place target, Date day);

    @Override
    public void updateRoutes(Place source, Place target, float usageCoefficient, DatePeriod datePeriod) {
        if (datePeriod == null) {
            Date date = DateHelper.clearTime(new Date());
            datePeriod = new DatePeriod(date, DateHelper.setTime(DateHelper.incDays(date, updateDays), 23, 59));
        }
        List<TimeRoute> routes = routeDao.getWays(source, target, datePeriod.getStart(), datePeriod.getEnd());
        Set<Date> daysWithExistsRoutes = getDaysExistsRoutes(routes);
        for (Date date : datePeriod.dayIterator()) {
            float priority = getExistsRouteCoefficient(daysWithExistsRoutes.contains(date), usageCoefficient);
            priority += getUsageCoefficient(date);
            addToQueue(source, target, date, priority);
        }
    }

    private void addToQueue(Place source, Place target, Date date, float priority) {
        DayRoutes dayRoutes = new DayRoutes(source.getId(), target.getId(), date);
        UpdateRoutesByPlacesDateDataProviderTask task = Task.create(UpdateRoutesByPlacesDateDataProviderTask.class);
        task.setDayRoutes(dayRoutes);
        task.setDataProvider(this);
        taskRunner.run(task);
    }

    private Set<Date> getDaysExistsRoutes(List<TimeRoute> routes) {
        Set<Date> result = new HashSet<>();
        for (TimeRoute route : routes) {
            result.add(route.getStartTime());
        }
        return result;
    }

    protected float getUsageCoefficient(Date date) {
        return usageDao.getUsageCoefficient(date);
    }

    public void setUpdateDays(int updateDays) {
        this.updateDays = updateDays;
    }
}
