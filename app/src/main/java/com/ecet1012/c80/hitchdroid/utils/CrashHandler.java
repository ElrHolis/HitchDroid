package com.ecet1012.c80.hitchdroid.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.ecet1012.c80.hitchdroid.MainActivity;

import java.util.List;

/**
 * Created by pc_mh on 11/5/2015.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";

    private static PendingIntent crashIntent;

    private static AlarmManager alarmManager;

    private static List<Throwable> ExitThrowables;

    public CrashHandler() {
        alarmManager = (AlarmManager) MainActivity.mainContext.getSystemService(Context.ALARM_SERVICE);
        crashIntent = PendingIntent.getActivity(MainActivity.mainContext, 0, new Intent(MainActivity.mainContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_ONE_SHOT);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1, crashIntent);
        System.exit(2);
    }

    public void addExit(Throwable ex) {

    }

}
