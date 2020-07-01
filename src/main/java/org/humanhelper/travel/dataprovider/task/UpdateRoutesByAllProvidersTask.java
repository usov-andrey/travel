package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.travel.dataprovider.DataProvider;
import org.humanhelper.travel.dataprovider.DataProviderStore;
import org.humanhelper.travel.service.period.DatePeriod;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Обновляет маршруты по всем DataProvider
 *
 * @author Андрей
 * @since 20.01.15
 */
public class UpdateRoutesByAllProvidersTask extends Task {

    @Autowired
    private DataProviderStore store;

    private DatePeriod datePeriod;

    @Override
    protected void doRun() {
        for (DataProvider dataProvider : store.getDataProviders()) {
            log.debug("Update routes by dataProvider:" + dataProvider.getName());
            dataProvider.updateRoutes(datePeriod);
        }
    }

    public DatePeriod getDatePeriod() {
        return datePeriod;
    }

    public void setDatePeriod(DatePeriod datePeriod) {
        this.datePeriod = datePeriod;
    }
}
