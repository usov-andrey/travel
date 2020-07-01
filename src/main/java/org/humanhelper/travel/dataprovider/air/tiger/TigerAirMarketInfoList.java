package org.humanhelper.travel.dataprovider.air.tiger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author Андрей
 * @since 29.05.14
 */
public class TigerAirMarketInfoList {

    private List<TigerAirMarketInfo> marketList;

    public List<TigerAirMarketInfo> getMarketList() {
        return marketList;
    }

    @JsonProperty("MarketList")
    public void setMarketList(List<TigerAirMarketInfo> marketList) {
        this.marketList = marketList;
    }
}
