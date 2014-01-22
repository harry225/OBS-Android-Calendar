package com.obs.mobile_tablet.datamodel.payments.ach;

import java.util.List;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class ApiBatchObj {
    private String effectiveDAte;
    private String templateId;
    private String companyEntryDescription;
    private String offsetAccountId;

    List<PayeeRecord> payeeRecords;

    public String getEffectiveDAte() {
        return effectiveDAte;
    }

    public void setEffectiveDAte(String effectiveDAte) {
        this.effectiveDAte = effectiveDAte;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCompanyEntryDescription() {
        return companyEntryDescription;
    }

    public void setCompanyEntryDescription(String companyEntryDescription) {
        this.companyEntryDescription = companyEntryDescription;
    }

    public String getOffsetAccountId() {
        return offsetAccountId;
    }

    public void setOffsetAccountId(String offsetAccountId) {
        this.offsetAccountId = offsetAccountId;
    }

    public List<PayeeRecord> getPayeeRecords() {
        return payeeRecords;
    }

    public void setPayeeRecords(List<PayeeRecord> payeeRecords) {
        this.payeeRecords = payeeRecords;
    }
}
