package org.humanhelper.travel.route.routesearch.query;

import org.humanhelper.service.utils.DateHelper;
import org.humanhelper.travel.service.period.DatePeriod;
import org.humanhelper.travel.service.period.NumberPeriod;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Проверяет RouteQuery на корректность данных
 *
 * @author Андрей
 * @since 10.12.14
 */
@Service
public class RouteQueryValidator {

    public void validate(RouteQuery query) {
        //Подготавливаем Query, так как могут быть проблемы с заполнением
        //Также пересчитываем даты, в случае если указаны более широкие сроки
        if (query.getEnd().getPlace() == null) {
            //Если не задана точка окончания, считаем, что вернуться нужно в начало
            query.getEnd().setPlace(query.getStart().getPlace());
        }
        DatePeriod startPeriod = query.getStart().getPeriod();
        DatePeriod endPeriod = query.getEnd().getPeriod();
        if (startPeriod.getStart() == null) {
            throw new IllegalArgumentException("Not found startPeriod.start");
        }
        if (endPeriod == null) {
            endPeriod = new DatePeriod(startPeriod);
        }
        if (endPeriod.getEnd() == null) {
            throw new IllegalArgumentException("Not found endPeriod.end");
        }

        for (RouteQueryStop stop : query.getStops()) {
            if (stop.getPlace() == null) {
                throw new IllegalArgumentException("Stop place can't be null");
            }
            if (stop.getPlace().equals(query.getStart().getPlace())) {
                throw new IllegalArgumentException("Stop can't equals start place");
            }
            if (stop.getPlace().equals(query.getEnd().getPlace())) {
                throw new IllegalArgumentException("Stop can't equals end place");
            }
        }

        int minNightsCount = 0;
        for (RouteQueryStop stop : query.getStops()) {
            NumberPeriod nightsPeriod = stop.getNights();
            Integer nightsStart = nightsPeriod.getStart();
            if (nightsStart == null) {
                throw new IllegalArgumentException("Not found nights.start in stop:" + stop);
            }
            minNightsCount += nightsStart;
            if (nightsPeriod.getEnd() == null) {
                nightsPeriod.setEnd(nightsStart);
            }
        }


        Date maxStartPeriodEnd = DateHelper.incDays(endPeriod.getEnd(), -minNightsCount);
        if (startPeriod.getEnd() == null || endPeriod.getEnd().after(maxStartPeriodEnd)) {
            startPeriod.setEnd(maxStartPeriodEnd);
        }

        Date minEndPeriodStart = DateHelper.incDays(startPeriod.getStart(), minNightsCount);
        if (endPeriod.getStart() == null || endPeriod.getStart().before(minEndPeriodStart)) {
            endPeriod.setStart(minEndPeriodStart);
        }

        startPeriod.clearTime();
        endPeriod.clearTime();
    }
}
