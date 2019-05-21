package com.qwert2603.email_webcam;

import android.util.Log;

public class LogUtils {

    private static final String TAG = "AASSDD";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg, Throwable throwable) {
        Log.e(TAG, msg, throwable);
    }

}
