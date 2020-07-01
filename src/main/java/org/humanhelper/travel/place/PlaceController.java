package org.humanhelper.travel.place;

import org.humanhelper.extservice.task.queue.Task;
import org.humanhelper.travel.dataprovider.task.UpdatePlacesByAllProvidersTask;
import org.humanhelper.travel.integration.booking.BookingDataProvider;
import org.humanhelper.travel.integration.travelpayouts.TPDataProvider;
import org.humanhelper.travel.place.createdb.FilePlaceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Андрей
 * @since 02.09.15
 */
@Controller
@RequestMapping("places")
public class PlaceController {

    private static final Logger log = LoggerFactory.getLogger(PlaceController.class);

    @Autowired
    private TPDataProvider tpDataLoader;
    @Autowired
    private BookingDataProvider bookingService;
    @Autowired
    private FilePlaceLoader filePlaceCreator;

    /**
     * Начальное наполнение чистой базы
     */
    @RequestMapping(value = "createdb", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void create() {
		/*
		log.debug("Creating countries");
		tpDataLoader.createCountries();
		log.debug("Creating cities with booking");
		bookingService.createCities();
		log.debug("Creating places from file");
		filePlaceCreator.createPlaces();
		log.debug("Creating airports");
		tpDataLoader.createAirportsAndCities();
		log.debug("Linking places by DataProviders");
		updatePlacesByDataProviders();
		log.debug("Places created");*/
    }

    @RequestMapping(value = "updateByProviders", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePlacesByDataProviders() {
        UpdatePlacesByAllProvidersTask task = Task.create(UpdatePlacesByAllProvidersTask.class);
        task.run();
    }
}
