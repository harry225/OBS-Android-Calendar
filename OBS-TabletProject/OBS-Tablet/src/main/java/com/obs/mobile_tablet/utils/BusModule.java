package com.obs.mobile_tablet.utils;

import com.obs.mobile_tablet.LoginActivity;
import com.obs.mobile_tablet.MainActivity;
import com.obs.mobile_tablet.RegistrationActivity;
import com.obs.mobile_tablet.accounts.AccountsDetailFragment;
import com.obs.mobile_tablet.accounts.AccountsFragment;
import com.obs.mobile_tablet.registration.RegistrationFragment1;
import com.obs.mobile_tablet.registration.RegistrationFragment2;
import com.obs.mobile_tablet.registration.RegistrationFragment3;
import com.obs.mobile_tablet.registration.ResetPasswordDialogFragment;
import com.obs.mobile_tablet.rest.AuthHelper;
import com.obs.mobile_tablet.rest.RestClient;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by david on 1/12/14.
 */

@Module(
        injects = {
                RestClient.class,
                RegistrationActivity.class,
                MainActivity.class,
                LoginActivity.class,
                RegistrationFragment1.class,
                RegistrationFragment2.class,
                RegistrationFragment3.class,
                ResetPasswordDialogFragment.class,
                AccountsFragment.class,
                AccountsDetailFragment.class
        },
        library=true,
        complete = false
)
public class BusModule {
    private final Bus bus;


    public BusModule() {
        this.bus = new Bus(ThreadEnforcer.MAIN);
    }

    @Provides @Singleton Bus provideBus() {
        return this.bus;
    }
}

