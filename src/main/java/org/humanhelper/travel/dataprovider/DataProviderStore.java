package org.humanhelper.travel.dataprovider;

import org.humanhelper.service.singleton.SingletonLocator;
import org.humanhelper.service.utils.ReflectHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Андрей
 * @since 19.05.14
 */
@Service
public class DataProviderStore {

    private List<DataProvider> dataProviders = new ArrayList<>();


    public List<DataProvider> getDataProviders() {
        return dataProviders;
    }


    public <T extends DataProvider> T getDataProvider(String dataProviderClassName) {
        Class<DataProvider> dataProviderClass = ReflectHelper.getClass(dataProviderClassName);
        if (dataProviderClass == null) {
            throw new IllegalArgumentException("Not found dataProviderClass:" + dataProviderClassName);
        }
        return (T) SingletonLocator.get(dataProviderClass);
    }

    public void addDataProvider(DataProvider dataProvider) {
        dataProviders.add(dataProvider);
    }

    public void clear() {
        dataProviders.clear();
    }
}
