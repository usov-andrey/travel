package org.humanhelper.travel.geo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Андрей
 * @since 07.09.15
 */
public class BoundingBox implements Serializable {

    @JsonProperty(value = "lt")
    private Location leftTop;
    @JsonProperty(value = "rb")
    private Location rightBottom;

    public BoundingBox() {
    }

    public BoundingBox(Location leftTop, Location rightBottom) {
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
    }

    public static BoundingBox location(Location location) {
        return new BoundingBox(location, location);
    }

    public static BoundingBox fromCenter(Location center, float latRadius, float lngRadius) {
        return new BoundingBox(new Location(center.getLat() - latRadius, center.getLng() - lngRadius),
                new Location(center.getLat() + latRadius, center.getLng() + lngRadius));
    }

    public static BoundingBox latLng(String value) {
        String[] values = value.split(",");
        return new BoundingBox(new Location(Double.parseDouble(values[0]), Double.parseDouble(values[1])),
                new Location(Double.parseDouble(values[2]), Double.parseDouble(values[3])));
    }

    public static BoundingBox lngLat(String value) {
        String[] values = value.split(",");
        return new BoundingBox(new Location(Double.parseDouble(values[1]), Double.parseDouble(values[0])),
                new Location(Double.parseDouble(values[3]), Double.parseDouble(values[2])));
    }

    public Location getLeftTop() {
        return leftTop;
    }

    public void setLeftTop(Location leftTop) {
        this.leftTop = leftTop;
    }

    public Location getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(Location rightBottom) {
        this.rightBottom = rightBottom;
    }

    /**
     * Lat,Lng
     */
    public Location getOffset(Location center) {
        float lat = rightBottom.getLat() - center.getLat();
        float lng = rightBottom.getLng() - center.getLng();
        return new Location(lat, lng);
    }

    @Override
    public String toString() {
        return leftTop + "," + rightBottom;
    }

    public String latLng() {
        return leftTop.latLng() + "," + leftTop.latLng();
    }

    /**
     * Увеличивает текущие границы, чтобы location тоже попадал в них
     */
    public void addLocation(Location location) {
        float lat = location.getLat();
        float lng = location.getLng();
        if (leftTop == null || rightBottom == null) { //Граница еще не заполнена
            leftTop = Location.latLng(lat, lng);
            rightBottom = Location.latLng(lat, lng);
        } else {
            if (lat < leftTop.getLat()) {
                leftTop.setLat(lat);
            } else if (lat > rightBottom.getLat()) {
                rightBottom.setLat(lat);
            }
            if (lng < leftTop.getLng()) {
                leftTop.setLng(lng);
            } else if (lng > rightBottom.getLng()) {
                rightBottom.setLng(lng);
            }
        }
    }

    public void addBoundingBox(BoundingBox boundingBox) {
        addLocation(boundingBox.getLeftTop());
        addLocation(boundingBox.getRightBottom());
    }

    public Location center() {
        return Location.latLng(leftTop.getLat() + (rightBottom.getLat() - leftTop.getLat()) / 2,
                leftTop.getLng() + (rightBottom.getLng() - leftTop.getLng()) / 2);
    }

    public boolean empty() {
        return leftTop == null || rightBottom == null;
    }

    public boolean inside(Location location) {
        if (empty()) {
            throw new IllegalStateException("Bounding box is empty");
        }
        return leftTop.getLat() <= location.getLat() && leftTop.getLng() <= location.getLng() &&
                rightBottom.getLat() >= location.getLat() && rightBottom.getLng() >= location.getLng();
    }
}
