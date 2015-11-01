package com.ecet1012.c80.hitchdroid.utils;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.util.Log;

import com.ecet1012.c80.hitchdroid.MainActivity;

import java.util.List;

/**
 * Created by pc_mh on 11/1/2015.
 */
public class TaskerService extends JobService {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Messenger callback = intent.getParcelableExtra("messenger");
        Message msg = Message.obtain();
        msg.what = MainActivity.MSG_SERVICE_OBJ;
        msg.obj = this;
        try {
            callback.send(msg);
        }
        catch (RemoteException e)
        {
            Log.e("Tasker", "ERROR: shit happened");
        }

        initGyroWakeJob();



        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters)
    {
        return false;
    }


    public void newJob(JobInfo jobInfo)
    {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    private void initGyroWakeJob()
    {

    }

    private void initGPSJob()
    {

    }

    private void initReportingJob()
    {

    }

    private void initCameraWakeJob()
    {

    }

}
