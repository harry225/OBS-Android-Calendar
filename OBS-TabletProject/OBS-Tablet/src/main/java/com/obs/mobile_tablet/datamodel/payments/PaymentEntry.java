package com.obs.mobile_tablet.datamodel.payments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 12/27/13.
 */
public class PaymentEntry implements Serializable {
    public enum Type { ACH, WIRE, ACCOUNT_TRANSFER, INT_WIRE, ACH_TAXES}
    public enum StatusCategory {CREATED, PENDING, PROCESSING, COMPLETE}

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")
    public Date paymentDate;

    private String displayStatus;
    private String payeeName;
    private String transactionId;
    private String displayAmount;
    private String displayAccount;
    private String detailsUrl;
    private String editUrl;
    private Type paymentType;
    private StatusCategory statusCategory;

    private Boolean isErrorStatus;
    private PaymentEntryDetails details;
    private PaymentEntryAction availableActions;



    @JsonIgnore
    public BigDecimal amount;

    @JsonIgnore
    public Object account;

    @JsonIgnore
    public String accountId;


//    PaymentEntryAction availableActions

    public PaymentEntry(){}

    public PaymentEntry(Date effectiveDate, String langPakStatusKey, String transactionId, BigDecimal amount, String accountId, Type paymentType, String payeeName){
        this.paymentDate = new Date(effectiveDate.getTime());
        this.displayStatus = langPakStatusKey;
        this.transactionId = transactionId;
        this.amount = amount;
        this.accountId = accountId;
        this.paymentType = paymentType;
        this.payeeName = payeeName;

    }



    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(String displayAmount) {
        this.displayAmount = displayAmount;
    }

    public String getDisplayAccount() {
        return displayAccount;
    }

    public void setDisplayAccount(String displayAccount) {
        this.displayAccount = displayAccount;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }

    public Type getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Type paymentType) {
        this.paymentType = paymentType;
    }

    public StatusCategory getStatusCategory() {
        return statusCategory;
    }

    public void setStatusCategory(StatusCategory statusCategory) {
        this.statusCategory = statusCategory;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Object getAccount() {
        return account;
    }

    public void setAccount(Object account) {
        this.account = account;
    }

    public PaymentEntryDetails getDetails() {
        return details;
    }

    public void setDetails(PaymentEntryDetails details) {
        this.details = details;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public PaymentEntryAction getAvailableActions() {
        return availableActions;
    }

    public void setAvailableActions(PaymentEntryAction availableActions) {
        this.availableActions = availableActions;
    }

    public Boolean getIsErrorStatus() {
        return isErrorStatus;
    }

    public void setIsErrorStatus(Boolean isErrorStatus) {
        this.isErrorStatus = isErrorStatus;
    }
}
