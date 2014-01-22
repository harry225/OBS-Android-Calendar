package com.obs.mobile_tablet.datamodel.payments.books;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class TransferItemRecord {
    private String uid;
    private String debitAccountId;
    private String displayDebitAccount;
    private String creditAccountId;
    private String displayCreditAccount;
    private String amount;
    private String memo;
    private Boolean principalOnly;
    private String transactionId;
    private Boolean valid;

    public TransferItemRecord(String debitAccountId, String creditAccountId, String amount, String memo) {
        this.debitAccountId = debitAccountId;
        this.creditAccountId = creditAccountId;
        this.amount = amount;
        this.memo = memo;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getPrincipalOnly() {

        return principalOnly;
    }

    public void setPrincipalOnly(Boolean principalOnly) {
        this.principalOnly = principalOnly;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(String debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public String getDisplayDebitAccount() {
        return displayDebitAccount;
    }

    public void setDisplayDebitAccount(String displayDebitAccount) {
        this.displayDebitAccount = displayDebitAccount;
    }

    public String getCreditAccountId() {
        return creditAccountId;
    }

    public void setCreditAccountId(String creditAccountId) {
        this.creditAccountId = creditAccountId;
    }

    public String getDisplayCreditAccount() {
        return displayCreditAccount;
    }

    public void setDisplayCreditAccount(String displayCreditAccount) {
        this.displayCreditAccount = displayCreditAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
