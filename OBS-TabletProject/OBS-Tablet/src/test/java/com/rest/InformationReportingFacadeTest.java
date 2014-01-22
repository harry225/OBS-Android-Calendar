package com.rest;

import android.content.Context;

import com.obs.mobile_tablet.facade.AuthenticationFacade;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.wikidsystems.android.client.TokenConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.util.Transcript;

import java.io.File;

/**
 * Created by david on 1/7/14.
 */

@RunWith(RobolectricGradleTestRunner.class)

public class InformationReportingFacadeTest {
    public static final String TOKEN_FILE_NAME = "WiKIDToken.wkd";

    private Transcript transcript;
    private AuthenticationFacade auth;
    private SetupFacade setupFacade;
    private InformationReportingFacade reportingFacade;
    private TokenConfiguration tokenConfiguration;
    private File f;
    private Context context;


    @Before
    public void setUp() throws Exception {
                             /*
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
                   */
    }


    /** This logs the user in, then does several test requests
     *
     * @throws Exception
     */

    /**
     * This creates fake api requests
     * @throws Exception
     */
/*
    @Test
    public void testFakeAPIRequests() throws Exception {
        reportingFacade.fake().getAccounts(new Callback<GetAccountsResponse>() {
            @Override
            public void onTaskCompleted(GetAccountsResponse result) {
                Log.d("onTaskCompleted", "Accounts Fetched");
                for (AccountObj accountObj:result.getAccounts()) {
                    Log.e("Fake Account", "AccountId: " + accountObj.accountId + "Name" + accountObj.displayName);
                }
            }

            @Override
            public void onTaskFailure(String Reason) {
                Log.e("Error: ", Reason);
            }
        });

        Robolectric.runBackgroundTasks();
        Robolectric.runUiThreadTasks();
    }
*/
    /*
    @Test
    public void testRealAndFakeAPIReqests() throws Exception {

        auth = new AuthenticationFacade();
        setupFacade = new SetupFacade();
        reportingFacade = new InformationReportingFacade();

        TestOBSApplication.injectMocks(setupFacade);
        TestOBSApplication.injectMocks(auth);
        TestOBSApplication.injectMocks(reportingFacade);
        TestOBSApplication.injectMocks(reportingFacade.getRestClient());

        setupFacade.init(tokenConfiguration);

        setupFacade.login("Asdf1234", new Callback() {
            @Override
            public void onTaskCompleted(Object result) {
                Log.d("onTaskCompleted", "Login Successful");

                reportingFacade.getAccounts(new Callback<GetAccountsResponse>() {
                    @Override
                    public void onTaskCompleted(GetAccountsResponse result) {
                        Log.d("onTaskCompleted", "Real Accounts Fetched");
                        reportingFacade.fake().getAccount(result.getAccounts().get(0), new Callback<AccountObj>() {
                            @Override
                            public void onTaskCompleted(AccountObj accountObj) {
                                Log.e("Fake Account From Real", "AccountId: " + accountObj.accountId + "Name" + accountObj.displayName);
                            }

                            @Override
                            public void onTaskFailure(String Reason) {
                                Log.e("Error: ", Reason);
                            }
                        });
                    }

                    @Override
                    public void onTaskFailure(String Reason) {
                        Log.e("Error: ", Reason);
                    }
                });

                Robolectric.runBackgroundTasks();
                Robolectric.runUiThreadTasks();
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
    */


    @Test
    public void testAPIReqests() throws Exception {
        /*
        auth = new AuthenticationFacade();
        setupFacade = new SetupFacade();
        reportingFacade = new InformationReportingFacade();

        TestOBSApplication.injectMocks(setupFacade);
        TestOBSApplication.injectMocks(auth);
        TestOBSApplication.injectMocks(reportingFacade);
        TestOBSApplication.injectMocks(reportingFacade.getRestClient());

        setupFacade.init(tokenConfiguration);

        setupFacade.login("Asdf1234", new Callback() {
            @Override
            public void onTaskCompleted(Object result) {
                Log.d("onTaskCompleted", "Login Successful");

                reportingFacade.getAccounts(new Callback<GetAccountsResponse>() {
                    @Override
                    public void onTaskCompleted(GetAccountsResponse result) {

                        Log.d("onTaskCompleted", "Accounts Fetched");
                        List<AccountObj> accountObjs = result.getAccounts();
                        AccountObj accountObj = accountObjs.get(0);

                        reportingFacade.getTransactions(accountObj, new TransactionSearch(null, null, null, true, null, new BigDecimal(1000), new BigDecimal(2000), accountObj.accountId, null, null), new Callback<TransactionsObj>() {

                            @Override
                            public void onTaskCompleted(TransactionsObj result) {
                                List<TransactionObj> transactionObjs = result.getTransactions();
                                for (TransactionObj objs : transactionObjs) {
                                    Log.d("Searched Transaction", "Debit: " + objs.debitAmount + " || Credit: " + objs.creditAmount);
                                }
                                Log.d("onTaskCompleted", result.toString());
                            }

                            @Override
                            public void onTaskFailure(String Reason) {
                                Log.d("onTaskFailure", "Get account failed: " + Reason);
                            }
                        });
                        */

                        /*
                        reportingFacade.getAccount(accountObj, new Callback<AccountObj>() {
                            @Override
                            public void onTaskCompleted(AccountObj result) {
                                Log.d("onTaskCompleted", result.toString());
                            }

                            @Override
                            public void onTaskFailure(String Reason) {
                                Log.d("onTaskFailure", "Get account failed: " + Reason);
                            }
                        });

                        reportingFacade.getTransactions(accountObj, new Callback<TransactionsObj>() {
                            @Override
                            public void onTaskCompleted(TransactionsObj result) {
                                Log.d("onTaskCompleted", result.toString());
                            }

                            @Override
                            public void onTaskFailure(String Reason) {
                                Log.d("onTaskFailure", "Get account failed: " + Reason);
                            }
                        });

                          reportingFacade.getTransactionGroups(new Callback<Collection<TransactionGroup>>() {
                            @Override
                            public void onTaskCompleted(Collection<TransactionGroup> result) {
                                for(TransactionGroup transactionGroup : result) {
                                    if (transactionGroup != null){
                                        Log.d("Transaction Group ", "Code: " + transactionGroup.code + "Name: " + transactionGroup.name);
                                    }
                                }
                                Log.d("onTaskCompleted", result.toString());

                            }

                            @Override
                            public void onTaskFailure(String Reason) {
                                Log.d("onTaskFailure", "Get Transaction Groups failed: " + Reason);

                            }
                        });

                        reportingFacade.getAccountTotals(new Callback<GetAccountTotalsResponse>() {
                            @Override
                            public void onTaskCompleted(GetAccountTotalsResponse result) {
                                for (AccountTotalObj totalObj : result.getTotals()) {
                                    Log.d("AccountTotalObj","Type: " + totalObj.displayType + "Account Counts: " + totalObj.accountCount);
                                }
                            }

                            @Override
                            public void onTaskFailure(String Reason) {

                            }
                        });
                          */


                           /*
                        Robolectric.runBackgroundTasks();
                        Robolectric.runUiThreadTasks();
                    }

                    @Override
                    public void onTaskFailure(String Reason) {
                        Log.d("onTaskFailure", "Get accounts failed: " + Reason);

                    }
                });


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
    */
    }

}
