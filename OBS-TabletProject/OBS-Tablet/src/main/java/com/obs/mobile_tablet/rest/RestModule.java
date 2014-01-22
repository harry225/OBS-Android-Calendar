package com.obs.mobile_tablet.rest;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.facade.AuthenticationFacade;
import com.obs.mobile_tablet.facade.BookTransferFacade;
import com.obs.mobile_tablet.facade.BulletinFacade;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.facade.OBSFacade;
import com.obs.mobile_tablet.facade.PaymentReportingFacade;
import com.obs.mobile_tablet.facade.SecureMessageFacade;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.facade.WirePaymentFacade;
import com.wikidsystems.android.client.wComms;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                OBSFacade.class,
                SetupFacade.class,
                InformationReportingFacade.class,
                PaymentReportingFacade.class,
                AuthenticationFacade.class,
                BookTransferFacade.class,
                WirePaymentFacade.class,
                SecureMessageFacade.class,
                BulletinFacade.class,
                wComms.class
        },
        library=true,
        complete = false
)
public class RestModule {
    private final RestClient restClient;
    private final OBSApplication application;

    public RestModule(OBSApplication app) {
        this.application = app;
        restClient = new RestClient();
    }

    @Provides @Singleton RestClient provideRestClient() {
        this.application.inject(restClient);
        return this.restClient;
    }


}
