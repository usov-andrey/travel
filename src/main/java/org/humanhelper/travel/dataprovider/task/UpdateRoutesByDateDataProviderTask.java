package org.humanhelper.travel.dataprovider.task;

import java.util.Date;
import java.util.Set;

/**
 * Обновить информацию о маршрутах по дате
 *
 * @author Андрей
 * @since 19.05.14
 */
public class UpdateRoutesByDateDataProviderTask extends AbstractRoutesByDateDataProviderTask {

    private Date date;

    @Override
    protected void addRoutes(Set<DayRoutes> routes, String source, String target) {
        routes.add(new DayRoutes(source, target, date));
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
