package com.obs.mobile_tablet;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.obs.mobile_tablet.accounts.AccountsDetailFragment;
import com.obs.mobile_tablet.accounts.AccountsFragment;
import com.obs.mobile_tablet.accounts.AccountsListAdapter;
import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.facade.InformationReportingFacade;
import com.obs.mobile_tablet.messages.MessagesFragment;
import com.obs.mobile_tablet.navigation.MainNavigation;
import com.obs.mobile_tablet.navigation.NavigationData;
import com.obs.mobile_tablet.payments.PaymentsFragment;
import com.obs.mobile_tablet.registration.RegistrationAlertBox;
import com.obs.mobile_tablet.utils.BannersReceivedEvent;
import com.obs.mobile_tablet.utils.TimeoutEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.testflightapp.lib.TestFlight;

import javax.inject.Inject;

public class MainActivity extends ActionBarActivity implements MainNavigation.OnNavListener {

    @Inject
    InformationReportingFacade informationReportingFacade;

    @Inject
    Bus bus;



    private int currentPageIndex = 0;
    private MainNavigation mainNav;

    //public AccountObj currentAccount; //used to pass data between the AccountsFragment and AccountsDetailFragment



    public void onNavClicked(int navIndex) {
        //Logger.debug("MainActivity :: onNavClicked = " + navIndex);
        changePage(navIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TestFlight.log("Main Activity Log Test");
        super.onCreate(savedInstanceState);
        ((OBSApplication)getApplication()).inject(this);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            mainNav = MainNavigation.newInstance(1);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.nav_holder, mainNav);
            ft.commit();
        }

        View navHolder = findViewById(R.id.nav_holder);

        navHolder.bringToFront();

    }

    @Override
    protected void onStart() {
        //Logger.debug("RegistrationActivity :: onStart");
        super.onStart();
        changePage(1);
    }

    private void changePage(int pageIndex) {
        if (pageIndex != currentPageIndex) {
            Fragment f = null;
            switch (pageIndex) {
                case NavigationData.NAV_ACCOUNTS:
                    f = AccountsFragment.newInstance(1);
                    ((OBSApplication)getApplication()).inject(f);
                    break;
                case NavigationData.NAV_PAYMENTS:
                    f = PaymentsFragment.newInstance(1);
                    ((OBSApplication)getApplication()).inject(f);
                    break;
                case NavigationData.NAV_MESSAGES:
                    f = MessagesFragment.newInstance(1);
                    break;
            }

            if (f != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                if (currentPageIndex > 0) {
                    ft.replace(R.id.content_holder, f);
                }
                else {
                    ft.add(R.id.content_holder, f);
                }
                ft.commit();

                mainNav.setCurrent(pageIndex);
                currentPageIndex = pageIndex;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //this.fetchAccounts();

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);

    }



    /**Banner items are here*/
    @Subscribe
    public void displayBanners(BannersReceivedEvent event) {
        for (String error : event.getErrorBanners()) {
//            bannersFragment(RegistrationAlertBox.TYPE_WARNING, error);
        }
        for (String info : event.getInfoBanners()){
//            bannersFragment(RegistrationAlertBox.TYPE_WARNING, error);
        }
        for (String success : event.getSuccessBanners()){
//            bannersFragment(RegistrationAlertBox.TYPE_WARNING, error);
        }
    }

    @Subscribe
    public void timeout(TimeoutEvent event) {
        //Take user to login screen
    }

}
