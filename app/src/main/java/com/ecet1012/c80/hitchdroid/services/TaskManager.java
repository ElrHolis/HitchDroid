package com.ecet1012.c80.hitchdroid.services;

import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.ecet1012.c80.hitchdroid.MainActivity;
import com.ecet1012.c80.hitchdroid.PowerState;
import com.ecet1012.c80.hitchdroid.Workers.AccelerometerWorker;
import com.ecet1012.c80.hitchdroid.Workers.ProximityWorker;


/**
 * Created by pc_mh on 10/30/2015.
 */
public class TaskManager extends BroadcastReceiver {
    private static final String TAG = "TaskManager";

    public static final String SENSOR_INTENT = "HitchDroid_SensorCheck";
    public static final String CAMERA_INTENT = "HitchDroid_CameraCheck";
    public static final String FINISH_INTENT = "HitchDroid_FinishWakeLock";
    public static final String INIT_INTENT = "HitchDroid_InitTaskManager";

    public static final int INTERVAL_SENSOR_DEFAULT = 250;
    public static final int INTERVAL_CAMERA_DEFAULT = 2000;
    public static final int INTERVAL_ACCELEROMETER_DEFAULT = 250;
    public static final int INTERVAL_PROXIMITY_DEFAULT = 5000;

    private static int accelerometerInterval = INTERVAL_SENSOR_DEFAULT;
    private static int proximityInterval = INTERVAL_SENSOR_DEFAULT;
    private static int cameraInterval = INTERVAL_CAMERA_DEFAULT;


    private static AlarmManager alarmManager;

    private static PendingIntent pendingSensorIntent;
    private static PendingIntent pendingCameraIntent;


    private static PowerState powerState;


    private static Intent sIntent = new Intent(SENSOR_INTENT);
    private static Intent cIntent = new Intent(CAMERA_INTENT);


    @Override
    public void onReceive(Context context, Intent intent) {


        switch (intent.getAction()) {
            case SENSOR_INTENT:
                powerState.AcquireWakeLock(SENSOR_INTENT, PowerState.PARTIAL_WAKE);
                InitNextSensorAlarm(accelerometerInterval);

                break;
            case CAMERA_INTENT:

                InitNextCameraAlarm(cameraInterval);
                break;
            case FINISH_INTENT:
                break;
            case INIT_INTENT:
                powerState = MainActivity.powerState;
                alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                pendingSensorIntent = PendingIntent.getBroadcast(context, 0, sIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                pendingCameraIntent = PendingIntent.getBroadcast(context, 0, cIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                sensorManager.registerListener(new AccelerometerWorker(), sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), accelerometerInterval * 1000);    // 250ms
                sensorManager.registerListener(new ProximityWorker(), sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), proximityInterval * 1000);       // 1000ms


                InitNextSensorAlarm(proximityInterval);
                InitNextCameraAlarm(cameraInterval);


                break;
        }


    }

    private void InitNextSensorAlarm(int newInterval) {


        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + newInterval, pendingSensorIntent);

    }

    private void InitNextCameraAlarm(int newInterval) {

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + newInterval, pendingCameraIntent);
    }
}
