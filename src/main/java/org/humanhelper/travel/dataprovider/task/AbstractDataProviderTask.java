package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.travel.dataprovider.DataProvider;
import org.humanhelper.travel.dataprovider.DataProviderStore;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Андрей
 * @since 30.05.14
 */
public abstract class AbstractDataProviderTask<T extends DataProvider> extends Task {

    private String dataProviderClassName;
    private T dataProvider;
    @Autowired
    private DataProviderStore store;

    public String getDataProviderClassName() {
        return getDataProvider().getClass().getName();
    }

    public void setDataProviderClassName(String dataProviderClassName) {
        this.dataProviderClassName = dataProviderClassName;
    }

    protected T getDataProvider() {
        if (dataProvider == null) {
            dataProvider = store.getDataProvider(dataProviderClassName);
        }
        return dataProvider;
    }

    public void setDataProvider(T dataProvider) {
        this.dataProvider = dataProvider;
    }
}
