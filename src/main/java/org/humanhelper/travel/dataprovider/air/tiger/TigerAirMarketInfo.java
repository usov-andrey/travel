package org.humanhelper.travel.dataprovider.air.tiger;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * @author Андрей
 * @since 29.05.14
 */
public class TigerAirMarketInfo {

    private String key;
    private List<Map<String, String>> values;

    public String getKey() {
        return key;
    }

    @JsonProperty("Key")
    public void setKey(String key) {
        this.key = key;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

    @JsonProperty("Value")
    public void setValues(List<Map<String, String>> values) {
        this.values = values;
    }
}
