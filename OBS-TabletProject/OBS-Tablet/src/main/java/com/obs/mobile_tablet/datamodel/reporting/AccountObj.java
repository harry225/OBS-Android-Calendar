package com.obs.mobile_tablet.datamodel.reporting;

import java.util.Collection;

/**
 * Created by david on 12/27/13.
 */
public class AccountObj {
    public String name;
    public String displayName;
    public String accountNumber;
    public String type;
    public String abaNumber;
    public String accountId;
    public String companyAccountId;
    public String companyId;
    public String abaId;
    public String bankId;
    public Collection<BalanceObj> balances;
    public TransactionsObj transactionDetails;
    public Collection<PaymentThumbnailObj> recentPayments;
    public Collection<GraphObj> graphs;
    public Collection<BalanceObj> historicalBalances;
    public String accountUrl;

    public AccountObj() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbaNumber() {
        return abaNumber;
    }

    public void setAbaNumber(String abaNumber) {
        this.abaNumber = abaNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCompanyAccountId() {
        return companyAccountId;
    }

    public void setCompanyAccountId(String companyAccountId) {
        this.companyAccountId = companyAccountId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAbaId() {
        return abaId;
    }

    public void setAbaId(String abaId) {
        this.abaId = abaId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Collection<BalanceObj> getBalances() {
        return balances;
    }

    public void setBalances(Collection<BalanceObj> balances) {
        this.balances = balances;
    }

    public TransactionsObj getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionsObj transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public Collection<PaymentThumbnailObj> getRecentPayments() {
        return recentPayments;
    }

    public void setRecentPayments(Collection<PaymentThumbnailObj> recentPayments) {
        this.recentPayments = recentPayments;
    }

    public Collection<GraphObj> getGraphs() {
        return graphs;
    }

    public void setGraphs(Collection<GraphObj> graphs) {
        this.graphs = graphs;
    }

    public Collection<BalanceObj> getHistoricalBalances() {
        return historicalBalances;
    }

    public void setHistoricalBalances(Collection<BalanceObj> historicalBalances) {
        this.historicalBalances = historicalBalances;
    }

    public String getAccountUrl() {
        return accountUrl;
    }

    public void setAccountUrl(String accountUrl) {
        this.accountUrl = accountUrl;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
