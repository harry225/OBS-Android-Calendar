package com.obs.mobile_tablet.utils;

/**
 * Created by david on 12/23/13.
 */
import android.content.Context;
import android.location.LocationManager;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.accounts.AccountsFragment;
import com.obs.mobile_tablet.accounts.AccountsListAdapter;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.rest.RestClient;
import com.obs.mobile_tablet.wikid_utils.ObsDetails;
import com.obs.mobile_tablet.wikid_utils.WikidAsyncHelper;
import com.wikidsystems.android.client.wComms;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module(
        injects = {
                SetupFacade.class,
                RestClient.class,
                WikidAsyncHelper.class,
                wComms.class,
                AccountsFragment.class,
                ObsDetails.class,
                AccountsListAdapter.class
        },
        complete = false,
        library = true
)
public class AndroidModule {
    private final OBSApplication application;

    public AndroidModule(OBSApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @ForApplication} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication Context provideContext() {
//        application.inject(application.getApplicationContext());
        return application.getApplicationContext();
    }

    @Provides @Singleton @ForApplication OBSApplication provideOBSApplication() {
//        application.inject(application.getApplicationContext());
        return application;
    }
}
