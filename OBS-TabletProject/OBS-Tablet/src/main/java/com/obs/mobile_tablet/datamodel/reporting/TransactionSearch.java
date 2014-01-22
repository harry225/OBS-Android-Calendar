package com.obs.mobile_tablet.datamodel.reporting;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 12/27/13.
 */
public class TransactionSearch {
    public Date fromDate;
    public Date toDate;

    public String accountId;
    public String description;
    public String minCheckNumber;
    public String maxCheckNumber;
    public String checkNumber;

    public boolean allTransactionGroups;
    public Collection transactionGroups;

    public BigDecimal minAmount;
    public BigDecimal maxAmount;

    public TransactionSearch() {
    }

    public TransactionSearch(String minCheckNumber, String maxCheckNumber, String checkNumber, boolean allTransactionGroups, Collection transactionGroups, BigDecimal minAmount, BigDecimal maxAmount, String accountId, Date toDate, Date fromDate) {
        this.minCheckNumber = minCheckNumber;
        this.maxCheckNumber = maxCheckNumber;
        this.checkNumber = checkNumber;
        this.allTransactionGroups = allTransactionGroups;
        this.transactionGroups = transactionGroups;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.accountId = accountId;
        this.toDate = toDate;
        this.fromDate = fromDate;
    }
    public Map<String, Object> toMap() {
        Map<String, Object> query = new HashMap<String, Object>();
        if (minCheckNumber != null) {
            query.put("minCheckNumber",minAmount);
        }
        if (maxCheckNumber != null) {
            query.put("maxCheckNumber",maxCheckNumber);
        }
        if (checkNumber != null) {
            query.put("checkNumber",checkNumber);
        }
        if (transactionGroups != null) {
            query.put("transactionGroups",transactionGroups);
        }
        if (minAmount != null) {
            query.put("minAmount",minAmount);
        }
        if (maxAmount != null) {
            query.put("maxAmount",maxAmount);
        }
        if (accountId != null) {
            query.put("accountId",accountId);
        }
        if (toDate != null) {
            query.put("toDate",toDate);
        }
        if (fromDate != null) {
            query.put("fromDate",fromDate);
        }
        query.put("allTransactionGroups",allTransactionGroups);

        return query;
    }
}
