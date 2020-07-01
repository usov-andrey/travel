package org.humanhelper.travel.dataprovider.task;

import org.humanhelper.travel.dataprovider.AbstractFixedPriceDataProvider;

/**
 * @author Андрей
 * @since 20.01.15
 */
public class UpdateRoutesWithFixedPriceDataProviderTask extends AbstractDataProviderTask<AbstractFixedPriceDataProvider> {

    private FixedPriceRoutes routes;

    @Override
    protected void doRun() {
        String source = routes.getSource();
        String target = routes.getTarget();
        getDataProvider().updateWays(source, target);
    }

    public FixedPriceRoutes getRoutes() {
        return routes;
    }

    public void setRoutes(FixedPriceRoutes routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "UpdateRoutesWithFixedPriceTask{" +
                "routes=" + routes +
                '}';
    }
}
