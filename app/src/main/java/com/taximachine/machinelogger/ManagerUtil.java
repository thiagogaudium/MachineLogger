package com.taximachine.machinelogger;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class ManagerUtil {
    public static final int PERMISSION_REQUEST_CODE = 1500;

    public static final String[] REQUIRED_LOCATION_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    public static final String[] REQUIRED_BKG_PERMISSIONS = {
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final String[] REQUIRED_BASIC_PERMISSIONS = {
            android.Manifest.permission.ACCESS_WIFI_STATE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.WAKE_LOCK,
            android.Manifest.permission.VIBRATE,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
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

}
