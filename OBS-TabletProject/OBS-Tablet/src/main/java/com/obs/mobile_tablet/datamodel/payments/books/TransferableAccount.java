package com.obs.mobile_tablet.datamodel.payments.books;

/**
 * Created by david on 1/13/14.
 */
public class TransferableAccount {
    private String accountId;
    private String displayName;
    private String displayBalance;
    private String displayCurrency;

    public TransferableAccount() {
    }

    public TransferableAccount(String accountId, String displayName, String displayBalance, String displayCurrency) {
        this.accountId = accountId;
        this.displayName = displayName;
        this.displayBalance = displayBalance;
        this.displayCurrency = displayCurrency;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayBalance() {
        return displayBalance;
    }

    public void setDisplayBalance(String displayBalance) {
        this.displayBalance = displayBalance;
    }

    public String getDisplayCurrency() {
        return displayCurrency;
    }

    public void setDisplayCurrency(String displayCurrency) {
        this.displayCurrency = displayCurrency;
    }
}
