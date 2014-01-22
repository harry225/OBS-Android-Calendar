package com.obs.mobile_tablet.utils;

import android.graphics.Color;
import android.content.Context;

import com.obs.mobile_tablet.R;

/**
 * Created by danielstensland on 12/16/13.
 */
public class OBSGUI {
    private static OBSGUI ourInstance = new OBSGUI();

    public static OBSGUI getInstance() {
        return ourInstance;
    }

    public static int primaryColor;
    public static int secondaryColor;
    public static int accentColor;
    public static Context context;

    private OBSGUI() {

        //primaryColor = getApplicationContext().getResources().getColor(R.id.primary);
    }

    public void setContext (Context newContext) {

        context = newContext;
        primaryColor = context.getResources().getColor(R.color.obsPrimary);
        secondaryColor = context.getResources().getColor(R.color.obsSecondary);
        accentColor = context.getResources().getColor(R.color.obsAccent);

    }
}