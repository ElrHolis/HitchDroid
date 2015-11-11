package com.ecet1012.c80.hitchdroid.task;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

import com.ecet1012.c80.hitchdroid.MainActivity;
import com.ecet1012.c80.hitchdroid.power.PowerState;
import com.ecet1012.c80.hitchdroid.services.TaskManager;
import com.ecet1012.c80.hitchdroid.utils.Settings;

/**
 * Created by pc_mh on 11/9/2015.
 */
public class Accelerometer extends GenericTask implements SensorEventListener {
    private static final String TAG = "Accelerometer";


    private String wake;


    private float[] accelerometer = {0f, 0f, 0f};

    public Accelerometer() {
        super();

        SensorManager sm = (SensorManager) MainActivity.mainContext.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), this.GetTaskInterval() * Settings.MSEC);

    }


    public void onSensorChanged(SensorEvent event) {
        float[] vector = NormalizeToGravity(event.values);
        float[] vector2 = new float[3];

        for (int i = 0; i < vector.length; i++) {
            vector2[i] = accelerometer[i] - vector[i];

            if (Math.abs(vector2[i]) > 0.165F) {
                MainActivity.lastActionTime = SystemClock.elapsedRealtime();
                if (!powerState.IsHoldingWake(this.getClass().getSimpleName(), PowerState.BRIGHT_WAKE)) {
                    powerState.AcquireWakeLock(this.getClass().getSimpleName(), PowerState.BRIGHT_WAKE);
                    Log.d(TAG, "BRIGHT WAKE ACQUIRED");
                    break;
                }


            }

        }
        accelerometer = vector.clone();
        long t = SystemClock.elapsedRealtime();
        if (t - MainActivity.lastActionTime > 3 * 1000 &&
                powerState.IsHoldingWake(this.getClass().getSimpleName(), PowerState.BRIGHT_WAKE)) {
            Log.d(TAG, "TIMEOUT SCREEN");
            if (powerState.IsHoldingWake(this.getClass().getSimpleName(), PowerState.BRIGHT_WAKE))
                powerState.ReleaseWakeLock(this.getClass().getSimpleName(), PowerState.BRIGHT_WAKE);
        }

        Done();
    }


    private float[] NormalizeToGravity(float[] vector) {
        float gravity = 9.809915F;
        for (int i = 0; i < vector.length; i++)
            vector[i] = vector[i] / gravity;
        return vector;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int newAccuracy) {

    }

    @Override
    public void DoTask() {
        Log.d(TAG, "DoTask");
    }

    @Override
    public void ScheduleNextAlarm() {
        super.ScheduleNextAlarm();
    }

    @Override
    public void DoSpecial() {

    }

    @Override
    protected void Done() {
        TaskManager.CompleteTask(this.getClass().getSimpleName());
    }

}
