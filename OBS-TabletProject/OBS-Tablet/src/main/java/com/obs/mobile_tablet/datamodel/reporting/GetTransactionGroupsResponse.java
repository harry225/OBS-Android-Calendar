package com.obs.mobile_tablet.datamodel.reporting;

import java.util.List;

/**
 * Created by sheenobu on 1/5/14.
 */
public class GetTransactionGroupsResponse {

    private List<TransactionGroup> transactionGroups;


    public List<TransactionGroup> getTransactionGroups() {
        return transactionGroups;
    }

    public void setTransactionGroups(List<TransactionGroup> transactionGroups) {
        this.transactionGroups = transactionGroups;
    }
}
