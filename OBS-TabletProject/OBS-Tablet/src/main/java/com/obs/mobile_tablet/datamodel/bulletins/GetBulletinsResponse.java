package com.obs.mobile_tablet.datamodel.bulletins;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by hbray on 1/13/14.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "bulletins",
})

public class GetBulletinsResponse {

    @JsonProperty("bulletins")
    private List<Bulletin> bulletins = new ArrayList<Bulletin>();


    // Public Methods: Getter/Setter
    @JsonProperty("bulletins")
    public List<Bulletin> getBulletins() { return bulletins; }

    @JsonProperty("bulletins")
    public void setBulletins(List<Bulletin> bulletins) { this.bulletins = bulletins; }
}
