package com.raghu.testfire.utils;

import android.util.Log;

import com.raghu.testfire.app.Constants;

public class LogUtil {

    private static String TAG = "TestFire";

    public static void v(String message) {
        if (Constants.isLogEnabled) Log.v(TAG, message);
    }

    public static void e(String message) {
        if (Constants.isLogEnabled) Log.e(TAG, message);
    }

    public static void d(String message) {
        if (Constants.isLogEnabled) Log.d(TAG, message);
    }

    public static void i(String message) {
        if (Constants.isLogEnabled) Log.i(TAG, message);
    }

    public static void w(String message) {
        if (Constants.isLogEnabled) Log.w(TAG, message);
    }
}
