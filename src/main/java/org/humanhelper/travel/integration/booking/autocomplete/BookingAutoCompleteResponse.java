package org.humanhelper.travel.integration.booking.autocomplete;

import java.util.List;

/**
 * @author Андрей
 * @since 11.09.15
 */
public class BookingAutoCompleteResponse {

    private String __did_you_mean__;

    private List<BookingAutoCompleteEntry> city;

    public List<BookingAutoCompleteEntry> getCity() {
        return city;
    }

    public void setCity(List<BookingAutoCompleteEntry> city) {
        this.city = city;
    }

    public String get__did_you_mean__() {
        return __did_you_mean__;
    }

    public void set__did_you_mean__(String __did_you_mean__) {
        this.__did_you_mean__ = __did_you_mean__;
    }
}
