package org.humanhelper.travel.integration.rome2rio.route;

import com.fasterxml.jackson.databind.JsonNode;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.integration.rome2rio.autocomplete.Rome2RioPlaceKind;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.transport.Airport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Андрей
 * @since 19.01.15
 */
public enum R2RRouteKind {
    flight {
        @Override
        public R2RRoute parseRoute(JsonNode json) {
            R2RRoute route = new R2RRoute();
            route.setKind(this);
            route.setSource(Airport.build(json.get(2).textValue()));
            route.setTarget(Airport.build(json.get(3).textValue()));
            return route;
        }
    },
    towncar//uber
    , shuttle, plane, bus, tram, train, ferry, carferry, busferry, subway,
    car {
        @Override
        protected List<String> getCompanies(JsonNode json) {
            //Аренда машины у rome2rio своя
            return Collections.emptyList();
        }

        @Override
        protected void setKM(R2RRoute route, JsonNode json) {
            route.setKm(json.get(5).asDouble());
        }
    }, hotel {
        @Override
        public R2RRoute parseRoute(JsonNode json) {
            return null;
        }
    }, walk {
        @Override
        protected List<String> getCompanies(JsonNode json) {
            return Collections.emptyList();
        }

        @Override
        protected void setKM(R2RRoute route, JsonNode json) {
            route.setKm(json.get(5).asDouble());
        }
    }, finalKind {
        @Override
        public R2RRoute parseRoute(JsonNode json) {
            return null;
        }
    };


    public R2RRoute parseRoute(JsonNode json) {
        R2RRoute route = new R2RRoute();
        route.setKind(this);
        setKM(route, json);
        route.setDurationInMinutes(json.get(3).asInt() / 60);
        route.setSource(parsePlace(json.get(6)));
        route.setTarget(parsePlace(json.get(7)));
        route.setPrices(getPrices(json));
        route.setCompanies(getCompanies(json));
        return route;
    }

    protected void setKM(R2RRoute route, JsonNode json) {

    }

    protected Place parsePlace(JsonNode json) {
        String name = json.get(1).asText();
        String kind = json.get(0).asText();
        Place place = Rome2RioPlaceKind.valueOf(kind).createPlace(name);
        place.setLocation(Location.latLng(json.get(2).asDouble(), json.get(3).asDouble()));
        return place;
    }

    protected List<String> getCompanies(JsonNode json) {
        List<String> result = new ArrayList<>();
        json = json.get(13).get(8);
        for (JsonNode company : json) {
            result.add(company.get(0).asText() + "(" + company.get(2).asText() + ")");
        }
        return result;
    }

    protected List<R2RPrice> getPrices(JsonNode json) {
        List<R2RPrice> result = new ArrayList<>();
        readPrices(result, json.get(16));
        readPrices(result, json.get(17));
        return result;
    }

    protected void readPrices(List<R2RPrice> result, JsonNode json) {
        for (JsonNode price : json) {
            if (!price.get(0).asText().isEmpty()) {
                result.add(new R2RPrice(price.get(2).asDouble(), price.get(1).asText()));
            }
        }
    }


}
