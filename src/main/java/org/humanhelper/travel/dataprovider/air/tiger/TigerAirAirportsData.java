package org.humanhelper.travel.dataprovider.air.tiger;

import org.humanhelper.travel.dataprovider.air.skysales.model.AirportsData;

import java.util.List;
import java.util.Map;

/**
 * @author Андрей
 * @since 29.05.14
 */
public class TigerAirAirportsData extends AirportsData {

    private TigerAirMarketInfoList marketInfoList;

    public void setMarketInfoList(TigerAirMarketInfoList marketInfoList) {
        this.marketInfoList = marketInfoList;
    }

    @Override
    public List<Map<String, String>> getTargetList(String sourceCode) {
        for (TigerAirMarketInfo marketInfo : marketInfoList.getMarketList()) {
            if (marketInfo.getKey().equals(sourceCode)) {
                return marketInfo.getValues();
            }
        }
        return null;
    }
}
