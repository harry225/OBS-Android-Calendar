package com.obs.mobile_tablet.utils;

import android.util.Log;

public class Logger {

	//TODO: before deploying app, set IS_DEV to false:
	public static final boolean IS_DEV = true; //true = show logs, false = don't show logs
	public static final String DEBUG_TAG = "OBSDebug"; //filter logcat with this tag


    public static void verbose(String verbose) {
        if (IS_DEV) {
            Log.v(DEBUG_TAG, verbose);
        }
    }

	public static void debug(String debugText) {
		if (IS_DEV) {
			Log.d(DEBUG_TAG, debugText);
		}
	}

	public static void warn(String warnText) {
		if (IS_DEV) {
			Log.w(DEBUG_TAG, warnText);
		}
	}

    public static void error(String warnText) {
        if (IS_DEV) {
            Log.e(DEBUG_TAG, warnText);
        }
    }

}