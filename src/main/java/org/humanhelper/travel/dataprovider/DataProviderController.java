package org.humanhelper.travel.dataprovider;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.dataprovider.task.*;
import org.humanhelper.travel.service.period.DatePeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Общий процессор позволяет выполнять команды:
 * обновить обслуживаемые места всех провайдеров
 * обновить какое-то место всеми провайдерами
 *
 * @author Андрей
 * @since 19.05.14
 */
@RestController
@RequestMapping(value = "dataProvider")
public class DataProviderController {

    Logger log = LoggerFactory.getLogger(DataProviderController.class);

    @Autowired
    private DataProviderStore dataProviderStore;

    @Autowired
    private TaskRunner taskRunner;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<String> getDataProviderInfo() {
        List<String> classes = new ArrayList<>();
        for (DataProvider dataProvider : dataProviderStore.getDataProviders()) {
            classes.add(dataProvider.toString());
        }
        return classes;
    }

    @RequestMapping(value = "classes", method = RequestMethod.GET)
    public List<String> getDataProviderClasses() {
        List<String> classes = new ArrayList<>();
        for (DataProvider dataProvider : dataProviderStore.getDataProviders()) {
            classes.add(dataProvider.getClass().getName());
        }
        return classes;
    }

    @RequestMapping(value = "places", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePlaces() {
        UpdatePlacesByAllProvidersTask task = Task.create(UpdatePlacesByAllProvidersTask.class);
        task.run();
    }

    @RequestMapping(value = "routes", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateRoutes() {
        UpdateRoutesByAllProvidersTask task = Task.create(UpdateRoutesByAllProvidersTask.class);
        task.run();
    }

    @RequestMapping(value = "routes/{startDate}/{endDate}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateRoutes(@PathVariable Date startDate, @PathVariable Date endDate) {
        UpdateRoutesByAllProvidersTask task = Task.create(UpdateRoutesByAllProvidersTask.class);
        task.setDatePeriod(new DatePeriod(startDate, endDate));
        task.run();
    }

    @RequestMapping(value = "{dataProviderClassName}/routes/{date}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateRoutes(@PathVariable String dataProviderClassName, @PathVariable Date date) {
        UpdateRoutesByDateDataProviderTask task = Task.create(UpdateRoutesByDateDataProviderTask.class);
        task.setDate(date);
        runTask(task, dataProviderClassName);
    }

    @RequestMapping(value = "{dataProviderClassName}/routes/{startDate}/{endDate}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateRoutes(@PathVariable String dataProviderClassName, @PathVariable Date startDate, @PathVariable Date endDate) {
        UpdateRoutesByDatePeriodDataProviderTask task = Task.create(UpdateRoutesByDatePeriodDataProviderTask.class);
        task.setDatePeriod(new DatePeriod(startDate, endDate));
        runTask(task, dataProviderClassName);
    }

    @RequestMapping(value = "{dataProviderClassName}/routes/{date}/{sourceId}/{targetId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateRoutes(@PathVariable String dataProviderClassName, @PathVariable Date date, @PathVariable String sourceId, @PathVariable String targetId) {
        UpdateRoutesByPlacesDateDataProviderTask task = Task.create(UpdateRoutesByPlacesDateDataProviderTask.class);
        task.setDayRoutes(new DayRoutes(sourceId, targetId, date));
        runTask(task, dataProviderClassName);
    }

    private void runTask(AbstractDataProviderTask task, String dataProviderClassName) {
        task.setDataProviderClassName(dataProviderClassName);
        taskRunner.run(task);
    }

}
