package com.rest;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.obs.mobile_tablet.TestOBSApplication;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountsResponse;
import com.obs.mobile_tablet.datamodel.reporting.TransactionsObj;
import com.obs.mobile_tablet.facade.AuthenticationFacade;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.obs.mobile_tablet.TestOBSApplication;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountsResponse;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionSearch;
import com.obs.mobile_tablet.datamodel.reporting.TransactionsObj;
import com.obs.mobile_tablet.facade.AuthenticationFacade;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.utils.Logger;
import com.wikidsystems.android.client.TokenConfiguration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.util.Transcript;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)

public class AuthenticationFacadeTest {

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
        try {
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
        }catch (Exception e){
            Log.w("FilePath: ",context.getPackageResourcePath()+TOKEN_FILE_NAME);
            f = new File(context.getPackageResourcePath()+TOKEN_FILE_NAME);


            Log.e("Error","Wikid file not found");
        }



             */
    }

    /** This logs the user in, then does several test requests
     *
     * @throws Exception
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

        setupFacade.init(tokenConfiguration);

        setupFacade.login("Asdf1234", new Callback() {
            @Override
            public void onTaskCompleted(Object result) {
                Log.d("onTaskCompleted", "Login Successful");
                reportingFacade.getAccounts(new Callback<GetAccountsResponse>() {
                    @Override
                    public void onTaskCompleted(GetAccountsResponse result) {
                        Log.d("onTaskCompleted", "Accounts Fetched");
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

/** This test is ran to setup the wikid token.
 * It should be commented out once wikid token is setup
 * */
             /*
    @Test
    public void testAuthenticateRequest() throws Exception {

        Log.e("testInfoRequest:","About to fire off a test");

        auth = new AuthenticationFacade();
        setupFacade = new SetupFacade();

        TestOBSApplication.injectMocks(setupFacade);
        TestOBSApplication.injectMocks(auth);

        setupFacade.init(context);

        tokenConfiguration.deleteAllDomains();

//        TokenConfiguration.save(setupFacade.tokenConfiguration, f, "lucky13".toCharArray());
//        TokenConfiguration.save(tokenConfiguration, new File(context.getExternalCacheDir().toURI().toString()), "lucky13".toCharArray());


        Log.w("Files File Directory",""+context.getFilesDir().toURI().toString());
        Log.w("Files External Cache",""+context.getExternalCacheDir().toURI().toString());

        final String[] fList = context.getExternalCacheDir().list();
        boolean configExists = false;

        for (String fname : fList) {
            Log.w("Files in external directory", "" + fname);
        }

        Log.w("Files External Storage",""+ Environment.getExternalStorageDirectory().toURI().toString());
//        Log.w("Files WiKid Token", "" + f.getAbsolutePath());



        setupFacade.authenticate("coco","david","XX349-7YQ79-447M7-A4P44-HL7TG".replaceAll("-",""),new Callback<String>() {
            @Override
            public void onTaskCompleted(String result) {
                Log.w("onTaskComplete", result);
                String[] fList = context.fileList();

                setupFacade.createRegistration("Asdf1234",new Callback<String>() {
                    @Override
                    public void onTaskCompleted(String result) {
                        String[] fList = context.fileList();
                        Log.w("onTaskCompleted: ","Registration Created");
                        setupFacade.registerTokenAndObs("mother","town", new Callback<String>() {
                            @Override
                            public void onTaskCompleted(String result) {
                                String[] fList = context.fileList();

                                Log.w("onTaskCompleted: ","Registration Verified");

                                setupFacade.login("Asdf1234", new Callback() {
                                    @Override
                                    public void onTaskCompleted(Object result) {

                                        if (setupFacade.tokenConfiguration!=null && f != null){
//                                            TokenConfiguration.save(setupFacade.tokenConfiguration,new File(context.getPackageResourcePath()),"lucky13".toCharArray());
//                                            TokenConfiguration.save(setupFacade.tokenConfiguration, f, "lucky13".toCharArray());
//                                            Log.e("TOKEN_URI", f.getAbsolutePath());
                                            Log.w("Files", "" + context.getFilesDir().toURI().toString());
                                        }

                                        String[] fList = context.fileList();

                                        Log.d("onTaskCompleted", "Login Successful");

                                    }

                                    @Override
                                    public void onTaskFailure(String Reason) {
                                        Log.e("onTaskFailure","Login Failed");

                                    }
                                });
                                Robolectric.runBackgroundTasks();
                                Robolectric.runUiThreadTasks();
                                Robolectric.runBackgroundTasks();
                                Robolectric.runUiThreadTasks();
                                Robolectric.runBackgroundTasks();
                                Robolectric.runUiThreadTasks();


                            }

                            @Override
                            public void onTaskFailure(String Reason) {
                                Log.w("onTaskFailure: ","Registration Verification Failed");

                            }
                        });
                        Robolectric.runBackgroundTasks();
                        Robolectric.runUiThreadTasks();
                    }

                    @Override
                    public void onTaskFailure(String Reason) {
                        Log.w("onTaskFailure: ", Reason!=null?Reason:"Registration Failed");
                    }
                });
                Robolectric.runBackgroundTasks();
                Robolectric.runUiThreadTasks();
                Robolectric.runBackgroundTasks();
                Robolectric.runUiThreadTasks();
            }

            @Override
            public void onTaskFailure(String Reason) {
                Log.e("onTaskComplete",Reason);
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


}
