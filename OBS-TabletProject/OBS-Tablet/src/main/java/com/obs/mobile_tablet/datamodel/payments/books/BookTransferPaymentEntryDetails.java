package com.obs.mobile_tablet.datamodel.payments.books;

import com.obs.mobile_tablet.datamodel.payments.PaymentEntryDetails;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class BookTransferPaymentEntryDetails extends PaymentEntryDetails {
    private TransferItemContainer transfers;
    private String requestTransactionId;

    public TransferItemContainer getTransfers() {
        return transfers;
    }

    public void setTransfers(TransferItemContainer transfers) {
        this.transfers = transfers;
    }

    public String getRequestTransactionId() {
        return requestTransactionId;
    }

    public void setRequestTransactionId(String requestTransactionId) {
        this.requestTransactionId = requestTransactionId;
    }
}
