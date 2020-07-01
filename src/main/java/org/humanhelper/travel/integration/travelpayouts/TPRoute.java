package org.humanhelper.travel.integration.travelpayouts;

import java.util.List;

/**
 * {"airline_iata":"2B","airline_icao":null,"departure_airport_iata":"AER","departure_airport_icao":null,
 * "arrival_airport_iata":"DME","arrival_airport_icao":null,"codeshare":false,"transfers":0,"planes":["CR2"]}
 *
 * @author Андрей
 * @since 05.09.15
 */
public class TPRoute {

    private String airline_iata;
    private String airline_icao;
    private String departure_airport_iata;
    private String arrival_airport_iata;
    private String arrival_airport_icao;
    private Boolean codeshare;
    private Integer transfers;
    private List<String> planes;

    public String getAirline_iata() {
        return airline_iata;
    }

    public void setAirline_iata(String airline_iata) {
        this.airline_iata = airline_iata;
    }

    public String getAirline_icao() {
        return airline_icao;
    }

    public void setAirline_icao(String airline_icao) {
        this.airline_icao = airline_icao;
    }

    public String getDeparture_airport_iata() {
        return departure_airport_iata;
    }

    public void setDeparture_airport_iata(String departure_airport_iata) {
        this.departure_airport_iata = departure_airport_iata;
    }

    public String getArrival_airport_iata() {
        return arrival_airport_iata;
    }

    public void setArrival_airport_iata(String arrival_airport_iata) {
        this.arrival_airport_iata = arrival_airport_iata;
    }

    public String getArrival_airport_icao() {
        return arrival_airport_icao;
    }

    public void setArrival_airport_icao(String arrival_airport_icao) {
        this.arrival_airport_icao = arrival_airport_icao;
    }

    public Boolean getCodeshare() {
        return codeshare;
    }

    public void setCodeshare(Boolean codeshare) {
        this.codeshare = codeshare;
    }

    public Integer getTransfers() {
        return transfers;
    }

    public void setTransfers(Integer transfers) {
        this.transfers = transfers;
    }

    public List<String> getPlanes() {
        return planes;
    }

    public void setPlanes(List<String> planes) {
        this.planes = planes;
    }

    @Override
    public String toString() {
        return "TPRoute{" +
                "airline_iata='" + airline_iata + '\'' +
                ", airline_icao='" + airline_icao + '\'' +
                ", departure_airport_iata='" + departure_airport_iata + '\'' +
                ", arrival_airport_iata='" + arrival_airport_iata + '\'' +
                ", arrival_airport_icao='" + arrival_airport_icao + '\'' +
                ", codeshare=" + codeshare +
                ", transfers=" + transfers +
                ", planes=" + planes +
                '}';
    }
}
