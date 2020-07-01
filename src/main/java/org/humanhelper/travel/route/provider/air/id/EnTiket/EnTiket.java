
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
        "code",
        "data",
        "airlines"
})
public class EnTiket {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("data")
    private List<Datum> data = new ArrayList<Datum>();
    @JsonProperty("airlines")
    private String airlines;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The code
     */
    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return The data
     */
    @JsonProperty("data")
    public List<Datum> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    @JsonProperty("data")
    public void setData(List<Datum> data) {
        this.data = data;
    }

    /**
     * @return The airlines
     */
    @JsonProperty("airlines")
    public String getAirlines() {
        return airlines;
    }

    /**
     * @param airlines The airlines
     */
    @JsonProperty("airlines")
    public void setAirlines(String airlines) {
        this.airlines = airlines;
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
