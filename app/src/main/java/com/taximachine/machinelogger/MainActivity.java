package com.taximachine.machinelogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    boolean serviceRunning = false;
    private GPSService gpsService;
    private Button btnIniciar;
    private boolean pressStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceRunning = isMyServiceRunning(GPSService.class);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setText(serviceRunning ? "Iniciado" : "Iniciar");
        btnIniciar.setOnClickListener((view) -> {
            if(!serviceRunning) {
                if(ManagerUtil.hasLocationPermissions(MainActivity.this) && gpsService == null) {
                    setupGPSService();
                } else {
                    pressStart = true;
                    requestLocationPermission();
                }
            }
        });
        if(!ManagerUtil.hasLocationPermissions(this)) {
            requestLocationPermission();
        }
    }

    public void setupGPSService() {
        gpsService = new GPSService();
        Intent serviceIntent = new Intent(this, gpsService.getClass());
        if(!isMyServiceRunning(gpsService.getClass())) {
            serviceRunning = true;
            btnIniciar.setText("Iniciado");
            startService(serviceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }



    public void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, ManagerUtil.getLocationPermissions(), ManagerUtil.PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == ManagerUtil.PERMISSION_REQUEST_CODE && pressStart) {
            boolean allGranted = true;
            if (grantResults.length > 0){
                for (int i = 0; i < grantResults.length; i++) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        allGranted = false;
                    }
                }
            }

            if(allGranted) {
                pressStart = false;
                setupGPSService();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(serviceRunning) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);
        }
        super.onDestroy();
    }
}