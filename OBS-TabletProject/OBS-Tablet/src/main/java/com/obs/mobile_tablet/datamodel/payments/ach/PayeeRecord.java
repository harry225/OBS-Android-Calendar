package com.obs.mobile_tablet.datamodel.payments.ach;

import java.math.BigDecimal;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class PayeeRecord {
    public enum RecordType { TEMPLATE, REQUEST, BATCH }
    public enum AchDebitCreditType { CREDIT, DEBIT, BOTH }

    private String uid;
    private String payeeId;
    private String payeeAccountId;
    private BigDecimal amount;
    private String addenda;
    private String emailAddress;
    private String templateRecordId;
    private String createdFrom;
    private boolean disabled;
    private boolean anonymous;
    private RecordType type;
    private AchDebitCreditType debitCreditType;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeAccountId() {
        return payeeAccountId;
    }

    public void setPayeeAccountId(String payeeAccountId) {
        this.payeeAccountId = payeeAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddenda() {
        return addenda;
    }

    public void setAddenda(String addenda) {
        this.addenda = addenda;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTemplateRecordId() {
        return templateRecordId;
    }

    public void setTemplateRecordId(String templateRecordId) {
        this.templateRecordId = templateRecordId;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public RecordType getType() {
        return type;
    }

    public void setType(RecordType type) {
        this.type = type;
    }

    public AchDebitCreditType getDebitCreditType() {
        return debitCreditType;
    }

    public void setDebitCreditType(AchDebitCreditType debitCreditType) {
        this.debitCreditType = debitCreditType;
    }
}
