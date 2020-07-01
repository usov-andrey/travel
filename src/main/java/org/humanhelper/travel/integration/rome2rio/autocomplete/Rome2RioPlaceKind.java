package org.humanhelper.travel.integration.rome2rio.autocomplete;

import org.humanhelper.service.utils.StringHelper;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.type.accomodation.Accomodation;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.sight.*;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.place.type.transport.SeaPort;

/**
 * @author Андрей
 * @since 26.11.14
 */
public enum Rome2RioPlaceKind {

    unknown, node, address {
        @Override
        public Rome2RioPlace createPlace(Place place) {
            Location location = place.getLocation();
            if (location == null) {
                throw new IllegalStateException("Place location is null");
            }
            Rome2RioPlace rome2RioPlace = new Rome2RioPlace();
            rome2RioPlace.setKind(this);

            rome2RioPlace.setLat(location.getLat());
            rome2RioPlace.setLng(location.getLng());
            String name = StringHelper.cut(Float.toString(location.getLat()), 7) +
                    "%2C" +//запятая
                    StringHelper.cut(Float.toString(location.getLng()), 7);
            rome2RioPlace.setShortName(name);
            rome2RioPlace.setLongName(name);
            rome2RioPlace.setCanonicalName(name);
            rome2RioPlace.setRad(-1f);
            return rome2RioPlace;
        }
    }, continent, country, admin3, admin2, admin1, island, village, town, city {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Region();
        }
    }, capital {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Region();
        }
    }, landmark {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Landmark();
        }
    }, place, road, accommodation {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Accomodation();
        }
    }, station, airport {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            Airport place = new Airport();
            place.setCode(r2rPlace.getCode());
            return place;
        }

        @Override
        public Place createPlace(String name) {
            Airport place = new Airport();
            String code = StringHelper.getStringBetween(name, "(", ")");
            if (code.length() != 3) {
                throw new IllegalStateException("Can't get airport code in name:" + name);
            }
            place.code(code).name(name);
            return place;
        }
    }, seaport {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new SeaPort();
        }
    }, sea {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Sea();
        }
    }, lake {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Lake();
        }
    }, river {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new River();
        }
    }, mountain {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Mountain();
        }
    }, park {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Park();
        }
    }, land, event {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Event();
        }
    }, water {
        @Override
        public Place createPlace(Rome2RioPlace r2rPlace) {
            return new Sea();
        }
    };

    public Place createPlace(Rome2RioPlace r2rPlace) {
        return new Place();
    }

    public Place createPlace(String name) {
        return new Place().name(name);
    }

    public Rome2RioPlace createPlace(Place place) {
        throw new UnsupportedOperationException();
    }

}
