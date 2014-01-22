package com.obs.mobile_tablet.datamodel.payments.ach;

import com.obs.mobile_tablet.datamodel.payments.PaymentEntryDetails;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class AchPaymentEntryDetails extends PaymentEntryDetails {
    private String companyEntryDescription;
    private String achCompanyName;
    private String achCompanyIdentifier;
    private String batchType;
    private String uid;
    private String debitCreditType;
    private String displayCreditTotal;
    private String displayDebitTotal;
    private int creditCount;
    private int debitCount;
    private String displayAchCompany;
    private String displayBatchType;


    AchPaymentEntryPayeeRecordContainer payeeRecords;

    public String getCompanyEntryDescription() {
        return companyEntryDescription;
    }

    public void setCompanyEntryDescription(String companyEntryDescription) {
        this.companyEntryDescription = companyEntryDescription;
    }

    public String getAchCompanyName() {
        return achCompanyName;
    }

    public void setAchCompanyName(String achCompanyName) {
        this.achCompanyName = achCompanyName;
    }

    public String getAchCompanyIdentifier() {
        return achCompanyIdentifier;
    }

    public void setAchCompanyIdentifier(String achCompanyIdentifier) {
        this.achCompanyIdentifier = achCompanyIdentifier;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDebitCreditType() {
        return debitCreditType;
    }

    public void setDebitCreditType(String debitCreditType) {
        this.debitCreditType = debitCreditType;
    }

    public String getDisplayCreditTotal() {
        return displayCreditTotal;
    }

    public void setDisplayCreditTotal(String displayCreditTotal) {
        this.displayCreditTotal = displayCreditTotal;
    }

    public String getDisplayDebitTotal() {
        return displayDebitTotal;
    }

    public void setDisplayDebitTotal(String displayDebitTotal) {
        this.displayDebitTotal = displayDebitTotal;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    public int getDebitCount() {
        return debitCount;
    }

    public void setDebitCount(int debitCount) {
        this.debitCount = debitCount;
    }

    public String getDisplayAchCompany() {
        return displayAchCompany;
    }

    public void setDisplayAchCompany(String displayAchCompany) {
        this.displayAchCompany = displayAchCompany;
    }

    public String getDisplayBatchType() {
        return displayBatchType;
    }

    public void setDisplayBatchType(String displayBatchType) {
        this.displayBatchType = displayBatchType;
    }

    public AchPaymentEntryPayeeRecordContainer getPayeeRecords() {
        return payeeRecords;
    }

    public void setPayeeRecords(AchPaymentEntryPayeeRecordContainer payeeRecords) {
        this.payeeRecords = payeeRecords;
    }
}
