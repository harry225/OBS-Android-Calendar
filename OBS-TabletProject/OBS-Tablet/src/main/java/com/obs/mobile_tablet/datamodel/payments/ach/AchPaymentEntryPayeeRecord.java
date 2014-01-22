package com.obs.mobile_tablet.datamodel.payments.ach;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class AchPaymentEntryPayeeRecord {
    private boolean disabled;
    private String uid;
    private String payeeUid;
    private String payeeId;
    private String payeeName;
    private String payeeAccountId;
    private String aba;
    private String amount;
    private String account;
    private String templateRecordId;
    private boolean anonymous;
    private String debitCreditType;
    private String addenda;
    private String prenote;
    private boolean blockedByPrenote;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPayeeUid() {
        return payeeUid;
    }

    public void setPayeeUid(String payeeUid) {
        this.payeeUid = payeeUid;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAccountId() {
        return payeeAccountId;
    }

    public void setPayeeAccountId(String payeeAccountId) {
        this.payeeAccountId = payeeAccountId;
    }

    public String getAba() {
        return aba;
    }

    public void setAba(String aba) {
        this.aba = aba;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTemplateRecordId() {
        return templateRecordId;
    }

    public void setTemplateRecordId(String templateRecordId) {
        this.templateRecordId = templateRecordId;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getDebitCreditType() {
        return debitCreditType;
    }

    public void setDebitCreditType(String debitCreditType) {
        this.debitCreditType = debitCreditType;
    }

    public String getAddenda() {
        return addenda;
    }

    public void setAddenda(String addenda) {
        this.addenda = addenda;
    }

    public String getPrenote() {
        return prenote;
    }

    public void setPrenote(String prenote) {
        this.prenote = prenote;
    }

    public boolean isBlockedByPrenote() {
        return blockedByPrenote;
    }

    public void setBlockedByPrenote(boolean blockedByPrenote) {
        this.blockedByPrenote = blockedByPrenote;
    }
}
