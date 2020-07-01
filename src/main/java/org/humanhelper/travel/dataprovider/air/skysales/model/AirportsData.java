package org.humanhelper.travel.dataprovider.air.skysales.model;

import org.humanhelper.service.utils.StringHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Андрей
 * @since 19.05.14
 */
public class AirportsData {
    private LocationInfoList locationInfo;
    private StationInfoList stationInfo;
    private MarketInfoList marketInfo;
    private CountryInfoList countryInfo;
    private Set<String> restrictedStationInfoCategories;

    public AirportsData() {
        restrictedStationInfoCategories = new HashSet<>();
    }

    public LocationInfoList getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfoList locationInfo) {
        this.locationInfo = locationInfo;
    }

    public StationInfoList getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(StationInfoList stationInfo) {
        this.stationInfo = stationInfo;
    }

    public CountryInfoList getCountryInfo() {
        return countryInfo;
    }

    public void setCountryInfo(CountryInfoList countryInfo) {
        this.countryInfo = countryInfo;
    }

    public MarketInfoList getMarketInfo() {
        return marketInfo;
    }

    public void setMarketInfo(MarketInfoList marketInfo) {
        this.marketInfo = marketInfo;
    }

    public void setRestrictedStationInfoCategories(Set<String> restrictedStationInfoCategories) {
        this.restrictedStationInfoCategories = restrictedStationInfoCategories;
    }

    @Override
    public String toString() {
        return "AirAsiaJsonAirportsData{" +
                "locationInfo=" + locationInfo +
                ", stationInfo=" + stationInfo +
                ", marketInfo=" + marketInfo +
                ", countryInfo=" + countryInfo +
                '}';
    }

    public List<Map<String, String>> getTargetList(String sourceCode) {
        return getMarketInfo().getMarketList().get(sourceCode);
    }


    public Set<String> getAirportCodes() {
        Set<String> codes = new HashSet<>();
        for (StationInfo stationInfo : getStationInfo().getStationList()) {
            String category = stationInfo.getStationCategories();
            if ((StringHelper.isEmpty(category) || !restrictedStationInfoCategories.contains(category))
                    && (stationInfo.getAllowed() == null || stationInfo.getAllowed().equals(Boolean.TRUE))) {
                codes.add(stationInfo.getCode());
            }
        }
        return codes;
    }
}
