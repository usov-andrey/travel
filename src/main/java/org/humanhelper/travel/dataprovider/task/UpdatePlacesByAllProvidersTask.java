package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.travel.dataprovider.DataProvider;
import org.humanhelper.travel.dataprovider.DataProviderStore;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Андрей
 * @since 16.01.15
 */
public class UpdatePlacesByAllProvidersTask extends Task {

    @Autowired
    private DataProviderStore store;

    @Override
    protected void doRun() {
        for (DataProvider dataProvider : store.getDataProviders()) {
            log.info("Update places by " + dataProvider);
            dataProvider.updatePlaces();
        }
    }
}
