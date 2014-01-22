package com.obs.mobile_tablet.facade;

import android.app.ListActivity;

import javax.inject.Inject;

import com.obs.mobile_tablet.datamodel.payments.GetPaymentsResponse;
import com.obs.mobile_tablet.datamodel.payments.PaymentEntry;
import com.obs.mobile_tablet.datamodel.payments.PaymentEntryDetails;
import com.obs.mobile_tablet.datamodel.payments.ach.AchPaymentEntryDetails;
import com.obs.mobile_tablet.datamodel.payments.books.BookTransferPaymentEntryDetails;
import com.obs.mobile_tablet.datamodel.payments.wires.WirePaymentEntryDetails;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.rest.RestClient;

import com.obs.mobile_tablet.datamodel.reporting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by david on 1/8/14.
 */
public class PaymentReportingFacade extends OBSFacade{

    @Override
    public OBSFacade fake() {
        restClient.setFAKE_API(true);
        return this;
    }

    public enum PaymentRequestType { RECENT, FUTURE}

    @Inject
    RestClient restClient;

    public void getPayments(PaymentRequestType type, Callback<GetPaymentsResponse> callback) {

        restClient.get("payments?type="+type,null,GetPaymentsResponse.class,callback);
    }



    public void getPayments(PaymentRequestType type, int year, int month, int day, Callback<GetPaymentsResponse> callback) {
        restClient.get("payments/year/"+year+"/month/"+month+"/day/"+day+"?type="+type,null, GetPaymentsResponse.class, callback);
    }

    public void getPayments(PaymentRequestType type, int year, int month, Callback<GetPaymentsResponse> callback) {
        restClient.get("payments/year/"+year+"/month/"+month+"?type="+type,null, GetPaymentsResponse.class, callback);
    }

    public void getPaymentDetails(final PaymentEntry paymentEntry, final Callback<PaymentEntry> callback) {
        PaymentEntryDetails details;
        switch (paymentEntry.getPaymentType()){
            case ACCOUNT_TRANSFER:
                details = new BookTransferPaymentEntryDetails();
                break;
            case ACH:
                details = new AchPaymentEntryDetails();
                break;
            case ACH_TAXES:
                details = new AchPaymentEntryDetails();
                break;
            case WIRE:
                details = new WirePaymentEntryDetails();
                break;
            case INT_WIRE:
                details = new WirePaymentEntryDetails();
                break;
            default:
                details = new PaymentEntryDetails();
        }

        restClient.get(paymentEntry.getDetailsUrl(),null, paymentEntry.getClass(), details.getClass(),"details",new Callback<PaymentEntry>() {

            @Override
            public void onTaskCompleted(PaymentEntry result) {
                callback.onTaskCompleted(result);
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason);

            }
        });
    }
}
