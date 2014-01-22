package com.obs.mobile_tablet.datamodel.payments;

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
 * Created by david on 1/8/14.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "payments",
        "numberOfAchItems",
        "numberOfWireItems",
        "numberOfBookTransferItems",
        "nextSetUrl"
})

public class GetPaymentsResponse {

        @JsonProperty("payments")
        private List<PaymentEntry> payments = new ArrayList<PaymentEntry>();
        @JsonProperty("numberOfAchItems")
        private Integer numberOfAchItems;
        @JsonProperty("numberOfWireItems")
        private Integer numberOfWireItems;
        @JsonProperty("numberOfBookTransferItems")
        private Integer numberOfBookTransferItems;
        @JsonProperty("nextSetUrl")
        private String nextSetUrl;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("payments")
        public List<PaymentEntry> getPayments() {
            return payments;
        }

        @JsonProperty("payments")
        public void setPayments(List<PaymentEntry> payments) {
            this.payments = payments;
        }

        @JsonProperty("numberOfAchItems")
        public Integer getNumberOfAchItems() {
            return numberOfAchItems;
        }

        @JsonProperty("numberOfAchItems")
        public void setNumberOfAchItems(Integer numberOfAchItems) {
            this.numberOfAchItems = numberOfAchItems;
        }

        @JsonProperty("numberOfWireItems")
        public Integer getNumberOfWireItems() {
            return numberOfWireItems;
        }

        @JsonProperty("numberOfWireItems")
        public void setNumberOfWireItems(Integer numberOfWireItems) {
            this.numberOfWireItems = numberOfWireItems;
        }

        @JsonProperty("numberOfBookTransferItems")
        public Integer getNumberOfBookTransferItems() {
            return numberOfBookTransferItems;
        }

        @JsonProperty("numberOfBookTransferItems")
        public void setNumberOfBookTransferItems(Integer numberOfBookTransferItems) {
            this.numberOfBookTransferItems = numberOfBookTransferItems;
        }

        @JsonProperty("nextSetUrl")
        public String getNextSetUrl() {
            return nextSetUrl;
        }

        @JsonProperty("nextSetUrl")
        public void setNextSetUrl(String nextSetUrl) {
            this.nextSetUrl = nextSetUrl;
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
