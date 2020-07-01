package org.humanhelper.travel.dataprovider.air.skysales.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author Андрей
 * @since 19.05.14
 */
public class MarketInfoList {

    private Map<String, List<Map<String, String>>> marketList;

    public Map<String, List<Map<String, String>>> getMarketList() {
        return marketList;
    }

    @JsonProperty("MarketList")
    public void setMarketList(Map<String, List<Map<String, String>>> marketList) {
        this.marketList = marketList;
    }

    @Override
    public String toString() {
        return "MarketInfoList{" +
                "marketList=" + marketList +
                '}';
    }
}
