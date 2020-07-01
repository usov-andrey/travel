package org.humanhelper.travel.integration.travelpayouts;

/**
 * @author Андрей
 * @since 08.01.15
 */
public class TPPlace extends TPName {

    private TPCoordinates coordinates;
    private String time_zone;

    public TPCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(TPCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    @Override
    public String toString() {
        return "TPPlace{" +
                super.toString() +
                ",coordinates=" + coordinates +
                ", time_zone='" + time_zone + '\'' +
                '}';
    }
}
