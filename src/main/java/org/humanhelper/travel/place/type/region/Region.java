package org.humanhelper.travel.place.type.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.humanhelper.data.bean.name.ProxyNameIdBean;
import org.humanhelper.service.utils.CollectionHelper;
import org.humanhelper.travel.geo.BoundingBox;
import org.humanhelper.travel.geo.Location;
import org.humanhelper.travel.place.Place;
import org.humanhelper.travel.place.dao.PlaceDao;

import java.util.HashSet;
import java.util.Set;

/**
 * Если у региона задан родительский регион, то этот родительский регион используется
 * только для того, чтобы пользователь мог в поиске мест выбрать этот родительский регион.
 * И это означает, что он может добраться до этого региона переместившись в любую дочернее
 * место этого региона.
 *
 * @author Андрей
 * @since 29.12.14
 */
public class Region extends Place {

    public static final String BOUNDING_BOX_FIELD = "box";
    public static final String HOTELS_FIELD = "hotels";
    public static final String CHILDS_FIELD = "childs";

    @JsonUnwrapped
    //@JsonProperty(BOUNDING_BOX_FIELD)
    private BoundingBox bBox;//Из-за того, что мы используем JsonUnwrapper, bBox будет всегда создаваться и уже там внутри он будет пустой
    @JsonProperty(HOTELS_FIELD)
    private Integer hotels;
    @JsonProperty(CHILDS_FIELD)
    private Set<Place> childs = new HashSet<>();

    public static Region build(String name) {
        return build(Region.class, name);
    }

    public static boolean isRegion(Place place) {
        return place instanceof Region;
    }

    public static Region get(Place place) {
        return (Region) place;
    }

    public Integer getHotels() {
        return hotels;
    }

    public void setHotels(Integer hotels) {
        this.hotels = hotels;
    }

    public Set<Place> getChilds() {
        return childs;
    }

    public void setChilds(Set<Place> childs) {
        this.childs = childs;
    }

    public Region addChild(Place child) {
        childs.add(child);
        return this;
    }

    public Region region(Region region) {
        setRegion(region);
        return this;
    }

    public Region linkRegion(Region region) {
        region.addChild(this);
        return region(region);
    }

    public BoundingBox getbBox() {
        return bBox;
    }

    public void setbBox(BoundingBox bBox) {
        this.bBox = bBox;
    }

    @Override
    public Set<Place> transportPlaces() {
        if (!regionGroup()) {
            return super.transportPlaces();
        }
        Set<Place> result = new HashSet<>();
        for (Place child : getChilds()) {
            result.addAll(child.transportPlaces());
        }
        return result;
    }

    @Override
    public String toString() {
        return "Region{" +
                "childs=" + CollectionHelper.toString(getChilds(), ProxyNameIdBean::nameOrId) + ", " +
                super.toString() +
                '}';
    }

    public boolean regionGroup() {
        return !getChilds().isEmpty();
    }

    public boolean removeChild(Place place) {
        if (childs.contains(place)) {
            return childs.remove(place);
        }
        //Возможно это место в момент попадания в childs имело другой hashCode
        Set<Place> newChilds = new HashSet<>();
        boolean removed = false;
        for (Place child : childs) {
            if (place.equals(child)) {
                removed = true;
            } else {
                newChilds.add(child);
            }
        }
        childs = newChilds;
        if (removed) {
            return true;
        }
        //Значит child находится где-то внутри childs.childs
        for (Place child : childs) {
            if (Region.isRegion(child)) {
                if (Region.get(child).removeChild(place)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void save(PlaceDao placeDao) {
        calculateBoundingBox(this);
        for (Place child : getChilds()) {
            //Также нам нужно сохранить все дочерние места
            child.save(placeDao);
        }
        super.save(placeDao);
    }

    private BoundingBox calculateBoundingBox(Region region) {
        if (region.regionGroup()) {//В тестах location может быть не задан
            //Убираем location и вычисляем bBox
            BoundingBox boundingBox = region.getLocation() != null ? BoundingBox.location(region.getLocation()) : new BoundingBox();
            for (Place child : region.getChilds()) {
                Location location = child.getLocation();
                if (location != null) {
                    boundingBox.addLocation(child.getLocation());
                } else {
                    if (Region.isRegion(child)) {
                        BoundingBox childBBox = calculateBoundingBox(Region.get(child));
                        if (childBBox != null) {
                            boundingBox.addBoundingBox(childBBox);
                        } else {
                            throw new IllegalStateException("Bounding box in region is null:" + child);
                        }
                    } else {
                        throw new IllegalStateException("Not found location in place:" + child);
                    }
                }
            }
            if (boundingBox.empty()) {
                throw new IllegalStateException("Location of childs is empty for:" + region);
            }
            region.setbBox(boundingBox);
            //region.setLocation(null); Не очищаем location, так как например у какого-то города может быть задан location, но при этом внутри него два дочерних аэропорта
            return boundingBox;
        }
        return null;
    }

    @Override
    public boolean isSimilar(Place place) {
        if (bBoxIsEmpty()) {
            return super.isSimilar(place);
        }
        return isNameSimilar(place) && bBox.inside(place.getLocation());
    }

    public boolean bBoxIsEmpty() {
        return bBox == null || bBox.empty(); //Если обьект создается из json, то bBox будет создан, но возможно пустой
    }
}
