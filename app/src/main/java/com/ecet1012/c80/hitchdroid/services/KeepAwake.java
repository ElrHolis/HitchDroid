package com.ecet1012.c80.hitchdroid.services;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by pc_mh on 10/30/2015.
 * This class is derived and based on the work of Joisar
 * https://github.com/Joisar/LockScreenApp/blob/master/LockScreenApp/src/com/mehuljoisar/lockscreen/utils/LockscreenService.java
 */

import com.ecet1012.c80.hitchdroid.R;
import com.ecet1012.c80.hitchdroid.utils.IntentReceiver;

public class KeepAwake extends Service {

    private BroadcastReceiver _Receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int initID) {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        iFilter.addAction(Intent.ACTION_SCREEN_OFF);
        _Receiver = new IntentReceiver();
        registerReceiver(_Receiver, iFilter);

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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(_Receiver);
    }
}
