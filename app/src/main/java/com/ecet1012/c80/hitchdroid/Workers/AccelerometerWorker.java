package com.ecet1012.c80.hitchdroid.Workers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.SystemClock;
import android.util.Log;

import com.ecet1012.c80.hitchdroid.MainActivity;
import com.ecet1012.c80.hitchdroid.PowerState;
import com.ecet1012.c80.hitchdroid.services.TaskManager;


/**
 * Created by pc_mh on 11/1/2015.
 */
public class AccelerometerWorker implements SensorEventListener {
    private static final String TAG = "AccelerometerWorker";

    private static PowerState powerState;

    private float[] accelerometer = {0f, 0f, 0f};

    public AccelerometerWorker() {
        powerState = MainActivity.powerState;
    }

    public void onSensorChanged(SensorEvent event) {
        float[] vector = NormalizeToGravity(event.values);
        float[] vector2 = new float[3];

        for (int i = 0; i < vector.length; i++) {
            vector2[i] = accelerometer[i] - vector[i];

            if (Math.abs(vector2[i]) > 0.165F) {
                MainActivity.lastActionTime = SystemClock.elapsedRealtime();
                if (!powerState.IsHoldingWake(TaskManager.SENSOR_INTENT, PowerState.BRIGHT_WAKE)) {
                    powerState.AcquireWakeLock(TaskManager.SENSOR_INTENT, PowerState.BRIGHT_WAKE);
                    Log.d(TAG, "BRIGHT WAKE ACQUIRED");
                    break;
                }


            }

        }
        accelerometer = vector.clone();
        long t = SystemClock.elapsedRealtime();
        if (t - MainActivity.lastActionTime > 3 * 1000 &&
                powerState.IsHoldingWake(TaskManager.SENSOR_INTENT, PowerState.BRIGHT_WAKE)) {
            Log.d(TAG, "TIMEOUT SCREEN");
            if (powerState.IsHoldingWake(TaskManager.SENSOR_INTENT, PowerState.BRIGHT_WAKE))
                powerState.ReleaseWakeLock(TaskManager.SENSOR_INTENT, PowerState.BRIGHT_WAKE);
        }

        if (powerState.IsHoldingWake(TaskManager.SENSOR_INTENT, PowerState.PARTIAL_WAKE))
            powerState.ReleaseWakeLock(TaskManager.SENSOR_INTENT, PowerState.PARTIAL_WAKE);
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

}