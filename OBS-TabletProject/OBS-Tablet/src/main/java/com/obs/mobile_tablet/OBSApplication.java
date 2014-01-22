package com.obs.mobile_tablet;

import android.app.Application;

import com.obs.mobile_tablet.facade.FacadeModule;
import com.obs.mobile_tablet.facade.SetupFacade;
import com.obs.mobile_tablet.rest.RestModule;
import com.obs.mobile_tablet.utils.BusModule;
import com.obs.mobile_tablet.utils.OBSGUI;
import com.obs.mobile_tablet.utils.AndroidModule;
import com.obs.mobile_tablet.wikid_utils.ObsDetails;
import com.testflightapp.lib.TestFlight;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;



/**
 * Created by danielstensland on 12/16/13.
 */
public class OBSApplication extends Application {

    private ObjectGraph graph;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        TestFlight.takeOff(this, "6e2548c0-ebbf-4eb6-b404-15f1719373c3");

        TestFlight.log("Test Log Here");
        graph = ObjectGraph.create(getModules().toArray());

        initSingletons();

    }

    protected void initSingletons()
    {
        // Initialize the instance of MySingleton

        inject(ObsDetails.getInstance());
        ObsDetails.getInstance().load();
        OBSGUI.getInstance();
    }

    public void customAppMethod()
    {
        // Custom application method
    }

    protected List<Object> getModules() {
        return Arrays.asList(
            new FacadeModule(this),
            new AndroidModule(this),
            new RestModule(this),
            new BusModule()
        );
    }

    public void inject(Object object) {
        graph.inject(object);
    }
}
