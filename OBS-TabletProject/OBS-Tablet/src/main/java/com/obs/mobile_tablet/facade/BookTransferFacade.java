package com.obs.mobile_tablet.facade;

import com.obs.mobile_tablet.datamodel.payments.books.BookTransferSetup;
import com.obs.mobile_tablet.datamodel.payments.books.TransferItemContainer;
import com.obs.mobile_tablet.rest.Callback;

import java.util.Map;

/**
 * Created by david on 1/13/14.
 */
public class BookTransferFacade extends OBSFacade{

    @Override
    public BookTransferFacade fake() {
        restClient.setFAKE_API(true);
        return this;
    }

    /**Book Transfer Initiation Methods*/

//    public void setupEditRequest()

    public void setup(final Callback<BookTransferSetup> callback) {
        restClient.get("payments/booktransfer/setup", null, Map.class, new Callback<Map<String, Map<String, String>>>() {

            @Override
            public void onTaskCompleted(Map<String, Map<String, String>> result) {
                BookTransferSetup bookTransferSetup = new BookTransferSetup(result);
                callback.onTaskCompleted(bookTransferSetup);
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason);
            }
        });
    }

    public void reviewNewTransfer(TransferItemContainer transferItemContainer, final Callback<TransferItemContainer> callback) {
        restClient.post("payments/booktransfer/verify", transferItemContainer, TransferItemContainer.class, new Callback<TransferItemContainer>() {

            @Override
            public void onTaskCompleted(TransferItemContainer result) {
                callback.onTaskCompleted(result);
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason);
            }
        });
    }

    public void commitNewTransfer(TransferItemContainer transferItemContainer, final Callback<TransferItemContainer> callback) {
        restClient.post(transferItemContainer.getCompleteUrl(), transferItemContainer, TransferItemContainer.class, new Callback<TransferItemContainer>() {

            @Override
            public void onTaskCompleted(TransferItemContainer result) {

            }

            @Override
            public void onTaskFailure(String Reason) {

            }
        });
    }

}
