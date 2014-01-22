package com.obs.mobile_tablet.datamodel.payments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.Date;

/**
 * Created by stevenguitar on 1/7/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentEntryDetails {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")
    private Date processingDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")
    private Date creationDate;
    private String templateName;
    private long auditTimestamp;
    private long index;
    private String displayFrequency;
    private String seriesName;

    Collection<StatusHistoryItem> statusHistory;

    public Date getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public long getAuditTimestamp() {
        return auditTimestamp;
    }

    public void setAuditTimestamp(long auditTimestamp) {
        this.auditTimestamp = auditTimestamp;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getDisplayFrequency() {
        return displayFrequency;
    }

    public void setDisplayFrequency(String displayFrequency) {
        this.displayFrequency = displayFrequency;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public Collection<StatusHistoryItem> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(Collection<StatusHistoryItem> statusHistory) {
        this.statusHistory = statusHistory;
    }
}
