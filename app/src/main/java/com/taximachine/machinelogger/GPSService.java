package com.taximachine.machinelogger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

public class GPSService extends Service {
    private Workable<GPSPoint> workable;
    private GpsDataObject gpsDataObject = new GpsDataObject();

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        workable = (gpsPoint) -> {
            if(gpsDataObject.getPosicoes().size() == 60) {
                gpsDataObject.setModelo(ManagerUtil.getDeviceName());
                gpsDataObject.setVersaoSO(ManagerUtil.getSOVersion());
                gpsDataObject.setMemoriaRAMLivre(ManagerUtil.getFreeRamMemorySize(getApplicationContext()));
                gpsDataObject.setTotalMemoriaRAM(ManagerUtil.getTotalRamMemorySize(getApplicationContext()));

                String nomeOperadora;
                TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                if(manager != null) {
                    nomeOperadora = manager.getNetworkOperatorName();
                }
                else{
                    nomeOperadora = "Erro ao obter operadora";
                }
                gpsDataObject.setOperadora(nomeOperadora);

                // TODO: Dispatch it to backend.
                Gson gson = new Gson();
                Log.i("GPS", gson.toJson(gpsDataObject));

                // Resetando objeto.
                gpsDataObject = new GpsDataObject();
            }

            gpsDataObject.addPosicao(new GpsDataObject.Posicao(gpsPoint.getLatitude(), gpsPoint.getLongitude(), gpsPoint.getSpeed(), gpsPoint.getAccuracy(), gpsPoint.getLastUpdate()));

        };
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Whereabouts.instance().onChange(workable);
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
