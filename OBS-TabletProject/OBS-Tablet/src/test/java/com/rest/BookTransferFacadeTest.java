package com.rest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.obs.mobile_tablet.TestOBSApplication;
import com.obs.mobile_tablet.datamodel.payments.GetPaymentsResponse;
import com.obs.mobile_tablet.datamodel.payments.PaymentEntry;
import com.obs.mobile_tablet.datamodel.payments.books.BookTransferSetup;
import com.obs.mobile_tablet.datamodel.payments.books.TransferItemContainer;
import com.obs.mobile_tablet.datamodel.payments.books.TransferItemRecord;
import com.obs.mobile_tablet.datamodel.payments.books.TransferableAccount;
import com.obs.mobile_tablet.facade.AuthenticationFacade;
import com.obs.mobile_tablet.facade.BookTransferFacade;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.facade.PaymentReportingFacade;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.Logger;
import com.obs.mobile_tablet.wikid_utils.ObsDetails;
import com.wikidsystems.android.client.TokenConfiguration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.Transcript;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by david on 1/14/14.
 */
@RunWith(RobolectricGradleTestRunner.class)

public class BookTransferFacadeTest {

    public static final String TOKEN_FILE_NAME = "WiKIDToken.wkd";

    private Transcript transcript;
    private AuthenticationFacade auth;
    private SetupFacade setupFacade;
    private PaymentReportingFacade paymentReportingFacade;
    private BookTransferFacade bookTransferFacade;
    private TokenConfiguration tokenConfiguration;
    private File f;
    private Context context;


    @Before
    public void setUp() throws Exception {

        ShadowLog.stream = System.out;
        transcript = new Transcript();

        context = new Activity();

        ObsDetails obsDetails = ObsDetails.getInstance();
        obsDetails.setCompanyCode("coco");
        obsDetails.setUsername("david");

        final String[] fList = context.fileList();
        boolean configExists = false;
        f = new File(HttpMessageConverterTest.class.getClassLoader().getResource(TOKEN_FILE_NAME).toURI());
        Assert.assertTrue(f.exists());

        for (String fname : fList) {
            configExists = configExists || TOKEN_FILE_NAME.equals(fname);
        }
        try {

            f = new File(HttpMessageConverterTest.class.getClassLoader().getResource(TOKEN_FILE_NAME).toURI());
            Assert.assertTrue(f.exists());

            tokenConfiguration = (new TokenConfiguration()).load(f, "lucky13".toCharArray());
            if (tokenConfiguration.getDomains().size() == 0) {
                TokenConfiguration.save(tokenConfiguration, f, "lucky13".toCharArray());
                setupFacade.init();

            } else {
                Logger.debug("Unable to create token Configuration");
            }
        } catch (Exception e) {
            Logger.debug("HomeMenu" + "Exception thrown: " + e.getMessage());
        }

        Robolectric.getBackgroundScheduler().pause();
        Robolectric.getUiThreadScheduler().pause();
    }

    @Test
    public void testAPIReqests() throws Exception {

        auth = new AuthenticationFacade();
        setupFacade = new SetupFacade();
        paymentReportingFacade = new PaymentReportingFacade();
        bookTransferFacade = new BookTransferFacade();

        TestOBSApplication.injectMocks(setupFacade);
        TestOBSApplication.injectMocks(auth);
        TestOBSApplication.injectMocks(bookTransferFacade);
        TestOBSApplication.injectMocks(setupFacade.getRestClient());

        setupFacade.init(tokenConfiguration);

        setupFacade.login("Asdf1234", new Callback() {
            @Override
            public void onTaskCompleted(Object result) {
                Log.d("onTaskCompleted", "Login Successful");

                bookTransferFacade.setup(new Callback<BookTransferSetup>() {
                    @Override
                    public void onTaskCompleted(BookTransferSetup result) {
                        Log.e("Book Transfer Setup","To Accounts: " + result.getToAccounts().toString() +
                        "From Accounts: " + result.getFromAccounts().toString() +
                        "minPaymentDate: " + result.getMinPaymentDate().toString() +
                        "maxPaymentDate: " + result.getMaxPaymentDate().toString());

                        TransferableAccount fromAccount = result.getFromAccounts().get(0);

                        TransferableAccount toAccount = result.getFromAccounts().get(0);
                        for(TransferableAccount account:result.getToAccounts()){
                            if(!account.getAccountId().equalsIgnoreCase(fromAccount.getAccountId())){
                                toAccount = account;
                            }
                        }

                        TransferItemRecord transfer = new TransferItemRecord(fromAccount.getAccountId(),toAccount.getAccountId(), "100", "unit test memo");
                        TransferItemContainer transferContainer = new TransferItemContainer();
                        ArrayList<TransferItemRecord> transfers = new ArrayList();
                        transfers.add(transfer);
                        transferContainer.setTransfers(transfers);


                    }

                    @Override
                    public void onTaskFailure(String Reason) {

                    }
                });


                /*
                paymentReportingFacade.getPayments(PaymentReportingFacade.PaymentRequestType.RECENT, 2013, 12, new Callback<GetPaymentsResponse>() {
                    @Override
                    public void onTaskCompleted(GetPaymentsResponse result) {

                        for (PaymentEntry objs : result.getPayments()) {
                            Log.d("Payment", "ID: " + objs.getTransactionId() + "Amount: " + objs.getDisplayAmount() + "Date: " + objs.getPaymentDate().toString());
                        }
                        Log.d("onTaskCompleted", result.toString());

                    }

                    @Override
                    public void onTaskFailure(String Reason) {

                    }
                });     */


                Robolectric.runBackgroundTasks();
                Robolectric.runUiThreadTasks();

            }

            @Override
            public void onTaskFailure(String Reason) {
                Log.e("onTaskFailure", "Login Failed");

            }
        });
        Robolectric.runBackgroundTasks();
        Robolectric.runUiThreadTasks();
        Robolectric.runBackgroundTasks();
        Robolectric.runUiThreadTasks();
        Robolectric.runBackgroundTasks();
        Robolectric.runUiThreadTasks();

    }
}
