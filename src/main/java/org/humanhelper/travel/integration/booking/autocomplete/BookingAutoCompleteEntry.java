package org.humanhelper.travel.integration.booking.autocomplete;

import org.humanhelper.travel.integration.booking.BookingCity;

import java.util.List;

/**
 * "cc1":"th","city_ufi":null,"dest_id":"-3414440","dest_type":"city","genius_hotels":"212","hotels":"1340","label":"Bangkok, Bangkok Province, Thailand","label_highlighted":"<b class='search_hl_name'>Bangkok</b>, Bangkok Province, Thailand","label_multiline":"<span><b class='search_hl_name'>Bangkok</b></span> Bangkok Province, Thailand","labels":[{"hl":1,"required":1,"text":"Bangkok","type":"city"},{"required":1,"text":"Bangkok Province","type":"region"},{"required":1,"text":"Thailand","type":"country"}],"lc":"en","nr_hotels":"1340","nr_hotels_25":"1482","nr_hotels_label":"1340 properties","region_id":"3921","rtl":0,"type":"ci"
 *
 * @author Андрей
 * @since 11.09.15
 */
public class BookingAutoCompleteEntry extends BookingCity {

    private String cc1;
    private String dest_id;
    private String dest_type;
    private String label;
    private String region_id;
    private List<BookingAutoCompleteLabel> labels;

    public String getDest_id() {
        return dest_id;
    }

    public void setDest_id(String dest_id) {
        this.dest_id = dest_id;
    }

    public String getDest_type() {
        return dest_type;
    }

    public void setDest_type(String dest_type) {
        this.dest_type = dest_type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public List<BookingAutoCompleteLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<BookingAutoCompleteLabel> labels) {
        this.labels = labels;
    }

    public String getCc1() {
        return cc1;
    }

    public void setCc1(String cc1) {
        this.cc1 = cc1;
    }

    @Override
    public String toString() {
        return "BookingAutoCompleteEntry{" +
                "cc1='" + cc1 + '\'' +
                ", dest_id='" + dest_id + '\'' +
                ", dest_type='" + dest_type + '\'' +
                ", label='" + label + '\'' +
                ", region_id='" + region_id + '\'' +
                ", labels=" + labels +
                '}';
    }
}
