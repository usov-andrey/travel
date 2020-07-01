package org.humanhelper.travel.dataprovider;

import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.AnyTimeRoute;

import java.util.Collection;

/**
 * Поставщик данных в котором цена не зависит от времени перемещения и всегда постоянна
 * Перемещаться можно в любое время и вместимость не ограничена
 * В этом случае обновлять цену необходимо максимально редко.
 *
 * @author Андрей
 * @since 20.01.15
 */
public abstract class AnyTimeDataProvider extends AbstractFixedPriceDataProvider<AnyTimeRoute> {

    @Override
    public void updateWays(String source, String target) {
        routeDao.updateAnyTimeWays(getWays(source, target));
    }

    @Override
    protected Collection<AnyTimeRoute> getExistsRoutes(Place source, Place target) {
        return routeDao.getAnyTimeRoutes(source, target);
    }

}
