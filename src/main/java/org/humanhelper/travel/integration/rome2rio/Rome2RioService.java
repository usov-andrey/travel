package org.humanhelper.travel.integration.rome2rio;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.route.type.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Андрей
 * @since 29.12.14
 */
@Service
@RequestMapping("rome2rio")
public class Rome2RioService {

    @Autowired
    private TaskRunner taskRunner;

    @RequestMapping(value = "updatePlaces", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateRoutes() {
        Rome2RioUpdatePlacesTask task = Task.create(Rome2RioUpdatePlacesTask.class);
        task.setPrefix("");
        taskRunner.run(task);
    }


    public List<List<Route>> getRoutes(Place source, Place target) {
        List<List<Route>> result = new ArrayList<>();

        return result;
    }
}
