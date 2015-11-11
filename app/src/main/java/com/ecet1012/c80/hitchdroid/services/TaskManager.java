package com.ecet1012.c80.hitchdroid.services;

import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;

import com.ecet1012.c80.hitchdroid.MainActivity;
import com.ecet1012.c80.hitchdroid.power.PowerState;

import com.ecet1012.c80.hitchdroid.task.GenericTask;
import com.ecet1012.c80.hitchdroid.utils.Settings;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * Created by pc_mh on 10/30/2015.
 */
public class TaskManager extends BroadcastReceiver {
    private static final String TAG = "TaskManager";


    public static final String INIT_INTENT = "HitchDroid_InitTaskManager";

    private static Map<String, Task> tasks;


    private static PowerState powerState;


    private static boolean initialized = false;




    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive " + intent.getAction());
        if (intent.getAction().equals(INIT_INTENT))
            Initialize();
        else {

            Task t = tasks.get(intent.getStringExtra("name"));
            powerState.AcquireWakeLock(t.taskName, PowerState.PARTIAL_WAKE);
            t.DoTask();
            if (t.worker.IsTaskFlagSet(Settings.Task.ACTION, Settings.Task.FLAG_ACTION_SPECIAL))
                t.DoSpecial();
            if (t.worker.ShouldSetAlarm() && t.worker.GetTaskInterval() > 0)
                MainActivity.alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + t.worker.GetTaskInterval(), t.GetPendingIntent());

        }

        powerState.UpdateStatus();

    }

    public void Initialize() {

        if (initialized)
            return;
        initialized = true;
        powerState = MainActivity.powerState;
        tasks = new ArrayMap<String, Task>();
        Log.d(TAG, "Initialized Task Manager");
        RegisterTasks();
        InitTasks();
    }

    private void InitTasks() {
        try {

            for (Map.Entry<String, Task> taskEntry : tasks.entrySet()) {
                //Log.d(TAG, "INIT_TASK" + taskEntry.getKey());
                taskEntry.getValue().worker.ScheduleNextAlarm();

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "INIT_TASK " + e.toString());
        }
    }


    private void RegisterTasks() {

        try {
            Class[] taskClasses = Settings.Task.class.getClasses();
            for (Class c : taskClasses) {

                //Log.d(TAG, c.getName() + " registered");
                tasks.put(c.getSimpleName(), new Task(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
    }

    public static void SetAlarm(String taskName, int number, boolean isInterval) {
        if (isInterval)
            MainActivity.alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + number, tasks.get(taskName).GetPendingIntent());
        else
            MainActivity.alarmManager.setExact(AlarmManager.RTC_WAKEUP, number, tasks.get(taskName).GetPendingIntent());
    }

    public static void CompleteTask(String taskName) {
        powerState.ReleaseWakeLock(taskName, PowerState.PARTIAL_WAKE);
    }


    private class Task {
        public String taskName;
        private Intent intent;
        private String intentName;
        private PendingIntent pendingIntent;
        private Class taskClass;
        private GenericTask worker;

        public Task(Class taskClass) {
            try {
                taskName = taskClass.getSimpleName();

                //Log.d("TASK_MAKE", "NAME " + taskName);
                this.taskClass = Class.forName((String) taskClass.getField("TASK_CLASS").get(taskClass.getClass()));
                //Log.d("TASK_MAKE", "CLASS " + taskClass.toString());
                worker = (GenericTask) this.taskClass.getConstructor().newInstance();
                //Log.d("TASK_MAKE", "WORKER " + worker.toString());
                intentName = (String) taskClass.getField("INTENT").get(taskClass.getClass());
                //Log.d("TASK_MAKE", "INTENT NAME " + intentName);
                intent = new Intent(this.intentName).putExtra("name", taskName);
                //Log.d("TASK_MAKE", "INTENT " + intent.toString());
                pendingIntent = PendingIntent.getBroadcast(MainActivity.mainContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //Log.d("TASK_MAKE", "PENDING " + pendingIntent.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "TASK " + e.toString());
            }
        }

        public String GetName() {
            return this.taskName;
        }

        public String GetIntent() {
            return this.intentName;
        }

        public PendingIntent GetPendingIntent() {
            return this.pendingIntent;
        }

        public void DoTask() {
            worker.DoTask();
        }

        public void DoSpecial() {
            worker.DoSpecial();
        }
    }
}
