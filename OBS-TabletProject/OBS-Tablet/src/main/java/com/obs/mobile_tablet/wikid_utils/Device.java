package com.obs.mobile_tablet.wikid_utils;

import android.content.pm.PackageInfo;
import android.content.pm.*;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.content.Context;
//import com.obs.android.R;
//import org.OpenUDID.OpenUDID_manager;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: davidnewman
 * Date: 5/21/13
 * Time: 10:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class Device {

    @Inject public static Context context;
    public static String os(){
        return "Android";
    }


    public static String osVersion() {
        String osv = Build.VERSION.INCREMENTAL;
        return osv;
    }


    public static String appVersion(){
        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0 ).versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "1.0";
        }
    }


    public static String deviceType() {

       Boolean isTablet = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        if (isTablet == true) {
            return "Tablet";
        } else{
            return "Phone";
        }
    }


    public static String deviceUuid() {
        return "SOMESTATICUDID";
//        return OpenUDID_manager.getOpenUDID();

        //TODO: This method can potentially return nil, needs to have a backup plan such as mac address

//        final TelephonyManager tm =(TelephonyManager)HomeMenu.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
//
//        String deviceid = tm.getDeviceId();
//
//        return deviceid;
    }


    public static String deviceName() {
        String manufacturer = Build.MANUFACTURER;
        String blah = Build.DEVICE;

        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
