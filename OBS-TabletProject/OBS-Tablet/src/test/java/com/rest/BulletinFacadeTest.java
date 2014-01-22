package com.rest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.obs.mobile_tablet.TestOBSApplication;
import com.obs.mobile_tablet.datamodel.bulletins.GetBulletinsResponse;
import com.obs.mobile_tablet.datamodel.bulletins.Bulletin;
import com.obs.mobile_tablet.datamodel.bulletins.BulletinAttachment;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.GetAccountsResponse;
import com.obs.mobile_tablet.datamodel.reporting.TransactionObj;
import com.obs.mobile_tablet.datamodel.reporting.TransactionSearch;
import com.obs.mobile_tablet.datamodel.reporting.TransactionsObj;
import com.obs.mobile_tablet.facade.AuthenticationFacade;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.facade.BulletinFacade;
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

/**
 * Created by hbray on 1/13/14.
 */

@RunWith(RobolectricGradleTestRunner.class)

public class BulletinFacadeTest {

    public static final String TOKEN_FILE_NAME = "WiKIDToken.wkd";

    private Transcript transcript;
    private AuthenticationFacade auth;
    private SetupFacade setupFacade;
    private InformationReportingFacade reportingFacade;
    private BulletinFacade bulletinFacade;
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
    public void testAPIRequests() throws Exception {

        auth = new AuthenticationFacade();
        setupFacade = new SetupFacade();
        bulletinFacade = new BulletinFacade();

        TestOBSApplication.injectMocks(setupFacade);
        TestOBSApplication.injectMocks(auth);
        TestOBSApplication.injectMocks(bulletinFacade);
        TestOBSApplication.injectMocks(setupFacade.getRestClient());

        setupFacade.init(tokenConfiguration);

        setupFacade.login("Asdf1234", new Callback() {
            @Override
            public void onTaskCompleted(Object result) {
                Log.d("HDB: onTaskCompleted", "Login Successful");
                bulletinFacade.getBulletins(new Callback<GetBulletinsResponse>() {
                    @Override
                    public void onTaskCompleted(GetBulletinsResponse result) {
                        Log.d("HDB: BulletinFacadeTest.onTaskCompleted", result.toString());

                        for (Bulletin msg : result.getBulletins().subList(0,result.getBulletins().size()<10?result.getBulletins().size():10)) {
                            Log.d("HDB: bulletin", "Subject: " + msg.getSubject() + ", Date: " + msg.getDate());
                        }
                        Log.d("HDB: onTaskCompleted", result.toString());

                        Robolectric.runBackgroundTasks();
                        Robolectric.runUiThreadTasks();
                    }

                    @Override
                    public void onTaskFailure(String Reason) {

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

    }

}
