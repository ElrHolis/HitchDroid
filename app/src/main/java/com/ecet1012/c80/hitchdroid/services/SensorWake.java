package com.ecet1012.c80.hitchdroid.services;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.ComposePathEffect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import com.ecet1012.c80.hitchdroid.MainActivity;

import java.util.List;

/**
 * Created by pc_mh on 11/1/2015.
 */
public class SensorWake extends JobService implements SensorEventListener {
    private static final String TAG = "SensorWake";

    private PowerManager _powerManager;
    private PowerManager.WakeLock _wakelock;

    private SensorManager _sensorManager;
    private SenseObj _accelerometer;
    private SenseObj _gravity;
    private SenseObj _gyro;

    private static int _jobId;
    private ComponentName _componentName;
    private JobScheduler _jobScheduler;

    private class SenseObj {
        public Sensor _sensor;
        public int _type;
        public String _stringType;
        public int _reportMode;
        public int _maxDelay;
        public int _minDelay;
        public float _maxRange;
        public float _power;
        public float _res;
        public boolean _canWake;

        public  boolean _reporting = false;

        public List<float[]> _history;
        public SenseObj(Sensor sensor)
        {
            this._sensor = sensor;
            this._type = sensor.getType();
            this._stringType = sensor.getStringType();
            this._reportMode = sensor.getReportingMode();
            if (this._reportMode == Sensor.REPORTING_MODE_CONTINUOUS || this._reportMode == Sensor.REPORTING_MODE_ON_CHANGE) {
                this._maxDelay = sensor.getMaxDelay();
                this._minDelay = sensor.getMinDelay();
            }
            this._res = sensor.getResolution();
            this._maxRange = sensor.getMaximumRange();
            this._power = sensor.getPower();
            this._canWake = sensor.isWakeUpSensor();
        }
    }



    public int onStartCommand(Intent intent, int flags, int startId)
    {

        this._powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        this._wakelock = this._powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, TAG);

        this._componentName = new ComponentName(this, SensorWake.class);

        this._sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<Sensor> sensors = _sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < sensors.size(); i++)
        {
            switch(sensors.get(i).getType()) {
                case Sensor.TYPE_PROXIMITY:
                    Log.d(TAG, "Found " + sensors.get(i).getStringType());
                    if (this._accelerometer == null)
                        this._accelerometer = new SenseObj(sensors.get(i));
                    break;
            }
        }

        Log.d(TAG, "MinDelay: " + String.valueOf(this._accelerometer._minDelay));
        Log.d(TAG, "MaxDelay: " + String.valueOf(this._accelerometer._maxDelay));
        Log.d(TAG, "Resolution: " + String.valueOf(this._accelerometer._res));
        Log.d(TAG, "Power: " + String.valueOf(this._accelerometer._power));
        Log.d(TAG, "Can Wake: " + String.valueOf(this._accelerometer._canWake));
        Log.d(TAG, "Report Mode: " + String.valueOf(this._accelerometer._reportMode));
        Log.d(TAG, "Max Range: " + String.valueOf(this._accelerometer._maxRange));

        _jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        JobInfo.Builder builder = new JobInfo.Builder(_jobId++, _componentName);
        builder.setPeriodic(10L);

        _jobScheduler.schedule(builder.build());

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params)
    {
        if (this._accelerometer == null)
            Log.d(TAG, "NO ACCELEROMETER DETECTED");
        else
        {
            this._sensorManager.registerListener(this, this._accelerometer._sensor, SensorManager.SENSOR_DELAY_NORMAL);
            this._accelerometer._reporting = true;
            MainActivity._lastActionTime = SystemClock.elapsedRealtime();
        }
        /**if (this._gyro == null)
            Log.d(TAG, "NO GYROSCOPE DETECTED");
        else
        {
            this._sensorManager.registerListener(this, this._gravity._sensor, SensorManager.SENSOR_DELAY_NORMAL);
            this._gravity._reporting = true;
        }
        if (this._gravity == null)
            Log.d(TAG, "NO GRAVITY DETECTED... FUCK");
        else
        {
            this._sensorManager.registerListener(this, this._gyro._sensor, SensorManager.SENSOR_DELAY_NORMAL);
            this._gyro._reporting = true;
        }*/

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_PROXIMITY:
                //this._sensorManager.unregisterListener(this, this._accelerometer._sensor);
                //Log.d(TAG, "X: " + String.valueOf(event.values[0]) + " Y: " + String.valueOf(event.values[1]) + " Z: " + String.valueOf(event.values[2]));
                //Log.d(TAG, "Accuracy: " + String.valueOf(event.accuracy));
                //for (int i = 0; i < event.values.length; i++)
                    //Log.d(TAG, "Distance: " + String.valueOf(i) + " :: " + String.valueOf(event.values[i]));
                long t = SystemClock.elapsedRealtime();
                if (t - MainActivity._lastActionTime > 50000)
                    this._sensorManager.unregisterListener(this, this._accelerometer._sensor);
                /**this._accelerometer._history.add(event.values);
                if (this._accelerometer._history.size() > 5)
                {
                    this._accelerometer._history.remove(0);
                }
                if (this._accelerometer._history.size() > 3)
                {
                    float[] data1 = this._accelerometer._history.get(this._accelerometer._history.size() - 2);
                    float[] data2 = this._accelerometer._history.get(0);
                    float[] data3 = new float[3];

                    data1[0] = Math.abs(data1[0] - event.values[0]);
                    data1[1] = Math.abs(data1[1] - event.values[1]);
                    data1[2] = Math.abs(data1[2] - event.values[2]);

                    data2[0] = Math.abs(data2[0] - event.values[0]);
                    data2[1] = Math.abs(data2[1] - event.values[1]);
                    data2[2] = Math.abs(data2[2] - event.values[2]);

                    data3[0] = Math.abs(data1[0] - data2[0]);
                    data3[1] = Math.abs(data1[1] - data2[1]);
                    data3[2] = Math.abs(data1[2] - data2[2]);

                    if (data3[0] > Math.abs((data1[0] - data2[0]) / 2F) * 0.25F ||
                        data3[1] > Math.abs((data1[1] - data2[1]) / 2F) * 0.25F ||
                        data3[1] > Math.abs((data1[1] - data2[1]) / 2F) * 0.25F)
                    {
                        MainActivity._lastActionTime = SystemClock.elapsedRealtime();
                        this._wakelock.acquire();
                        this._wakelock.release();
                    }

                }*/
                break;


        }


    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }
}
