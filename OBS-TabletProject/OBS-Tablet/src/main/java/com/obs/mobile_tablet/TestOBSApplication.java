package com.obs.mobile_tablet;


//import org.robolectric.Robolectric;

import java.util.List;

/**
 * Created by david on 1/5/14.
 */
public class TestOBSApplication extends OBSApplication{

    @Override
    protected List<Object> getModules() {
        List<Object> modules = super.getModules();
        return modules;
    }

    public static void injectMocks(Object object) {
//        TestOBSApplication app = (TestOBSApplication) Robolectric.application;
//        app.inject(object);
    }
}
