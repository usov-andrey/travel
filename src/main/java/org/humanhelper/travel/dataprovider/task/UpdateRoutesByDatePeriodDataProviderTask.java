package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.travel.service.period.DatePeriod;

import java.util.Date;
import java.util.Set;

/**
 * Делает запросы по датам в случайном порядке
 *
 * @author Андрей
 * @since 19.12.14
 */
public class UpdateRoutesByDatePeriodDataProviderTask extends AbstractRoutesByDateDataProviderTask {

    private DatePeriod datePeriod;

    @Override
    protected void addRoutes(Set<DayRoutes> routes, String source, String target) {
        for (Date day : datePeriod.dayIterator()) {
            routes.add(new DayRoutes(source, target, day));
        }
    }

    public DatePeriod getDatePeriod() {
        return datePeriod;
    }

    public void setDatePeriod(DatePeriod datePeriod) {
        this.datePeriod = datePeriod;
    }


}
