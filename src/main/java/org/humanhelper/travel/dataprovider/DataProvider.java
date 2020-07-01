package org.humanhelper.travel.dataprovider;

import org.humanhelper.service.task.TaskRunner;
import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.integration.google.GoogleService;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.place.fordelete.OldPlaceService;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.price.PriceAgent;
import org.humanhelper.travel.price.PriceAgentDao;
import org.humanhelper.travel.price.currency.CurrencyConverter;
import org.humanhelper.travel.route.dao.RouteDao;
import org.humanhelper.travel.route.type.TimeRoute;
import org.humanhelper.travel.route.usage.UsageDao;
import org.humanhelper.travel.service.period.DatePeriod;
import org.humanhelper.travel.transport.Transport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Поставщик данных о местоположениях и путях следования
 *
 * @author Human Helper
 * @since 30.10.13
 */
public abstract class DataProvider implements InitializingBean, PriceAgent {

    @Autowired(required = false)//Для простых тестов dao не подключен
    protected PlaceDao placeDao;
    @Autowired
    protected RouteDao routeDao;
    @Autowired
    protected UsageDao usageDao;
    @Autowired
    protected TaskRunner taskRunner;
    @Autowired
    protected OldPlaceService placeResolver;
    @Autowired
    private PriceAgentDao priceAgentDao;
    @Autowired
    private CurrencyConverter currencyConverter;
    @Autowired
    private DataProviderStore store;
    @Autowired
    private GoogleService googleService;
    private Map<Place, Collection<Place>> places;
    private Map<String, Place> placeByIdMap;

    /**
     * @return Список всех обслуживаемых мест данным поставщиком и для каждого места - список мест, куда можно попасть
     * с помощью этого поставщика
     */
    public final synchronized Map<Place, Collection<Place>> getPlaces() {
        loadPlaces();
        return places;
    }

    protected void loadPlaces() {
        if (places == null) {
            clearPlaces();
            updatePlaces();
        }
    }

    public void clearPlaces() {
        places = new HashMap<>();
        placeByIdMap = new HashMap<>();
    }

    abstract public void updatePlaces();

    @Override
    public void afterPropertiesSet() throws Exception {
        priceAgentDao.addPriceAgent(this);
        store.addDataProvider(this);
    }

    @Override
    public float getPrice(float price, Currency from, Currency to) {
        return currencyConverter.convert(price, from, to);
    }

    protected Region getRegion(String regionName, CountryBuilder country) {
        return placeResolver.getRegion(regionName, placeResolver.getCountry(country.getCode()));
    }

    /**
     * Некоторые города в базе с правильным суффиксом City, но при этом во многих поставщиках данных, они идут просто без City
     */
    protected Region getRegionWithSuffixCity(String regionName, CountryBuilder country) {
        return placeResolver.getRegionWithSuffixCity(regionName, placeResolver.getCountry(country.getCode()));
    }

    protected Region getRegion(Place place) {
        Region region = place.getRegion();
        if (region == null) {
            throw new IllegalStateException("Not found region in dao by place:" + place);
        }
        return region;
    }

    protected void addRoundRoute(Place source, Place target) {
        addOneWayRoute(source, target);
        addOneWayRoute(target, source);
    }

    protected void addOneWayRoute(Place source, Place target) {
        if (places == null) {
            clearPlaces();
        }
        Collection<Place> targets = places.get(source);
        if (targets == null) {
            targets = new HashSet<>();
            places.put(source, targets);
        }
        targets.add(target);
        placeDao.addRoad(source, target);
        placeByIdMap.put(source.getId(), source);
        placeByIdMap.put(target.getId(), target);
    }

    protected Place getPlaceById(String placeId) {
        loadPlaces();
        Place place = placeByIdMap.get(placeId);
        if (place == null) {
            throw new IllegalStateException("Not found place by id:" + placeId);
        }
        return place;
    }

    protected Transport createTransport(String transportName, String company) {
        Transport transport = createTransport(company);
        transport.setName(transportName);
        return transport;
    }

    protected Transport createTransport(String company) {
        return new Transport(company);
    }

    //-----------Обновление маршрутов-------------

    /**
     * @param datePeriod может быть null
     */
    public void updateRoutes(DatePeriod datePeriod) {
        Map<Place, Collection<Place>> places = getPlaces();
        for (Place source : places.keySet()) {
            float sourceUsageCoefficient = getUsageCoefficient(source);
            for (Place target : places.get(source)) {
                float targetUsageCoefficient = getUsageCoefficient(target);
                updateRoutes(source, target, sourceUsageCoefficient + targetUsageCoefficient, datePeriod);
            }
        }
    }

    protected int getDuration(Place source, Place target) {
        //Считаем, что продолжительность маршрута в два раза больше чем ехать на автомобиле
        return googleService.getDurationRoute(source.getLocation(), target.getLocation()) * 2;
    }

    abstract public void updateRoutes(Place source, Place target, float usageCoefficient, DatePeriod datePeriod);

    private float getUsageCoefficient(Place place) {
        return usageDao.getUsageCoefficient(place);
    }

    protected float getExistsRouteCoefficient(boolean isExists, float usageCoefficient) {
        //Если два места используются, то это + 6
        //Если используется время то + 1
        //В результате если данные по перемещению еще не получены, то + 10
        return !isExists ? 10 + usageCoefficient * 3 : usageCoefficient * 3;
    }

    public void setTaskRunner(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    @Override
    public String toString() {
        loadPlaces();
        int routes = 0;
        for (Collection<Place> targets : places.values()) {
            routes += targets.size();
        }
        return "DataProvider{" + getClass() + " places=" + places.keySet().size() + ", routes=" + routes + "}";
    }

    @Override
    public String getUrl(TimeRoute route) {
        return null;  //TODO
    }
}
