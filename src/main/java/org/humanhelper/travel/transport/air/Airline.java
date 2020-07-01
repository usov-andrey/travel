package org.humanhelper.travel.transport.air;


import org.humanhelper.travel.transport.TransportCompany;

/**
 * @author Андрей
 * @since 19.12.14
 */
public class Airline extends TransportCompany {

    private String iata;
    private String icao;
    private String callSign;

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

}
