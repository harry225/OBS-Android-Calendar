package com.obs.mobile_tablet.datamodel.payments.ach;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class AchPaymentEntryPayeeRecordContainer {
    private List<AchPaymentEntryPayeeRecord> payeeRecords;
    private int numberOfPayeeRecords = 0;
    private String nextSetUrl;

    public AchPaymentEntryPayeeRecordContainer(){
        payeeRecords = new ArrayList<AchPaymentEntryPayeeRecord>();
    }

    public List<AchPaymentEntryPayeeRecord> getPayeeRecords() {
        return payeeRecords;
    }

    public void setPayeeRecords(List<AchPaymentEntryPayeeRecord> payeeRecords) {
        this.payeeRecords = payeeRecords;
    }

    public int getNumberOfPayeeRecords() {
        return numberOfPayeeRecords;
    }

    public void setNumberOfPayeeRecords(int numberOfPayeeRecords) {
        this.numberOfPayeeRecords = numberOfPayeeRecords;
    }

    public String getNextSetUrl() {
        return nextSetUrl;
    }

    public void setNextSetUrl(String nextSetUrl) {
        this.nextSetUrl = nextSetUrl;
    }
}
