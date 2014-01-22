package com.obs.mobile_tablet.datamodel.setup;

/**
 * Created by david on 1/1/14.
 */
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "authToken",
        "offset",
        "accessToken",
        "pinFormat",
        "pinDescription"
})
public class ApiSession {

    @JsonProperty("authToken")
    private String authToken;
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("pinFormat")
    private String pinFormat;
    @JsonProperty("pinDescription")
    private String pinDescription;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("authToken")
    public String getAuthToken() {
        return authToken;
    }

    @JsonProperty("authToken")
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @JsonProperty("offset")
    public Integer getOffset() {
        return offset;
    }

    @JsonProperty("offset")
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("accessToken")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("pinFormat")
    public String getPinFormat() {
        return pinFormat;
    }

    @JsonProperty("pinFormat")
    public void setPinFormat(String pinFormat) {
        this.pinFormat = pinFormat;
    }

    @JsonProperty("pinDescription")
    public String getPinDescription() {
        return pinDescription;
    }

    @JsonProperty("pinDescription")
    public void setPinDescription(String pinDescription) {
        this.pinDescription = pinDescription;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

