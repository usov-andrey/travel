
package org.humanhelper.travel.route.provider.air.id.EnTiket;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "flight_number",
        "departure_city",
        "arrival_city",
        "simple_departure_time",
        "simple_arrival_time"
})
public class FlightInfo {

    @JsonProperty("flight_number")
    private String flightNumber;
    @JsonProperty("departure_city")
    private String departureCity;
    @JsonProperty("arrival_city")
    private String arrivalCity;
    @JsonProperty("simple_departure_time")
    private String simpleDepartureTime;
    @JsonProperty("simple_arrival_time")
    private String simpleArrivalTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The flightNumber
     */
    @JsonProperty("flight_number")
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * @param flightNumber The flight_number
     */
    @JsonProperty("flight_number")
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * @return The departureCity
     */
    @JsonProperty("departure_city")
    public String getDepartureCity() {
        return departureCity;
    }

    /**
     * @param departureCity The departure_city
     */
    @JsonProperty("departure_city")
    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    /**
     * @return The arrivalCity
     */
    @JsonProperty("arrival_city")
    public String getArrivalCity() {
        return arrivalCity;
    }

    /**
     * @param arrivalCity The arrival_city
     */
    @JsonProperty("arrival_city")
    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    /**
     * @return The simpleDepartureTime
     */
    @JsonProperty("simple_departure_time")
    public String getSimpleDepartureTime() {
        return simpleDepartureTime;
    }

    /**
     * @param simpleDepartureTime The simple_departure_time
     */
    @JsonProperty("simple_departure_time")
    public void setSimpleDepartureTime(String simpleDepartureTime) {
        this.simpleDepartureTime = simpleDepartureTime;
    }

    /**
     * @return The simpleArrivalTime
     */
    @JsonProperty("simple_arrival_time")
    public String getSimpleArrivalTime() {
        return simpleArrivalTime;
    }

    /**
     * @param simpleArrivalTime The simple_arrival_time
     */
    @JsonProperty("simple_arrival_time")
    public void setSimpleArrivalTime(String simpleArrivalTime) {
        this.simpleArrivalTime = simpleArrivalTime;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
