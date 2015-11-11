package com.ecet1012.c80.hitchdroid.services;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import com.ecet1012.c80.hitchdroid.R;


/**
 * Created by pc_mh on 11/1/2015.
 */
public class ActiveNotification extends Service {
    private static final String TAG = "ActiveNotification";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notification tNotification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText("HitchDROID Operational")
                .setContentIntent(null)
                .setOngoing(true)
                .build();
        startForeground(101280, tNotification);
        return START_STICKY;
    }
}
