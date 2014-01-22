package com.obs.mobile_tablet.datamodel.payments.books;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class TransferItemContainer {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")
    private Date effectiveDate;
    private Boolean valid;

    List<TransferItemRecord> transfers;
    int numberOfTransferItems = 0;

    @JsonIgnore
    private String completeUrl;

    public TransferItemContainer(){
        transfers = new ArrayList<TransferItemRecord>();
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<TransferItemRecord> getTransfers() {
        return transfers;
    }

    public void setTransfers(ArrayList<TransferItemRecord> transfers) {
        this.transfers = transfers;
    }

    public int getNumberOfTransferItems() {
        return numberOfTransferItems;
    }

    public void setNumberOfTransferItems(int numberOfTransferItems) {
        this.numberOfTransferItems = numberOfTransferItems;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @JsonIgnore
    public String getCompleteUrl() {
        return completeUrl;
    }

    @JsonProperty
    public void setCompleteUrl(String completeUrl) {
        this.completeUrl = completeUrl;
    }
}
