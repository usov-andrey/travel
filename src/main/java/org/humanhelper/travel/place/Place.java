package org.humanhelper.travel.place;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.StringUtils;
import org.humanhelper.service.utils.ReflectHelper;
import org.humanhelper.travel.country.BeanWithCountry;
import org.humanhelper.travel.country.Country;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.createdb.location.LocationProxy;
import org.humanhelper.travel.place.createdb.location.PlaceLocationResolver;
import org.humanhelper.travel.place.dao.PlaceDao;
import org.humanhelper.travel.place.type.accomodation.Hotel;
import org.humanhelper.travel.place.type.region.Island;
import org.humanhelper.travel.place.type.region.Region;
import org.humanhelper.travel.place.type.transport.Airport;
import org.humanhelper.travel.place.type.transport.BusStation;
import org.humanhelper.travel.place.type.transport.SeaPort;
import org.humanhelper.travel.route.provider.RouteProviderStore;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Абстрактное место обозначается системным названием, по которому происходит связь с наполнением
 * id
 * kind - airport
 * name - название места
 * shortName_локаль - название места на локальном языке
 * country - код страны
 * longName - name + название страны на найденной локали
 *
 * @author Андрей
 * @since 08.05.14
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = Place.KIND_FIELD)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Airport.class, name = "airport"),
        @JsonSubTypes.Type(value = Hotel.class, name = "hotel"),
        @JsonSubTypes.Type(value = Region.class, name = "region"),
        @JsonSubTypes.Type(value = Island.class, name = "island"),
        //@JsonSubTypes.Type(value = Event.class, name = "event"),
        //@JsonSubTypes.Type(value = Island.class, name = "island"),
        //@JsonSubTypes.Type(value = Lake.class, name = "lake"),
        //@JsonSubTypes.Type(value = Landmark.class, name = "landmark"),
        //@JsonSubTypes.Type(value = Mountain.class, name = "mountain"),
        //@JsonSubTypes.Type(value = Park.class, name = "park"),
        //@JsonSubTypes.Type(value = River.class, name = "river"),
        @JsonSubTypes.Type(value = SeaPort.class, name = "seaport"),
        @JsonSubTypes.Type(value = BusStation.class, name = "bus")
        //@JsonSubTypes.Type(value = Town.class, name = "town"),
        //@JsonSubTypes.Type(value = Village.class, name = "village")
})
public class Place extends BeanWithCountry {

    public static final String KIND_FIELD = "kind";
    public static final String REGION_FIELD = "region";
    public static final String COUNTRY_FIELD = "country";
    public static final String TARGETS_FIELD = "targets";

    //@Id//Для MongoDB
    @JsonIgnore
    private Location location;//Заполняем руками через методы setLoc и getLoc
    @JsonProperty(REGION_FIELD)
    private Region region;
    @JsonProperty(TARGETS_FIELD)
    private Set<Place> targets;

    public static <T extends Place> T create(Class<T> placeClass) {
        return ReflectHelper.newInstance(placeClass);
    }

    public static <T extends Place> T build(Class<T> placeClass, String name) {
        T place = create(placeClass);
        place.setName(name);
        return place;
    }

    //------Depricated
    public static <T extends Place> T build(Class<T> placeClass, String id, String name) {
        T place = build(placeClass, id);
        place.setName(name);
        return place;
    }

    public static <T extends Place> T build(Class<T> placeClass, String id, String name, Region city) {
        T place = build(placeClass, id, name);
        place.linkRegion(city);
        place.setCountry(city.getCountry());
        return place;
    }

    public static <T extends Place> T build(Class<T> placeClass, String id, String name, Country country) {
        T place = build(placeClass, id, name);
        place.setCountry(country);
        return place;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Place rootPlace() {
        return region != null ? region.rootPlace() : this;
    }

    /**
     * Проставляет сюда регион и в регион добавляет текущее место в childs
     */
    public Place linkRegion(Region region) {
        setRegion(region);
        region.addChild(this);
        return this;
    }

    /**
     * Время прибытия + время прохождения проверок
     */
    public Date getFirstDepartTime(Date arrivalTime) {
        return arrivalTime;
    }

    /**
     * Время отправления - время прохождения проверок
     */
    public Date getLastArriveTime(Date departTime) {
        return departTime;
    }

    public boolean isNameSimilar(Place place) {
        int differentLettersCount = StringUtils.getLevenshteinDistance(place.getName(), getName());
        return differentLettersCount < 3;
    }

    public boolean isSimilar(Place place) {
        if (getLocation() == null) {
            throw new IllegalStateException("Location is null for place:" + this);
        }
        return getLocation().inRadius(place.getLocation(), 100) && (isNameSimilar(place) || isSimilar(place.getTranslations()));
    }

    public boolean equalsPlace(Class<? extends Place> placeClass) {
        return placeClass.isAssignableFrom(getClass());
    }

    public boolean equalsPlace(Class<? extends Place> placeClass, Country country) {
        return equalsPlace(placeClass) && country.equals(this.getCountry());
    }

    public boolean equalsPlace(Class<? extends Place> placeClass, Region region) {
        return equalsPlace(placeClass) && region.equals(this.region);
    }

    @Override
    public String toString() {
        return (id != null ? "id=" + id + ", " : "")
                + "name=" + name
                + (getRegion() != null ? ", regionId=" + getRegion().nameOrId() : "")
                + (getCountry() != null ? ", country=" + getCountry() : "")
                + (getLocation() != null ? ", location=" + getLocation() : "")
                ;
    }

    //для хранения в Elastic и вывода в файл
    @JsonProperty(Location.LOCATION_FIELD)
    public float[] getLoc() {
        if (location == null) {
            return null;
        }
        return location.getLoc();
    }

    @JsonProperty(Location.LOCATION_FIELD)
    public void setLoc(float[] location) {
        this.location = new Location();
        this.location.setLoc(location);
    }

    //Должен вызываться самым последним из обращения к getName и getClass
    @JsonProperty("location")
    public void setLocationResolver(PlaceLocationResolver resolver) {
        resolver.setPlaceName(getName());
        resolver.setPlaceClass(getClass());
        setLocation(new LocationProxy(resolver));
    }

    public double getDistance(Place place) {
        return place.getLocation().getDistance(getLocation());
    }

    //Builder
    public Place name(String name) {
        setName(name);
        return this;
    }

    public Place location(double lat, double lng) {
        setLocation(Location.latLng(lat, lng));
        return this;
    }

    public Set<Place> transportPlaces() {
        return new PlaceSet<>(this);
    }

    public Set<Place> getTargets() {
        return targets;
    }

    public void setTargets(Set<Place> targets) {
        this.targets = targets;
    }

    /**
     * Заполняем this.targets, если targets не пустой список
     */
    public void createTargets(Set<? extends Place> targets) {
        if (!targets.isEmpty()) {
            Set<Place> places = new HashSet<>();
            places.addAll(targets);
            this.targets = places;
        }
    }

    public Set<? extends Place> targets() {
        return RouteProviderStore.getMainRouteProvider().getTargets(this);
    }

    /**
     * Сохраняем в базе
     */
    public void save(PlaceDao placeDao) {
        placeDao.insert(this);
    }

    /**
     * Вызывает setId(generateId)
     */
    public final void fillId() {
        setId(generateId());
    }

    protected String generateId() {
        StringBuilder sb = new StringBuilder();
        if (getRegion() != null) {
            sb.append(getRegion().generateId());
        } else if (getCountry() != null) {
            sb.append(getCountry().getId());
        }
        if (sb.length() > 0) {
            sb.append(".");
        }
        sb.append(getName());
        return sb.toString();
    }

    public boolean equalsNameWithTranslations(Place place) {
        return equalsNameWithTranslations(place.getName()) || place.equalsNameWithTranslations(name);
    }

    public String withTranslations() {
        return toString() + getTranslations();
    }

    @Override
    public int hashCode() {
        if (id == null) fillId();
        return super.hashCode();
    }
}
