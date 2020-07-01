package org.humanhelper.travel.integration.travelpayouts;

import org.humanhelper.data.bean.name.NameBean;

/**
 * @author Андрей
 * @since 05.09.15
 */
public class TPAirline extends NameBean {

    private String country;
    private String iata;
    private String icao;
    private String callSign;
    private Boolean is_active;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }
}
