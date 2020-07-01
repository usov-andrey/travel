package org.humanhelper.travel.dataprovider.air.skysales.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Андрей
 * @since 19.05.14
 */
public class StationInfoList {

    private List<StationInfo> StationList;

    public List<StationInfo> getStationList() {
        return StationList;
    }

    @JsonProperty("StationList")
    public void setStationList(List<StationInfo> stationList) {
        StationList = stationList;
    }

    public StationInfo getByCode(String stationCode) {
        for (StationInfo stationInfo : StationList) {
            if (stationInfo.getCode().equals(stationCode)) {
                return stationInfo;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "StationInfoList{" +
                "StationList=" + StationList +
                '}';
    }
}
