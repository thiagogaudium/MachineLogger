package com.taximachine.machinelogger;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import androidx.core.app.ActivityCompat;

import java.io.File;

public class ManagerUtil {
    public static final int GPS_PERMISSION_REQUEST_CODE = 1500;
    public static final int BASIC_PERMISSION_REQUEST_CODE = 1501;

    public static final String[] REQUIRED_LOCATION_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    @SuppressLint("InlinedApi")
    public static final String[] REQUIRED_BKG_PERMISSIONS = {
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final String[] REQUIRED_BASIC_PERMISSIONS = {
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_NETWORK_STATE
    };

    public static String[] getLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return REQUIRED_BKG_PERMISSIONS;
        }
        else
            return REQUIRED_LOCATION_PERMISSIONS;
    }

    public static boolean hasLocationPermissions(Context ctx, boolean includeBackgroundPermissions) {
        if(includeBackgroundPermissions && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (String requiredBkgPermission : REQUIRED_BKG_PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(ctx, requiredBkgPermission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        for (String requiredLocationPermission : REQUIRED_LOCATION_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(ctx, requiredLocationPermission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasLocationPermissions(Context ctx) {
        return hasLocationPermissions(ctx, true);
    }

    public static String getSOVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getAppVersion(Context ctx) {
        String version = "";
        PackageInfo pInfo;
        try {
            pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            version = "A" + pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return version;
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static long getFreeRamMemorySize(Context ctx) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        return availableMegs;
    }

    public static long getTotalRamMemorySize(Context ctx) {
        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            long totalMegs = mi.totalMem / 1048576L;
            return totalMegs;
        } catch (Throwable t) {
        }
        return -1;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    public static long getAvailableExternalMemorySize() {
        try {
            if (externalMemoryAvailable()) {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSizeLong();
                long availableBlocks = stat.getAvailableBlocksLong();
                return availableBlocks * blockSize;
            } else {
                return -1;
            }
        }
        catch (Throwable t){
            return -1;
        }
    }

    public static long getTotalExternalMemorySize() {
        try {
            if (externalMemoryAvailable()) {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSizeLong();
                long totalBlocks = stat.getBlockCountLong();
                return totalBlocks * blockSize;
            } else {
                return -1;
            }
        }
        catch (Throwable t){
            return -1;
        }
    }

    public static String capitalize(String s) {
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

    public static String[] getRequiredBasicPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // only for android 10+
            return null;
        }

        return REQUIRED_BASIC_PERMISSIONS;
    }

    public static boolean checkBasicAppPermissions(Context ctx){
        String[] permissions = getRequiredBasicPermissions();
        if (permissions != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(ctx, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
