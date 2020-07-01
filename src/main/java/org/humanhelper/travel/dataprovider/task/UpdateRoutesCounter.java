package org.humanhelper.travel.dataprovider.task;

import com.codahale.metrics.MetricRegistry;
import org.humanhelper.service.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Считаем количество запросов для получения билетов в день
 * и количество положительных ответов(когда есть билеты на этот день)
 *
 * @author Андрей
 * @since 19.12.14
 */
@Service
public class UpdateRoutesCounter {

    @Autowired
    private MetricRegistry metrics;

    private Set<DayRoutes> hashCodes = new HashSet<>();

    public void addCounter(DayRoutes dayRoutes, boolean haveWays) {
        if (!hashCodes.contains(dayRoutes)) {
            hashCodes.add(dayRoutes);
            metrics.counter("UpdateRoutesAll." + DateHelper.getDate(dayRoutes.getDay())).inc();
            if (haveWays) {
                metrics.counter("UpdateRoutesWithWays." + DateHelper.getDate(dayRoutes.getDay())).inc();
            }
        }
    }

}
