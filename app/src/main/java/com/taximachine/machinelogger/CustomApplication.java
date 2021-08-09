package com.taximachine.machinelogger;

import android.app.Application;
import android.content.Context;

public class CustomApplication extends Application {
    private static boolean activityVisible;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
