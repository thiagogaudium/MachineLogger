package com.taximachine.machinelogger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GPSService extends Service {
    private static final int TIMEOUT_SECONDS = 20;
    private static final int POSITION_DISPATCHING_THRESHOLD = 30;
    private static final String DISPATCH_URL = "https://dbgapi-desenv.taximachine.com.br/machinelogger/log.php";

    private Workable<GPSPoint> workable;
    private GpsDataObject gpsDataObject = new GpsDataObject();
    private boolean useMultipart = true;
    private String identifier;
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        SharedPreferences sp = getApplicationContext().getSharedPreferences("SHARED_PREFS" ,Context.MODE_PRIVATE);
        identifier = sp.getString("identifier", UUID.randomUUID().toString());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("identifier", identifier);
        editor.commit();

        workable = (gpsPoint) -> {
            if(gpsDataObject.getPosicoes().size() == POSITION_DISPATCHING_THRESHOLD) {
                gpsDataObject.setModelo(ManagerUtil.getDeviceName());
                gpsDataObject.setVersao_so(ManagerUtil.getSOVersion());
                gpsDataObject.setMemoria_ram_livre(ManagerUtil.getFreeRamMemorySize(getApplicationContext()));
                gpsDataObject.setTotal_memoria_ram(ManagerUtil.getTotalRamMemorySize(getApplicationContext()));
                gpsDataObject.setIdentifier(identifier);

                String nomeOperadora;
                TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                if(manager != null) {
                    nomeOperadora = manager.getNetworkOperatorName();
                    if(nomeOperadora == null || nomeOperadora.length() <= 0) {
                        nomeOperadora = "Sem operadora";
                    }
                }
                else{
                    nomeOperadora = "Erro ao obter operadora";
                }
                
                gpsDataObject.setOperadora(nomeOperadora);

                doPost(gpsDataObject);

                Gson gson = new Gson();
                Log.i("GPS", gson.toJson(gpsDataObject));

                // Resetando objeto.
                gpsDataObject = new GpsDataObject();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ", Locale.getDefault());
            gpsDataObject.addPosicao(new GpsDataObject.Posicao(gpsPoint.getLatitude(), gpsPoint.getLongitude(), gpsPoint.getSpeed(), gpsPoint.getAccuracy(), dateFormat.format(gpsPoint.getDate())));

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

    protected void doPost(GpsDataObject dataObject) {
        Request.Builder requestBuilder = new Request.Builder().url(DISPATCH_URL);

        RequestBody requestBody = null;
        Gson gson = new Gson();
        String jsonBody = gson.toJson(dataObject);
        requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json"));


        requestBuilder.post(requestBody);

        client.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Request Failed", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("Request Response", response.isSuccessful() ? response.body().string() : "Request Failed");
            }
        });
    }
}
