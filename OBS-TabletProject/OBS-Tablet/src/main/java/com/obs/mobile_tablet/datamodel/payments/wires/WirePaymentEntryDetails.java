package com.obs.mobile_tablet.datamodel.payments.wires;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.obs.mobile_tablet.datamodel.payments.PaymentEntryDetails;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class WirePaymentEntryDetails extends PaymentEntryDetails {
    public enum LockFieldsMode {
        REPETITIVE,
        SEMIREPETITIVE,
        FREEFORM
    }


    private BigDecimal exchangeRate;
    private String displayExchangeRate;
    private BigDecimal fxPaymentAmount;
    private String displayFxPaymentAmount;
    private BigDecimal debitAmount;

    private String uid;
    private String transactionId;
    private String templateId;
    private String templateName;
    private String name;
    private String payeeId;
    private String payeeAccountId;

    private String debitAccount;
    private String debitAccountId;
    private String amount;

    private String beneficiaryId;
    private String displayBeneficiaryId;
    private String beneficiaryIdType;
    private String displayBeneficiaryIdType;
    private String beneficiaryBankId;
    private String beneficiaryBankIdType;
    private String displayBeneficiaryBankIdType;

    private String confirmationNumber;
    private String currency;

    private String receivingRoutingType;
    private String receivingRoutingInfo;
    private String receivingInstitutionName;

    private String intermediaryRoutingType;
    private String intermediaryRoutingInfo;
    private String intermediaryInstitutionName;

    private String originatorName;
    private String beneficiaryName;
    private String beneficiaryBankName;

    private String originatorAddress1;
    private String originatorAddress2;
    private String originatorAddress3;

    private String beneficiaryAddress1;
    private String beneficiaryAddress2;
    private String beneficiaryAddress3;

    private String beneficiaryBankAddress1;
    private String beneficiaryBankAddress2;
    private String beneficiaryBankAddress3;

    private String beneficiaryInformation;
    private String beneficiaryBankInformation;
    private String referenceToBeneficiary;

    Date lastUsed;

    boolean manuallyEnteredInternationalBeneficiaryBank;

    String fxCurrencyCode;
    String encryptedExchangeRate;
    boolean isAmountInForex = true;
    boolean displayInFxPerUsd = true;
    String contractNumber;

    String masterTemplateId;

    // Controller-specific for facade passthrough.
    LockFieldsMode lockFieldsMode;

    // Payee creation fields
    String payeeNickname;
    String payeeType;
    String payeeAccountType;
    String payeeIdentifier;

    boolean international;
    //ONLY USE FOR TEMPLATES
    boolean usdInternational;

    LockFieldsMode templateType;
    int numberOfActivations = 0;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")
    Date firingDate;

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getDisplayExchangeRate() {
        return displayExchangeRate;
    }

    public void setDisplayExchangeRate(String displayExchangeRate) {
        this.displayExchangeRate = displayExchangeRate;
    }

    public BigDecimal getFxPaymentAmount() {
        return fxPaymentAmount;
    }

    public void setFxPaymentAmount(BigDecimal fxPaymentAmount) {
        this.fxPaymentAmount = fxPaymentAmount;
    }

    public String getDisplayFxPaymentAmount() {
        return displayFxPaymentAmount;
    }

    public void setDisplayFxPaymentAmount(String displayFxPaymentAmount) {
        this.displayFxPaymentAmount = displayFxPaymentAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(String debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getDisplayBeneficiaryId() {
        return displayBeneficiaryId;
    }

    public void setDisplayBeneficiaryId(String displayBeneficiaryId) {
        this.displayBeneficiaryId = displayBeneficiaryId;
    }

    public String getBeneficiaryIdType() {
        return beneficiaryIdType;
    }

    public void setBeneficiaryIdType(String beneficiaryIdType) {
        this.beneficiaryIdType = beneficiaryIdType;
    }

    public String getDisplayBeneficiaryIdType() {
        return displayBeneficiaryIdType;
    }

    public void setDisplayBeneficiaryIdType(String displayBeneficiaryIdType) {
        this.displayBeneficiaryIdType = displayBeneficiaryIdType;
    }

    public String getBeneficiaryBankId() {
        return beneficiaryBankId;
    }

    public void setBeneficiaryBankId(String beneficiaryBankId) {
        this.beneficiaryBankId = beneficiaryBankId;
    }

    public String getBeneficiaryBankIdType() {
        return beneficiaryBankIdType;
    }

    public void setBeneficiaryBankIdType(String beneficiaryBankIdType) {
        this.beneficiaryBankIdType = beneficiaryBankIdType;
    }

    public String getDisplayBeneficiaryBankIdType() {
        return displayBeneficiaryBankIdType;
    }

    public void setDisplayBeneficiaryBankIdType(String displayBeneficiaryBankIdType) {
        this.displayBeneficiaryBankIdType = displayBeneficiaryBankIdType;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReceivingRoutingType() {
        return receivingRoutingType;
    }

    public void setReceivingRoutingType(String receivingRoutingType) {
        this.receivingRoutingType = receivingRoutingType;
    }

    public String getReceivingRoutingInfo() {
        return receivingRoutingInfo;
    }

    public void setReceivingRoutingInfo(String receivingRoutingInfo) {
        this.receivingRoutingInfo = receivingRoutingInfo;
    }

    public String getReceivingInstitutionName() {
        return receivingInstitutionName;
    }

    public void setReceivingInstitutionName(String receivingInstitutionName) {
        this.receivingInstitutionName = receivingInstitutionName;
    }

    public String getIntermediaryRoutingType() {
        return intermediaryRoutingType;
    }

    public void setIntermediaryRoutingType(String intermediaryRoutingType) {
        this.intermediaryRoutingType = intermediaryRoutingType;
    }

    public String getIntermediaryRoutingInfo() {
        return intermediaryRoutingInfo;
    }

    public void setIntermediaryRoutingInfo(String intermediaryRoutingInfo) {
        this.intermediaryRoutingInfo = intermediaryRoutingInfo;
    }

    public String getIntermediaryInstitutionName() {
        return intermediaryInstitutionName;
    }

    public void setIntermediaryInstitutionName(String intermediaryInstitutionName) {
        this.intermediaryInstitutionName = intermediaryInstitutionName;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        this.beneficiaryBankName = beneficiaryBankName;
    }

    public String getOriginatorAddress1() {
        return originatorAddress1;
    }

    public void setOriginatorAddress1(String originatorAddress1) {
        this.originatorAddress1 = originatorAddress1;
    }

    public String getOriginatorAddress2() {
        return originatorAddress2;
    }

    public void setOriginatorAddress2(String originatorAddress2) {
        this.originatorAddress2 = originatorAddress2;
    }

    public String getOriginatorAddress3() {
        return originatorAddress3;
    }

    public void setOriginatorAddress3(String originatorAddress3) {
        this.originatorAddress3 = originatorAddress3;
    }

    public String getBeneficiaryAddress1() {
        return beneficiaryAddress1;
    }

    public void setBeneficiaryAddress1(String beneficiaryAddress1) {
        this.beneficiaryAddress1 = beneficiaryAddress1;
    }

    public String getBeneficiaryAddress2() {
        return beneficiaryAddress2;
    }

    public void setBeneficiaryAddress2(String beneficiaryAddress2) {
        this.beneficiaryAddress2 = beneficiaryAddress2;
    }

    public String getBeneficiaryAddress3() {
        return beneficiaryAddress3;
    }

    public void setBeneficiaryAddress3(String beneficiaryAddress3) {
        this.beneficiaryAddress3 = beneficiaryAddress3;
    }

    public String getBeneficiaryBankAddress1() {
        return beneficiaryBankAddress1;
    }

    public void setBeneficiaryBankAddress1(String beneficiaryBankAddress1) {
        this.beneficiaryBankAddress1 = beneficiaryBankAddress1;
    }

    public String getBeneficiaryBankAddress2() {
        return beneficiaryBankAddress2;
    }

    public void setBeneficiaryBankAddress2(String beneficiaryBankAddress2) {
        this.beneficiaryBankAddress2 = beneficiaryBankAddress2;
    }

    public String getBeneficiaryBankAddress3() {
        return beneficiaryBankAddress3;
    }

    public void setBeneficiaryBankAddress3(String beneficiaryBankAddress3) {
        this.beneficiaryBankAddress3 = beneficiaryBankAddress3;
    }

    public String getBeneficiaryInformation() {
        return beneficiaryInformation;
    }

    public void setBeneficiaryInformation(String beneficiaryInformation) {
        this.beneficiaryInformation = beneficiaryInformation;
    }

    public String getBeneficiaryBankInformation() {
        return beneficiaryBankInformation;
    }

    public void setBeneficiaryBankInformation(String beneficiaryBankInformation) {
        this.beneficiaryBankInformation = beneficiaryBankInformation;
    }

    public String getReferenceToBeneficiary() {
        return referenceToBeneficiary;
    }

    public void setReferenceToBeneficiary(String referenceToBeneficiary) {
        this.referenceToBeneficiary = referenceToBeneficiary;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public boolean isManuallyEnteredInternationalBeneficiaryBank() {
        return manuallyEnteredInternationalBeneficiaryBank;
    }

    public void setManuallyEnteredInternationalBeneficiaryBank(boolean manuallyEnteredInternationalBeneficiaryBank) {
        this.manuallyEnteredInternationalBeneficiaryBank = manuallyEnteredInternationalBeneficiaryBank;
    }

    public String getFxCurrencyCode() {
        return fxCurrencyCode;
    }

    public void setFxCurrencyCode(String fxCurrencyCode) {
        this.fxCurrencyCode = fxCurrencyCode;
    }

    public String getEncryptedExchangeRate() {
        return encryptedExchangeRate;
    }

    public void setEncryptedExchangeRate(String encryptedExchangeRate) {
        this.encryptedExchangeRate = encryptedExchangeRate;
    }

    public boolean isAmountInForex() {
        return isAmountInForex;
    }

    public void setAmountInForex(boolean isAmountInForex) {
        this.isAmountInForex = isAmountInForex;
    }

    public boolean isDisplayInFxPerUsd() {
        return displayInFxPerUsd;
    }

    public void setDisplayInFxPerUsd(boolean displayInFxPerUsd) {
        this.displayInFxPerUsd = displayInFxPerUsd;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getMasterTemplateId() {
        return masterTemplateId;
    }

    public void setMasterTemplateId(String masterTemplateId) {
        this.masterTemplateId = masterTemplateId;
    }

    public LockFieldsMode getLockFieldsMode() {
        return lockFieldsMode;
    }

    public void setLockFieldsMode(LockFieldsMode lockFieldsMode) {
        this.lockFieldsMode = lockFieldsMode;
    }

    public String getPayeeNickname() {
        return payeeNickname;
    }

    public void setPayeeNickname(String payeeNickname) {
        this.payeeNickname = payeeNickname;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getPayeeIdentifier() {
        return payeeIdentifier;
    }

    public void setPayeeIdentifier(String payeeIdentifier) {
        this.payeeIdentifier = payeeIdentifier;
    }

    public boolean isInternational() {
        return international;
    }

    public void setInternational(boolean international) {
        this.international = international;
    }

    public boolean isUsdInternational() {
        return usdInternational;
    }

    public void setUsdInternational(boolean usdInternational) {
        this.usdInternational = usdInternational;
    }

    public LockFieldsMode getTemplateType() {
        return templateType;
    }

    public void setTemplateType(LockFieldsMode templateType) {
        this.templateType = templateType;
    }

    public int getNumberOfActivations() {
        return numberOfActivations;
    }

    public void setNumberOfActivations(int numberOfActivations) {
        this.numberOfActivations = numberOfActivations;
    }

    public Date getFiringDate() {
        return firingDate;
    }

    public void setFiringDate(Date firingDate) {
        this.firingDate = firingDate;
    }
}
