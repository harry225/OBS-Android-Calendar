package com.obs.mobile_tablet.facade;

import com.obs.mobile_tablet.LoginActivity;
import com.obs.mobile_tablet.MainActivity;
import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.RegistrationActivity;
import com.obs.mobile_tablet.accounts.AccountSummaryFragment;
import com.obs.mobile_tablet.accounts.AccountsDetailFragment;
import com.obs.mobile_tablet.accounts.AccountsFragment;
import com.obs.mobile_tablet.bulletins.BulletinsFragment;
import com.obs.mobile_tablet.datamodel.payments.wires.WirePaymentEntryDetails;
import com.obs.mobile_tablet.messages.MessagesFragment;
import com.obs.mobile_tablet.payments.PaymentsFragment;
import com.obs.mobile_tablet.registration.RegistrationFragment1;
import com.obs.mobile_tablet.registration.RegistrationFragment2;
import com.obs.mobile_tablet.registration.RegistrationFragment3;
import com.obs.mobile_tablet.registration.ResetPasswordDialogFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
    injects = {
            RegistrationActivity.class,
            MainActivity.class,
            LoginActivity.class,
            RegistrationFragment1.class,
            RegistrationFragment2.class,
            RegistrationFragment3.class,
            ResetPasswordDialogFragment.class,
            AccountsFragment.class,
            AccountSummaryFragment.class,
            AccountsDetailFragment.class,
            PaymentsFragment.class,
            MessagesFragment.class,
            MessagesFragment.class,
            BulletinsFragment.class
        },
        library=true,
        complete = false
)
public class FacadeModule {

    private final OBSApplication application;
    private final AuthenticationFacade authenticationFacade;
    private final SetupFacade setupFacade;
    private final InformationReportingFacade informationReportingFacade;
    private final PaymentReportingFacade paymentReportingFacade;
    private final SecureMessageFacade secureMessageFacade;
    private final BulletinFacade bulletinFacade;
    private final BookTransferFacade bookTransferFacade;
    private final WirePaymentFacade wirePaymentFacade;

    public FacadeModule(OBSApplication app) {
        this.application = app;
        this.authenticationFacade = new AuthenticationFacade();
        this.setupFacade = new SetupFacade();
        this.informationReportingFacade = new InformationReportingFacade();
        this.paymentReportingFacade = new PaymentReportingFacade();
        this.secureMessageFacade = new SecureMessageFacade();
        this.bulletinFacade = new BulletinFacade();
        this.bookTransferFacade = new BookTransferFacade();
        this.wirePaymentFacade = new WirePaymentFacade();
    }

    @Provides @Singleton AuthenticationFacade provideAuthenticationFacade() {
        this.application.inject(authenticationFacade);
        return authenticationFacade;
    }

    @Provides @Singleton SetupFacade provideSetupFacade() {
        this.application.inject(setupFacade);
        this.setupFacade.init();
        return setupFacade;
    }

    @Provides @Singleton InformationReportingFacade provideInformationReportingFacade() {
        this.application.inject(informationReportingFacade);
        return informationReportingFacade;
    }

    @Provides @Singleton
    PaymentReportingFacade providePaymentFacade() {
        this.application.inject(paymentReportingFacade);
        return paymentReportingFacade;
    }

    @Provides @Singleton
    SecureMessageFacade provideSecureMessageFacade() {
        this.application.inject(secureMessageFacade);
        return secureMessageFacade;
    }

    @Provides @Singleton
    BulletinFacade provideBulletinFacade() {
        this.application.inject(bulletinFacade);
        return bulletinFacade;
    }
}
