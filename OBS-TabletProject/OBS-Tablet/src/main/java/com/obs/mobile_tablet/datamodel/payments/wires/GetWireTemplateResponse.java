package com.obs.mobile_tablet.datamodel.payments.wires;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by david on 1/17/14.
 */
public class GetWireTemplateResponse {

    private WirePaymentEntryDetails wireData;
    private String completeUrl;
    private String verifyUrl;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")
    private Date minPaymentDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")
    private Date maxPaymentDate;

    public GetWireTemplateResponse() {

    }

    public WirePaymentEntryDetails getWireData() {
        return wireData;
    }

    public String getCompleteUrl() {
        return completeUrl;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public Date getMinPaymentDate() {
        return minPaymentDate;
    }

    public Date getMaxPaymentDate() {
        return maxPaymentDate;
    }
}
