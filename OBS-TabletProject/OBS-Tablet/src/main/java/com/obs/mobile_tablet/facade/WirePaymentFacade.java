package com.obs.mobile_tablet.facade;

import com.obs.mobile_tablet.datamodel.payments.wires.GetWireTemplateResponse;
import com.obs.mobile_tablet.datamodel.payments.wires.GetWireTemplatesResponse;
import com.obs.mobile_tablet.datamodel.payments.wires.WireData;
import com.obs.mobile_tablet.datamodel.payments.wires.WirePaymentEntryDetails;
import com.obs.mobile_tablet.rest.Callback;

/**
 * Created by david on 1/17/14.
 */
public class WirePaymentFacade extends OBSFacade{
    @Override
    public WirePaymentFacade fake() {
        restClient.setFAKE_API(true);
        return this;
    }

    /**Template Based Wire Payment Initiation Methods*/

    public void getWireTemplates(final Callback<GetWireTemplatesResponse> callback) {
        //Method: GET
        //Path: /v1/payments/wires/templates
    }

    public void createRequestFromTemplate(final Callback<GetWireTemplateResponse> callback) {
        //Method: GET
        //Path: /v1/payments/wires/templates/{templateId}
    }

    public void reviewWireTransferTemplate(WireData wireData, String verifyUrl, WirePaymentEntryDetails wirePaymentEntryDetails, final Callback<GetWireTemplateResponse> callback) {
        //Method: POST

        //Path: /v1/payments/wires/templates/{templateId}/verify   (The verifyUrl)
    }

    public void submitWireTransferFromTemplate(WireData wireData, String completeUrl, WirePaymentEntryDetails wirePaymentEntryDetails, final Callback<GetWireTemplateResponse> callback) {
        //Method: POST
        //Path: /v1/payments/wires/templates/{templateId} (The completeUrl)
    }
}
