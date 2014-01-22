package com.obs.mobile_tablet.datamodel.securemessages;

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
        "messages",
})

public class GetSecureMessagesResponse {

    @JsonProperty("messages")
    private List<SecureMessage> messages = new ArrayList<SecureMessage>();


    // Public Methods: Getter/Setter
    @JsonProperty("messages")
    public List<SecureMessage> getMessages() { return messages; }

    @JsonProperty("messages")
    public void setMessages(List<SecureMessage> messages) { this.messages = messages; }
}
