
package org.humanhelper.travel.route.provider.air.id.EnTiket;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "flight_id",
        "airlines_master_id",
        "airlines_name",
        "flight_number",
        "flight_date",
        "departure_city",
        "departure_time",
        "departure_timezone",
        "arrival_city",
        "arrival_time",
        "arrival_timezone",
        "class",
        "stop",
        "via",
        "flight_info_json",
        "price_currency",
        "price_value",
        "price_nta",
        "price_adult",
        "price_child",
        "price_infant",
        "fare_info_json",
        "count_adult",
        "count_child",
        "count_infant",
        "timestamp",
        "is_active",
        "hit",
        "fare_sell_key",
        "journey_sell_key",
        "is_airlines_active",
        "airlines_real_name",
        "airlines_short_real_name",
        "has_food",
        "additional_price_IDR",
        "sub_price_IDR",
        "multiplier",
        "check_in_baggage",
        "reseller_sub_price_IDR",
        "airlines_short_real_name_ucwords",
        "is_promo",
        "isPromoClass",
        "isSpecialPrice",
        "is_garuda_executive",
        "airport_tax",
        "formatted_flight_date",
        "simple_departure_time",
        "flight_hour",
        "duration_date",
        "simple_arrival_time",
        "duration_time",
        "duration_hour",
        "duration_minute",
        "closing",
        "departure_time_ts",
        "arrival_time_ts",
        "img_src",
        "img_src_2",
        "img_src_m",
        "img",
        "img_2",
        "img_m",
        "stop_lang",
        "cstop",
        "real_via",
        "long_via",
        "seat_count",
        "flight_info",
        "full_via",
        "price",
        "markup_price_string",
        "markup_price_IDR",
        "isBestDeal",
        "special_subsidy",
        "price_IDR",
        "price_string",
        "price_value_IDR",
        "price_adult_IDR",
        "price_child_IDR",
        "price_infant_IDR",
        "price_value_convert",
        "price_adult_convert",
        "price_child_convert",
        "price_infant_convert",
        "price_value_string",
        "price_adult_string",
        "price_child_string",
        "price_infant_string",
        "total_save_price_string",
        "per_person_save_price_string",
        "tiket_point_reward",
        "tiket_point_value",
        "tiket_point_curr",
        "tiket_point_amount",
        "tiket_point_string",
        "installment"
})
public class Datum {

    @JsonProperty("flight_id")
    private String flightId;
    @JsonProperty("airlines_master_id")
    private String airlinesMasterId;
    @JsonProperty("airlines_name")
    private String airlinesName;
    @JsonProperty("flight_number")
    private String flightNumber;
    @JsonProperty("flight_date")
    private String flightDate;
    @JsonProperty("departure_city")
    private String departureCity;
    @JsonProperty("departure_time")
    private String departureTime;
    @JsonProperty("departure_timezone")
    private String departureTimezone;
    @JsonProperty("arrival_city")
    private String arrivalCity;
    @JsonProperty("arrival_time")
    private String arrivalTime;
    @JsonProperty("arrival_timezone")
    private String arrivalTimezone;
    @JsonProperty("class")
    private String _class;
    @JsonProperty("stop")
    private String stop;
    @JsonProperty("via")
    private String via;
    @JsonProperty("flight_info_json")
    private String flightInfoJson;
    @JsonProperty("price_currency")
    private String priceCurrency;
    @JsonProperty("price_value")
    private Double priceValue;
    @JsonProperty("price_nta")
    private String priceNta;
    @JsonProperty("price_adult")
    private Double priceAdult;
    @JsonProperty("price_child")
    private Integer priceChild;
    @JsonProperty("price_infant")
    private Integer priceInfant;
    @JsonProperty("fare_info_json")
    private Object fareInfoJson;
    @JsonProperty("count_adult")
    private String countAdult;
    @JsonProperty("count_child")
    private String countChild;
    @JsonProperty("count_infant")
    private String countInfant;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("is_active")
    private Integer isActive;
    @JsonProperty("hit")
    private String hit;
    @JsonProperty("fare_sell_key")
    private Object fareSellKey;
    @JsonProperty("journey_sell_key")
    private Object journeySellKey;
    @JsonProperty("is_airlines_active")
    private String isAirlinesActive;
    @JsonProperty("airlines_real_name")
    private String airlinesRealName;
    @JsonProperty("airlines_short_real_name")
    private String airlinesShortRealName;
    @JsonProperty("has_food")
    private String hasFood;
    @JsonProperty("additional_price_IDR")
    private String additionalPriceIDR;
    @JsonProperty("sub_price_IDR")
    private String subPriceIDR;
    @JsonProperty("multiplier")
    private String multiplier;
    @JsonProperty("check_in_baggage")
    private String checkInBaggage;
    @JsonProperty("reseller_sub_price_IDR")
    private Integer resellerSubPriceIDR;
    @JsonProperty("airlines_short_real_name_ucwords")
    private String airlinesShortRealNameUcwords;
    @JsonProperty("is_promo")
    private Integer isPromo;
    @JsonProperty("isPromoClass")
    private Boolean isPromoClass;
    @JsonProperty("isSpecialPrice")
    private Boolean isSpecialPrice;
    @JsonProperty("is_garuda_executive")
    private Boolean isGarudaExecutive;
    @JsonProperty("airport_tax")
    private Boolean airportTax;
    @JsonProperty("formatted_flight_date")
    private String formattedFlightDate;
    @JsonProperty("simple_departure_time")
    private String simpleDepartureTime;
    @JsonProperty("flight_hour")
    private Integer flightHour;
    @JsonProperty("duration_date")
    private Integer durationDate;
    @JsonProperty("simple_arrival_time")
    private String simpleArrivalTime;
    @JsonProperty("duration_time")
    private Integer durationTime;
    @JsonProperty("duration_hour")
    private Integer durationHour;
    @JsonProperty("duration_minute")
    private Integer durationMinute;
    @JsonProperty("closing")
    private Boolean closing;
    @JsonProperty("departure_time_ts")
    private Integer departureTimeTs;
    @JsonProperty("arrival_time_ts")
    private Integer arrivalTimeTs;
    @JsonProperty("img_src")
    private String imgSrc;
    @JsonProperty("img_src_2")
    private String imgSrc2;
    @JsonProperty("img_src_m")
    private String imgSrcM;
    @JsonProperty("img")
    private String img;
    @JsonProperty("img_2")
    private String img2;
    @JsonProperty("img_m")
    private String imgM;
    @JsonProperty("stop_lang")
    private String stopLang;
    @JsonProperty("cstop")
    private Integer cstop;
    @JsonProperty("real_via")
    private String realVia;
    @JsonProperty("long_via")
    private String longVia;
    @JsonProperty("seat_count")
    private Integer seatCount;
    @JsonProperty("flight_info")
    private List<FlightInfo> flightInfo = new ArrayList<FlightInfo>();
    @JsonProperty("full_via")
    private String fullVia;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("markup_price_string")
    private String markupPriceString;
    @JsonProperty("markup_price_IDR")
    private Integer markupPriceIDR;
    @JsonProperty("isBestDeal")
    private Boolean isBestDeal;
    @JsonProperty("special_subsidy")
    private Boolean specialSubsidy;
    @JsonProperty("price_IDR")
    private Integer priceIDR;
    @JsonProperty("price_string")
    private String priceString;
    @JsonProperty("price_value_IDR")
    private Integer priceValueIDR;
    @JsonProperty("price_adult_IDR")
    private Integer priceAdultIDR;
    @JsonProperty("price_child_IDR")
    private Integer priceChildIDR;
    @JsonProperty("price_infant_IDR")
    private Integer priceInfantIDR;
    @JsonProperty("price_value_convert")
    private Double priceValueConvert;
    @JsonProperty("price_adult_convert")
    private Double priceAdultConvert;
    @JsonProperty("price_child_convert")
    private Integer priceChildConvert;
    @JsonProperty("price_infant_convert")
    private Integer priceInfantConvert;
    @JsonProperty("price_value_string")
    private String priceValueString;
    @JsonProperty("price_adult_string")
    private String priceAdultString;
    @JsonProperty("price_child_string")
    private String priceChildString;
    @JsonProperty("price_infant_string")
    private String priceInfantString;
    @JsonProperty("total_save_price_string")
    private String totalSavePriceString;
    @JsonProperty("per_person_save_price_string")
    private String perPersonSavePriceString;
    @JsonProperty("tiket_point_reward")
    private String tiketPointReward;
    @JsonProperty("tiket_point_value")
    private Integer tiketPointValue;
    @JsonProperty("tiket_point_curr")
    private String tiketPointCurr;
    @JsonProperty("tiket_point_amount")
    private Double tiketPointAmount;
    @JsonProperty("tiket_point_string")
    private String tiketPointString;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The flightId
     */
    @JsonProperty("flight_id")
    public String getFlightId() {
        return flightId;
    }

    /**
     * @param flightId The flight_id
     */
    @JsonProperty("flight_id")
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    /**
     * @return The airlinesMasterId
     */
    @JsonProperty("airlines_master_id")
    public String getAirlinesMasterId() {
        return airlinesMasterId;
    }

    /**
     * @param airlinesMasterId The airlines_master_id
     */
    @JsonProperty("airlines_master_id")
    public void setAirlinesMasterId(String airlinesMasterId) {
        this.airlinesMasterId = airlinesMasterId;
    }

    /**
     * @return The airlinesName
     */
    @JsonProperty("airlines_name")
    public String getAirlinesName() {
        return airlinesName;
    }

    /**
     * @param airlinesName The airlines_name
     */
    @JsonProperty("airlines_name")
    public void setAirlinesName(String airlinesName) {
        this.airlinesName = airlinesName;
    }

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
     * @return The flightDate
     */
    @JsonProperty("flight_date")
    public String getFlightDate() {
        return flightDate;
    }

    /**
     * @param flightDate The flight_date
     */
    @JsonProperty("flight_date")
    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
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
     * @return The departureTime
     */
    @JsonProperty("departure_time")
    public String getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime The departure_time
     */
    @JsonProperty("departure_time")
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * @return The departureTimezone
     */
    @JsonProperty("departure_timezone")
    public String getDepartureTimezone() {
        return departureTimezone;
    }

    /**
     * @param departureTimezone The departure_timezone
     */
    @JsonProperty("departure_timezone")
    public void setDepartureTimezone(String departureTimezone) {
        this.departureTimezone = departureTimezone;
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
     * @return The arrivalTime
     */
    @JsonProperty("arrival_time")
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @param arrivalTime The arrival_time
     */
    @JsonProperty("arrival_time")
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * @return The arrivalTimezone
     */
    @JsonProperty("arrival_timezone")
    public String getArrivalTimezone() {
        return arrivalTimezone;
    }

    /**
     * @param arrivalTimezone The arrival_timezone
     */
    @JsonProperty("arrival_timezone")
    public void setArrivalTimezone(String arrivalTimezone) {
        this.arrivalTimezone = arrivalTimezone;
    }

    /**
     * @return The _class
     */
    @JsonProperty("class")
    public String getClass_() {
        return _class;
    }

    /**
     * @param _class The class
     */
    @JsonProperty("class")
    public void setClass_(String _class) {
        this._class = _class;
    }

    /**
     * @return The stop
     */
    @JsonProperty("stop")
    public String getStop() {
        return stop;
    }

    /**
     * @param stop The stop
     */
    @JsonProperty("stop")
    public void setStop(String stop) {
        this.stop = stop;
    }

    /**
     * @return The via
     */
    @JsonProperty("via")
    public String getVia() {
        return via;
    }

    /**
     * @param via The via
     */
    @JsonProperty("via")
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * @return The flightInfoJson
     */
    @JsonProperty("flight_info_json")
    public String getFlightInfoJson() {
        return flightInfoJson;
    }

    /**
     * @param flightInfoJson The flight_info_json
     */
    @JsonProperty("flight_info_json")
    public void setFlightInfoJson(String flightInfoJson) {
        this.flightInfoJson = flightInfoJson;
    }

    /**
     * @return The priceCurrency
     */
    @JsonProperty("price_currency")
    public String getPriceCurrency() {
        return priceCurrency;
    }

    /**
     * @param priceCurrency The price_currency
     */
    @JsonProperty("price_currency")
    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    /**
     * @return The priceValue
     */
    @JsonProperty("price_value")
    public Double getPriceValue() {
        return priceValue;
    }

    /**
     * @param priceValue The price_value
     */
    @JsonProperty("price_value")
    public void setPriceValue(Double priceValue) {
        this.priceValue = priceValue;
    }

    /**
     * @return The priceNta
     */
    @JsonProperty("price_nta")
    public String getPriceNta() {
        return priceNta;
    }

    /**
     * @param priceNta The price_nta
     */
    @JsonProperty("price_nta")
    public void setPriceNta(String priceNta) {
        this.priceNta = priceNta;
    }

    /**
     * @return The priceAdult
     */
    @JsonProperty("price_adult")
    public Double getPriceAdult() {
        return priceAdult;
    }

    /**
     * @param priceAdult The price_adult
     */
    @JsonProperty("price_adult")
    public void setPriceAdult(Double priceAdult) {
        this.priceAdult = priceAdult;
    }

    /**
     * @return The priceChild
     */
    @JsonProperty("price_child")
    public Integer getPriceChild() {
        return priceChild;
    }

    /**
     * @param priceChild The price_child
     */
    @JsonProperty("price_child")
    public void setPriceChild(Integer priceChild) {
        this.priceChild = priceChild;
    }

    /**
     * @return The priceInfant
     */
    @JsonProperty("price_infant")
    public Integer getPriceInfant() {
        return priceInfant;
    }

    /**
     * @param priceInfant The price_infant
     */
    @JsonProperty("price_infant")
    public void setPriceInfant(Integer priceInfant) {
        this.priceInfant = priceInfant;
    }

    /**
     * @return The fareInfoJson
     */
    @JsonProperty("fare_info_json")
    public Object getFareInfoJson() {
        return fareInfoJson;
    }

    /**
     * @param fareInfoJson The fare_info_json
     */
    @JsonProperty("fare_info_json")
    public void setFareInfoJson(Object fareInfoJson) {
        this.fareInfoJson = fareInfoJson;
    }

    /**
     * @return The countAdult
     */
    @JsonProperty("count_adult")
    public String getCountAdult() {
        return countAdult;
    }

    /**
     * @param countAdult The count_adult
     */
    @JsonProperty("count_adult")
    public void setCountAdult(String countAdult) {
        this.countAdult = countAdult;
    }

    /**
     * @return The countChild
     */
    @JsonProperty("count_child")
    public String getCountChild() {
        return countChild;
    }

    /**
     * @param countChild The count_child
     */
    @JsonProperty("count_child")
    public void setCountChild(String countChild) {
        this.countChild = countChild;
    }

    /**
     * @return The countInfant
     */
    @JsonProperty("count_infant")
    public String getCountInfant() {
        return countInfant;
    }

    /**
     * @param countInfant The count_infant
     */
    @JsonProperty("count_infant")
    public void setCountInfant(String countInfant) {
        this.countInfant = countInfant;
    }

    /**
     * @return The timestamp
     */
    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp The timestamp
     */
    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return The isActive
     */
    @JsonProperty("is_active")
    public Integer getIsActive() {
        return isActive;
    }

    /**
     * @param isActive The is_active
     */
    @JsonProperty("is_active")
    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    /**
     * @return The hit
     */
    @JsonProperty("hit")
    public String getHit() {
        return hit;
    }

    /**
     * @param hit The hit
     */
    @JsonProperty("hit")
    public void setHit(String hit) {
        this.hit = hit;
    }

    /**
     * @return The fareSellKey
     */
    @JsonProperty("fare_sell_key")
    public Object getFareSellKey() {
        return fareSellKey;
    }

    /**
     * @param fareSellKey The fare_sell_key
     */
    @JsonProperty("fare_sell_key")
    public void setFareSellKey(Object fareSellKey) {
        this.fareSellKey = fareSellKey;
    }

    /**
     * @return The journeySellKey
     */
    @JsonProperty("journey_sell_key")
    public Object getJourneySellKey() {
        return journeySellKey;
    }

    /**
     * @param journeySellKey The journey_sell_key
     */
    @JsonProperty("journey_sell_key")
    public void setJourneySellKey(Object journeySellKey) {
        this.journeySellKey = journeySellKey;
    }

    /**
     * @return The isAirlinesActive
     */
    @JsonProperty("is_airlines_active")
    public String getIsAirlinesActive() {
        return isAirlinesActive;
    }

    /**
     * @param isAirlinesActive The is_airlines_active
     */
    @JsonProperty("is_airlines_active")
    public void setIsAirlinesActive(String isAirlinesActive) {
        this.isAirlinesActive = isAirlinesActive;
    }

    /**
     * @return The airlinesRealName
     */
    @JsonProperty("airlines_real_name")
    public String getAirlinesRealName() {
        return airlinesRealName;
    }

    /**
     * @param airlinesRealName The airlines_real_name
     */
    @JsonProperty("airlines_real_name")
    public void setAirlinesRealName(String airlinesRealName) {
        this.airlinesRealName = airlinesRealName;
    }

    /**
     * @return The airlinesShortRealName
     */
    @JsonProperty("airlines_short_real_name")
    public String getAirlinesShortRealName() {
        return airlinesShortRealName;
    }

    /**
     * @param airlinesShortRealName The airlines_short_real_name
     */
    @JsonProperty("airlines_short_real_name")
    public void setAirlinesShortRealName(String airlinesShortRealName) {
        this.airlinesShortRealName = airlinesShortRealName;
    }

    /**
     * @return The hasFood
     */
    @JsonProperty("has_food")
    public String getHasFood() {
        return hasFood;
    }

    /**
     * @param hasFood The has_food
     */
    @JsonProperty("has_food")
    public void setHasFood(String hasFood) {
        this.hasFood = hasFood;
    }

    /**
     * @return The additionalPriceIDR
     */
    @JsonProperty("additional_price_IDR")
    public String getAdditionalPriceIDR() {
        return additionalPriceIDR;
    }

    /**
     * @param additionalPriceIDR The additional_price_IDR
     */
    @JsonProperty("additional_price_IDR")
    public void setAdditionalPriceIDR(String additionalPriceIDR) {
        this.additionalPriceIDR = additionalPriceIDR;
    }

    /**
     * @return The subPriceIDR
     */
    @JsonProperty("sub_price_IDR")
    public String getSubPriceIDR() {
        return subPriceIDR;
    }

    /**
     * @param subPriceIDR The sub_price_IDR
     */
    @JsonProperty("sub_price_IDR")
    public void setSubPriceIDR(String subPriceIDR) {
        this.subPriceIDR = subPriceIDR;
    }

    /**
     * @return The multiplier
     */
    @JsonProperty("multiplier")
    public String getMultiplier() {
        return multiplier;
    }

    /**
     * @param multiplier The multiplier
     */
    @JsonProperty("multiplier")
    public void setMultiplier(String multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * @return The checkInBaggage
     */
    @JsonProperty("check_in_baggage")
    public String getCheckInBaggage() {
        return checkInBaggage;
    }

    /**
     * @param checkInBaggage The check_in_baggage
     */
    @JsonProperty("check_in_baggage")
    public void setCheckInBaggage(String checkInBaggage) {
        this.checkInBaggage = checkInBaggage;
    }

    /**
     * @return The resellerSubPriceIDR
     */
    @JsonProperty("reseller_sub_price_IDR")
    public Integer getResellerSubPriceIDR() {
        return resellerSubPriceIDR;
    }

    /**
     * @param resellerSubPriceIDR The reseller_sub_price_IDR
     */
    @JsonProperty("reseller_sub_price_IDR")
    public void setResellerSubPriceIDR(Integer resellerSubPriceIDR) {
        this.resellerSubPriceIDR = resellerSubPriceIDR;
    }

    /**
     * @return The airlinesShortRealNameUcwords
     */
    @JsonProperty("airlines_short_real_name_ucwords")
    public String getAirlinesShortRealNameUcwords() {
        return airlinesShortRealNameUcwords;
    }

    /**
     * @param airlinesShortRealNameUcwords The airlines_short_real_name_ucwords
     */
    @JsonProperty("airlines_short_real_name_ucwords")
    public void setAirlinesShortRealNameUcwords(String airlinesShortRealNameUcwords) {
        this.airlinesShortRealNameUcwords = airlinesShortRealNameUcwords;
    }

    /**
     * @return The isPromo
     */
    @JsonProperty("is_promo")
    public Integer getIsPromo() {
        return isPromo;
    }

    /**
     * @param isPromo The is_promo
     */
    @JsonProperty("is_promo")
    public void setIsPromo(Integer isPromo) {
        this.isPromo = isPromo;
    }

    /**
     * @return The isPromoClass
     */
    @JsonProperty("isPromoClass")
    public Boolean getIsPromoClass() {
        return isPromoClass;
    }

    /**
     * @param isPromoClass The isPromoClass
     */
    @JsonProperty("isPromoClass")
    public void setIsPromoClass(Boolean isPromoClass) {
        this.isPromoClass = isPromoClass;
    }

    /**
     * @return The isSpecialPrice
     */
    @JsonProperty("isSpecialPrice")
    public Boolean getIsSpecialPrice() {
        return isSpecialPrice;
    }

    /**
     * @param isSpecialPrice The isSpecialPrice
     */
    @JsonProperty("isSpecialPrice")
    public void setIsSpecialPrice(Boolean isSpecialPrice) {
        this.isSpecialPrice = isSpecialPrice;
    }

    /**
     * @return The isGarudaExecutive
     */
    @JsonProperty("is_garuda_executive")
    public Boolean getIsGarudaExecutive() {
        return isGarudaExecutive;
    }

    /**
     * @param isGarudaExecutive The is_garuda_executive
     */
    @JsonProperty("is_garuda_executive")
    public void setIsGarudaExecutive(Boolean isGarudaExecutive) {
        this.isGarudaExecutive = isGarudaExecutive;
    }

    /**
     * @return The airportTax
     */
    @JsonProperty("airport_tax")
    public Boolean getAirportTax() {
        return airportTax;
    }

    /**
     * @param airportTax The airport_tax
     */
    @JsonProperty("airport_tax")
    public void setAirportTax(Boolean airportTax) {
        this.airportTax = airportTax;
    }

    /**
     * @return The formattedFlightDate
     */
    @JsonProperty("formatted_flight_date")
    public String getFormattedFlightDate() {
        return formattedFlightDate;
    }

    /**
     * @param formattedFlightDate The formatted_flight_date
     */
    @JsonProperty("formatted_flight_date")
    public void setFormattedFlightDate(String formattedFlightDate) {
        this.formattedFlightDate = formattedFlightDate;
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
     * @return The flightHour
     */
    @JsonProperty("flight_hour")
    public Integer getFlightHour() {
        return flightHour;
    }

    /**
     * @param flightHour The flight_hour
     */
    @JsonProperty("flight_hour")
    public void setFlightHour(Integer flightHour) {
        this.flightHour = flightHour;
    }

    /**
     * @return The durationDate
     */
    @JsonProperty("duration_date")
    public Integer getDurationDate() {
        return durationDate;
    }

    /**
     * @param durationDate The duration_date
     */
    @JsonProperty("duration_date")
    public void setDurationDate(Integer durationDate) {
        this.durationDate = durationDate;
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

    /**
     * @return The durationTime
     */
    @JsonProperty("duration_time")
    public Integer getDurationTime() {
        return durationTime;
    }

    /**
     * @param durationTime The duration_time
     */
    @JsonProperty("duration_time")
    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    /**
     * @return The durationHour
     */
    @JsonProperty("duration_hour")
    public Integer getDurationHour() {
        return durationHour;
    }

    /**
     * @param durationHour The duration_hour
     */
    @JsonProperty("duration_hour")
    public void setDurationHour(Integer durationHour) {
        this.durationHour = durationHour;
    }

    /**
     * @return The durationMinute
     */
    @JsonProperty("duration_minute")
    public Integer getDurationMinute() {
        return durationMinute;
    }

    /**
     * @param durationMinute The duration_minute
     */
    @JsonProperty("duration_minute")
    public void setDurationMinute(Integer durationMinute) {
        this.durationMinute = durationMinute;
    }

    /**
     * @return The closing
     */
    @JsonProperty("closing")
    public Boolean getClosing() {
        return closing;
    }

    /**
     * @param closing The closing
     */
    @JsonProperty("closing")
    public void setClosing(Boolean closing) {
        this.closing = closing;
    }

    /**
     * @return The departureTimeTs
     */
    @JsonProperty("departure_time_ts")
    public Integer getDepartureTimeTs() {
        return departureTimeTs;
    }

    /**
     * @param departureTimeTs The departure_time_ts
     */
    @JsonProperty("departure_time_ts")
    public void setDepartureTimeTs(Integer departureTimeTs) {
        this.departureTimeTs = departureTimeTs;
    }

    /**
     * @return The arrivalTimeTs
     */
    @JsonProperty("arrival_time_ts")
    public Integer getArrivalTimeTs() {
        return arrivalTimeTs;
    }

    /**
     * @param arrivalTimeTs The arrival_time_ts
     */
    @JsonProperty("arrival_time_ts")
    public void setArrivalTimeTs(Integer arrivalTimeTs) {
        this.arrivalTimeTs = arrivalTimeTs;
    }

    /**
     * @return The imgSrc
     */
    @JsonProperty("img_src")
    public String getImgSrc() {
        return imgSrc;
    }

    /**
     * @param imgSrc The img_src
     */
    @JsonProperty("img_src")
    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    /**
     * @return The imgSrc2
     */
    @JsonProperty("img_src_2")
    public String getImgSrc2() {
        return imgSrc2;
    }

    /**
     * @param imgSrc2 The img_src_2
     */
    @JsonProperty("img_src_2")
    public void setImgSrc2(String imgSrc2) {
        this.imgSrc2 = imgSrc2;
    }

    /**
     * @return The imgSrcM
     */
    @JsonProperty("img_src_m")
    public String getImgSrcM() {
        return imgSrcM;
    }

    /**
     * @param imgSrcM The img_src_m
     */
    @JsonProperty("img_src_m")
    public void setImgSrcM(String imgSrcM) {
        this.imgSrcM = imgSrcM;
    }

    /**
     * @return The img
     */
    @JsonProperty("img")
    public String getImg() {
        return img;
    }

    /**
     * @param img The img
     */
    @JsonProperty("img")
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return The img2
     */
    @JsonProperty("img_2")
    public String getImg2() {
        return img2;
    }

    /**
     * @param img2 The img_2
     */
    @JsonProperty("img_2")
    public void setImg2(String img2) {
        this.img2 = img2;
    }

    /**
     * @return The imgM
     */
    @JsonProperty("img_m")
    public String getImgM() {
        return imgM;
    }

    /**
     * @param imgM The img_m
     */
    @JsonProperty("img_m")
    public void setImgM(String imgM) {
        this.imgM = imgM;
    }

    /**
     * @return The stopLang
     */
    @JsonProperty("stop_lang")
    public String getStopLang() {
        return stopLang;
    }

    /**
     * @param stopLang The stop_lang
     */
    @JsonProperty("stop_lang")
    public void setStopLang(String stopLang) {
        this.stopLang = stopLang;
    }

    /**
     * @return The cstop
     */
    @JsonProperty("cstop")
    public Integer getCstop() {
        return cstop;
    }

    /**
     * @param cstop The cstop
     */
    @JsonProperty("cstop")
    public void setCstop(Integer cstop) {
        this.cstop = cstop;
    }

    /**
     * @return The realVia
     */
    @JsonProperty("real_via")
    public String getRealVia() {
        return realVia;
    }

    /**
     * @param realVia The real_via
     */
    @JsonProperty("real_via")
    public void setRealVia(String realVia) {
        this.realVia = realVia;
    }

    /**
     * @return The longVia
     */
    @JsonProperty("long_via")
    public String getLongVia() {
        return longVia;
    }

    /**
     * @param longVia The long_via
     */
    @JsonProperty("long_via")
    public void setLongVia(String longVia) {
        this.longVia = longVia;
    }

    /**
     * @return The seatCount
     */
    @JsonProperty("seat_count")
    public Integer getSeatCount() {
        return seatCount;
    }

    /**
     * @param seatCount The seat_count
     */
    @JsonProperty("seat_count")
    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }

    /**
     * @return The flightInfo
     */
    @JsonProperty("flight_info")
    public List<FlightInfo> getFlightInfo() {
        return flightInfo;
    }

    /**
     * @param flightInfo The flight_info
     */
    @JsonProperty("flight_info")
    public void setFlightInfo(List<FlightInfo> flightInfo) {
        this.flightInfo = flightInfo;
    }

    /**
     * @return The fullVia
     */
    @JsonProperty("full_via")
    public String getFullVia() {
        return fullVia;
    }

    /**
     * @param fullVia The full_via
     */
    @JsonProperty("full_via")
    public void setFullVia(String fullVia) {
        this.fullVia = fullVia;
    }

    /**
     * @return The price
     */
    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    /**
     * @param price The price
     */
    @JsonProperty("price")
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return The markupPriceString
     */
    @JsonProperty("markup_price_string")
    public String getMarkupPriceString() {
        return markupPriceString;
    }

    /**
     * @param markupPriceString The markup_price_string
     */
    @JsonProperty("markup_price_string")
    public void setMarkupPriceString(String markupPriceString) {
        this.markupPriceString = markupPriceString;
    }

    /**
     * @return The markupPriceIDR
     */
    @JsonProperty("markup_price_IDR")
    public Integer getMarkupPriceIDR() {
        return markupPriceIDR;
    }

    /**
     * @param markupPriceIDR The markup_price_IDR
     */
    @JsonProperty("markup_price_IDR")
    public void setMarkupPriceIDR(Integer markupPriceIDR) {
        this.markupPriceIDR = markupPriceIDR;
    }

    /**
     * @return The isBestDeal
     */
    @JsonProperty("isBestDeal")
    public Boolean getIsBestDeal() {
        return isBestDeal;
    }

    /**
     * @param isBestDeal The isBestDeal
     */
    @JsonProperty("isBestDeal")
    public void setIsBestDeal(Boolean isBestDeal) {
        this.isBestDeal = isBestDeal;
    }

    /**
     * @return The specialSubsidy
     */
    @JsonProperty("special_subsidy")
    public Boolean getSpecialSubsidy() {
        return specialSubsidy;
    }

    /**
     * @param specialSubsidy The special_subsidy
     */
    @JsonProperty("special_subsidy")
    public void setSpecialSubsidy(Boolean specialSubsidy) {
        this.specialSubsidy = specialSubsidy;
    }

    /**
     * @return The priceIDR
     */
    @JsonProperty("price_IDR")
    public Integer getPriceIDR() {
        return priceIDR;
    }

    /**
     * @param priceIDR The price_IDR
     */
    @JsonProperty("price_IDR")
    public void setPriceIDR(Integer priceIDR) {
        this.priceIDR = priceIDR;
    }

    /**
     * @return The priceString
     */
    @JsonProperty("price_string")
    public String getPriceString() {
        return priceString;
    }

    /**
     * @param priceString The price_string
     */
    @JsonProperty("price_string")
    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    /**
     * @return The priceValueIDR
     */
    @JsonProperty("price_value_IDR")
    public Integer getPriceValueIDR() {
        return priceValueIDR;
    }

    /**
     * @param priceValueIDR The price_value_IDR
     */
    @JsonProperty("price_value_IDR")
    public void setPriceValueIDR(Integer priceValueIDR) {
        this.priceValueIDR = priceValueIDR;
    }

    /**
     * @return The priceAdultIDR
     */
    @JsonProperty("price_adult_IDR")
    public Integer getPriceAdultIDR() {
        return priceAdultIDR;
    }

    /**
     * @param priceAdultIDR The price_adult_IDR
     */
    @JsonProperty("price_adult_IDR")
    public void setPriceAdultIDR(Integer priceAdultIDR) {
        this.priceAdultIDR = priceAdultIDR;
    }

    /**
     * @return The priceChildIDR
     */
    @JsonProperty("price_child_IDR")
    public Integer getPriceChildIDR() {
        return priceChildIDR;
    }

    /**
     * @param priceChildIDR The price_child_IDR
     */
    @JsonProperty("price_child_IDR")
    public void setPriceChildIDR(Integer priceChildIDR) {
        this.priceChildIDR = priceChildIDR;
    }

    /**
     * @return The priceInfantIDR
     */
    @JsonProperty("price_infant_IDR")
    public Integer getPriceInfantIDR() {
        return priceInfantIDR;
    }

    /**
     * @param priceInfantIDR The price_infant_IDR
     */
    @JsonProperty("price_infant_IDR")
    public void setPriceInfantIDR(Integer priceInfantIDR) {
        this.priceInfantIDR = priceInfantIDR;
    }

    /**
     * @return The priceValueConvert
     */
    @JsonProperty("price_value_convert")
    public Double getPriceValueConvert() {
        return priceValueConvert;
    }

    /**
     * @param priceValueConvert The price_value_convert
     */
    @JsonProperty("price_value_convert")
    public void setPriceValueConvert(Double priceValueConvert) {
        this.priceValueConvert = priceValueConvert;
    }

    /**
     * @return The priceAdultConvert
     */
    @JsonProperty("price_adult_convert")
    public Double getPriceAdultConvert() {
        return priceAdultConvert;
    }

    /**
     * @param priceAdultConvert The price_adult_convert
     */
    @JsonProperty("price_adult_convert")
    public void setPriceAdultConvert(Double priceAdultConvert) {
        this.priceAdultConvert = priceAdultConvert;
    }

    /**
     * @return The priceChildConvert
     */
    @JsonProperty("price_child_convert")
    public Integer getPriceChildConvert() {
        return priceChildConvert;
    }

    /**
     * @param priceChildConvert The price_child_convert
     */
    @JsonProperty("price_child_convert")
    public void setPriceChildConvert(Integer priceChildConvert) {
        this.priceChildConvert = priceChildConvert;
    }

    /**
     * @return The priceInfantConvert
     */
    @JsonProperty("price_infant_convert")
    public Integer getPriceInfantConvert() {
        return priceInfantConvert;
    }

    /**
     * @param priceInfantConvert The price_infant_convert
     */
    @JsonProperty("price_infant_convert")
    public void setPriceInfantConvert(Integer priceInfantConvert) {
        this.priceInfantConvert = priceInfantConvert;
    }

    /**
     * @return The priceValueString
     */
    @JsonProperty("price_value_string")
    public String getPriceValueString() {
        return priceValueString;
    }

    /**
     * @param priceValueString The price_value_string
     */
    @JsonProperty("price_value_string")
    public void setPriceValueString(String priceValueString) {
        this.priceValueString = priceValueString;
    }

    /**
     * @return The priceAdultString
     */
    @JsonProperty("price_adult_string")
    public String getPriceAdultString() {
        return priceAdultString;
    }

    /**
     * @param priceAdultString The price_adult_string
     */
    @JsonProperty("price_adult_string")
    public void setPriceAdultString(String priceAdultString) {
        this.priceAdultString = priceAdultString;
    }

    /**
     * @return The priceChildString
     */
    @JsonProperty("price_child_string")
    public String getPriceChildString() {
        return priceChildString;
    }

    /**
     * @param priceChildString The price_child_string
     */
    @JsonProperty("price_child_string")
    public void setPriceChildString(String priceChildString) {
        this.priceChildString = priceChildString;
    }

    /**
     * @return The priceInfantString
     */
    @JsonProperty("price_infant_string")
    public String getPriceInfantString() {
        return priceInfantString;
    }

    /**
     * @param priceInfantString The price_infant_string
     */
    @JsonProperty("price_infant_string")
    public void setPriceInfantString(String priceInfantString) {
        this.priceInfantString = priceInfantString;
    }

    /**
     * @return The totalSavePriceString
     */
    @JsonProperty("total_save_price_string")
    public String getTotalSavePriceString() {
        return totalSavePriceString;
    }

    /**
     * @param totalSavePriceString The total_save_price_string
     */
    @JsonProperty("total_save_price_string")
    public void setTotalSavePriceString(String totalSavePriceString) {
        this.totalSavePriceString = totalSavePriceString;
    }

    /**
     * @return The perPersonSavePriceString
     */
    @JsonProperty("per_person_save_price_string")
    public String getPerPersonSavePriceString() {
        return perPersonSavePriceString;
    }

    /**
     * @param perPersonSavePriceString The per_person_save_price_string
     */
    @JsonProperty("per_person_save_price_string")
    public void setPerPersonSavePriceString(String perPersonSavePriceString) {
        this.perPersonSavePriceString = perPersonSavePriceString;
    }

    /**
     * @return The tiketPointReward
     */
    @JsonProperty("tiket_point_reward")
    public String getTiketPointReward() {
        return tiketPointReward;
    }

    /**
     * @param tiketPointReward The tiket_point_reward
     */
    @JsonProperty("tiket_point_reward")
    public void setTiketPointReward(String tiketPointReward) {
        this.tiketPointReward = tiketPointReward;
    }

    /**
     * @return The tiketPointValue
     */
    @JsonProperty("tiket_point_value")
    public Integer getTiketPointValue() {
        return tiketPointValue;
    }

    /**
     * @param tiketPointValue The tiket_point_value
     */
    @JsonProperty("tiket_point_value")
    public void setTiketPointValue(Integer tiketPointValue) {
        this.tiketPointValue = tiketPointValue;
    }

    /**
     * @return The tiketPointCurr
     */
    @JsonProperty("tiket_point_curr")
    public String getTiketPointCurr() {
        return tiketPointCurr;
    }

    /**
     * @param tiketPointCurr The tiket_point_curr
     */
    @JsonProperty("tiket_point_curr")
    public void setTiketPointCurr(String tiketPointCurr) {
        this.tiketPointCurr = tiketPointCurr;
    }

    /**
     * @return The tiketPointAmount
     */
    @JsonProperty("tiket_point_amount")
    public Double getTiketPointAmount() {
        return tiketPointAmount;
    }

    /**
     * @param tiketPointAmount The tiket_point_amount
     */
    @JsonProperty("tiket_point_amount")
    public void setTiketPointAmount(Double tiketPointAmount) {
        this.tiketPointAmount = tiketPointAmount;
    }

    /**
     * @return The tiketPointString
     */
    @JsonProperty("tiket_point_string")
    public String getTiketPointString() {
        return tiketPointString;
    }

    /**
     * @param tiketPointString The tiket_point_string
     */
    @JsonProperty("tiket_point_string")
    public void setTiketPointString(String tiketPointString) {
        this.tiketPointString = tiketPointString;
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
