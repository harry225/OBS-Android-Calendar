package com.obs.mobile_tablet.datamodel.reporting;

import com.obs.mobile_tablet.datamodel.payments.PaymentEntry;

import java.util.Date;

/**
 * Created by david on 12/27/13.
 */
public class PaymentThumbnailObj {
    public String type;
    public String amount;

    public Date paymentDate;
    public String companyAccountId;
    public String transactionId;
    public PaymentEntry.StatusCategory statusCategory;
    boolean isErrorStatus;

    boolean getIsErrorStatus() {
        return isErrorStatus;
    }

    void setIsErrorStatus(boolean errorStatus) {
        isErrorStatus = errorStatus;
    }
}
