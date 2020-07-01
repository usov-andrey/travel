package org.humanhelper.travel.dataprovider.offline;

import org.humanhelper.travel.country.CountryBuilder;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.fordelete.OldPlaceService;
import org.humanhelper.travel.place.fordelete.quicklink.AirportPlaceLink;
import org.humanhelper.travel.place.fordelete.quicklink.PlaceLink;
import org.humanhelper.travel.place.fordelete.quicklink.RegionPlaceLink;
import org.humanhelper.travel.price.PriceAgent;
import org.humanhelper.travel.price.PriceResolver;
import org.humanhelper.travel.transport.Transport;

/**
 * Возможность быстрого создания маршрута из кода
 *
 * @author Андрей
 * @since 28.01.15
 */
public class RouteMapEntry {

    private CountryBuilder country;
    private PriceAgent agent;

    private PlaceLink source;
    private PlaceLink target;
    private Place sourcePlace;
    private Place targetPlace;
    private Transport transport;
    private float price;

    /**
     * Создаем копию ManualRoute меняя при этом source и target местами
     */
    public RouteMapEntry(RouteMapEntry manualRoute) {
        this.source = manualRoute.target;
        this.target = manualRoute.source;
        this.transport = manualRoute.transport;
        this.country = manualRoute.country;
        this.price = manualRoute.price;
        this.agent = manualRoute.agent;
        this.sourcePlace = targetPlace;
        this.targetPlace = sourcePlace;
    }

    public RouteMapEntry(CountryBuilder country) {
        this.country = country;
    }

    public static RouteMapEntry create(CountryBuilder country) {
        return new RouteMapEntry(country);
    }

    private RegionPlaceLink region(String region) {
        return RegionPlaceLink.create(region, country);
    }

    public RouteMapEntry sourceBus(String region) {
        this.source = region(region).bus();
        return this;
    }

    public RouteMapEntry targetBus(String region) {
        this.target = region(region).bus();
        return this;
    }

    public RouteMapEntry price(float price) {
        this.price = price;
        return this;
    }

    public RouteMapEntry sourceSea(String region) {
        this.source = region(region).sea();
        return this;
    }

    public RouteMapEntry targetSea(String region) {
        this.target = region(region).sea();
        return this;
    }

    public RouteMapEntry sourceBus(String region, String placeName) {
        this.source = region(region).bus(placeName);
        return this;
    }

    public RouteMapEntry transport(String company) {
        this.transport = new Transport(company);
        return this;
    }

    public RouteMapEntry sourceAir(String airportCode) {
        this.source = AirportPlaceLink.create(airportCode);
        return this;
    }

    public RouteMapEntry targetAir(String airportCode) {
        this.target = AirportPlaceLink.create(airportCode);
        return this;
    }

    public RouteMapEntry targetRegion(String region) {
        this.target = region(region);
        return this;
    }

    public Place getSource(OldPlaceService placeResolver) {
        loadSource(placeResolver);
        return sourcePlace;
    }

    public Place getTarget(OldPlaceService placeResolver) {
        loadTarget(placeResolver);
        return targetPlace;
    }

    private void loadTarget(OldPlaceService placeResolver) {
        if (targetPlace == null) {
            targetPlace = target.getPlace(placeResolver);
        }
    }

    private void loadSource(OldPlaceService placeResolver) {
        if (sourcePlace == null) {
            sourcePlace = source.getPlace(placeResolver);
        }
    }

    public Transport getTransport() {
        return transport;
    }

    public PriceResolver getPriceResolver() {
        return new PriceResolver(price, country.getCurrency(), agent);
    }

    public RouteMapEntry agent(PriceAgent agent) {
        this.agent = agent;
        return this;
    }

    @Override
    public String toString() {
        return "ManualRoute{" +
                "country=" + country +
                ", agent=" + agent +
                ", source=" + source +
                ", target=" + target +
                ", sourcePlace=" + sourcePlace +
                ", targetPlace=" + targetPlace +
                ", transport=" + transport +
                ", price=" + price +
                '}';
    }

}
