package org.humanhelper.travel.integration.rome2rio;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.service.task.TaskRunException;
import org.humanhelper.service.task.TaskRunnable;
import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioAutoCompleteService;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioPlace;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Андрей
 * @since 29.12.14
 */
public class Rome2RioUpdatePlacesTask extends Task {

    private static final String symbols = "abcdefghijklnmopqrstuvwxyz";

    private String prefix;

    @Autowired
    private TaskRunner taskRunner;
    @Autowired
    private Rome2RioAutoCompleteService service;
    @Autowired
    private PlaceDao placeDao;

    public Rome2RioUpdatePlacesTask() {
    }

    @Override
    protected void doRun() {
        if (prefix.length() > 2) {
            //Делаем запрос, если данных слишком много, то увеличиваем текст запроса на один символ
            Collection<Rome2RioPlace> places = service.search(prefix);
            if (places.size() < 10) {
                //Сохраняем места
                for (Rome2RioPlace rome2RioPlace : places) {
                    Place place = rome2RioPlace.createPlace();
                    if (placeDao.get(place.getId()) == null) {
                        placeDao.insert(place);
                    }
                }
                return;
            }

        }
        runChildTasks();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void runChildTasks() {
        Set<TaskRunnable> tasks = new HashSet<>();
        for (int i = 0; i < symbols.length(); i++) {
            Rome2RioUpdatePlacesTask task = Task.create(Rome2RioUpdatePlacesTask.class);
            String symb = String.valueOf(symbols.charAt(i));
            if (prefix.isEmpty()) {
                symb = symb.toUpperCase();
            }
            task.setPrefix(prefix + symb);
            tasks.add(task);
        }
        taskRunner.run((taskRunnable, e) -> {
            throw new TaskRunException(e);
        }, tasks);
    }
}
