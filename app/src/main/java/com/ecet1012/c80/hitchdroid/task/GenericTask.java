package com.ecet1012.c80.hitchdroid.task;

import android.app.AlarmManager;
import android.content.Intent;
import android.util.Log;

import com.ecet1012.c80.hitchdroid.MainActivity;
import com.ecet1012.c80.hitchdroid.power.PowerState;
import com.ecet1012.c80.hitchdroid.services.TaskManager;
import com.ecet1012.c80.hitchdroid.utils.Settings;

/**
 * Created by pc_mh on 11/9/2015.7
 */
public abstract class GenericTask {

    protected PowerState powerState;

    public GenericTask() {

        this.powerState = MainActivity.powerState;

    }


    public abstract void DoTask();

    public abstract void DoSpecial();

    protected void Done() {
        Log.d(this.getClass().getSimpleName(), "<<<<<<<<< DONE");
        TaskManager.CompleteTask(this.getClass().getSimpleName());
    }

    public void ScheduleNextAlarm() {

        // override this method if you want to set an alarm at a specific time
        // such as task.night does


        Log.d(this.getClass().getSimpleName(), "<<<<< ScheduleNextAlarm " + String.valueOf(this.GetTaskInterval()));
        if (ShouldSetAlarm())
            TaskManager.SetAlarm(this.getClass().getSimpleName(), this.GetTaskInterval(), true);
    }

    public boolean ShouldSetAlarm() {

        // only checks if you need to run at interval


        float percentBattery = powerState.GetPercentBattery();
        boolean CanRunAtBatteryPercent = false;
        if (percentBattery >= 99)
            CanRunAtBatteryPercent = IsTaskFlagSet(Settings.Task.BATTERY, Settings.Task.FLAG_BATTERY_FULL);
        else if (percentBattery >= 80 && percentBattery < 99)
            CanRunAtBatteryPercent = IsTaskFlagSet(Settings.Task.BATTERY, Settings.Task.FLAG_BATTERY_GREAT);
        else if (percentBattery >= 50 && percentBattery < 80)
            CanRunAtBatteryPercent = IsTaskFlagSet(Settings.Task.BATTERY, Settings.Task.FLAG_BATTERY_HIGH);
        else if (percentBattery >= 20 && percentBattery < 50)
            CanRunAtBatteryPercent = IsTaskFlagSet(Settings.Task.BATTERY, Settings.Task.FLAG_BATTERY_LOW);
        else if (percentBattery < 20)
            CanRunAtBatteryPercent = IsTaskFlagSet(Settings.Task.BATTERY, Settings.Task.FLAG_BATTERY_CRITICAL);


        if (this.IsTaskFlagSet(Settings.Task.ACTION, Settings.Task.FLAG_ACTION_RUN_AT_INTERVAL)) {
            if (powerState.GetState() == PowerState.STATE_AWAKE) {
                if (IsTaskFlagSet(Settings.Task.ACTION, Settings.Task.FLAG_ACTION_RUN_WHILE_AWAKE)) {
                    return true;
                }
            } else {

                switch (powerState.GetState()) {
                    case PowerState.STATE_IDLE_SHORT:
                        if (CanRunAtBatteryPercent && GetTaskInterval(powerState.GetState()) != Settings.OFF)
                            return true;
                    case PowerState.STATE_IDLE_MID:
                        if (CanRunAtBatteryPercent && GetTaskInterval(powerState.GetState()) != Settings.OFF)
                            return true;
                    case PowerState.STATE_IDLE_LONG:
                        if (CanRunAtBatteryPercent && GetTaskInterval(powerState.GetState()) != Settings.OFF)
                            return true;
                    case PowerState.STATE_NIGHT:
                        if (CanRunAtBatteryPercent && GetTaskInterval(powerState.GetState()) != Settings.OFF)
                            return true;
                    default:
                        return false;
                }
            }
        }
        return false;
    }


    public boolean IsTaskFlagSet(String flagGroup, byte flag) {

        try {
            String s = "com.ecet1012.c80.hitchdroid.utils.Settings$Task$" + getClass().getSimpleName();
            Class c = Class.forName(s);
            return (c.getField(flagGroup).getByte(c) & flag) == flag;

        } catch (Exception e) {
            Log.d(this.getClass().getName(), e.toString());
        }
        return false;
    }

    public int GetTaskInterval() {
        return GetTaskInterval(powerState.GetState());
    }

    public int GetTaskInterval(int state) {
        try {
            String s = "com.ecet1012.c80.hitchdroid.utils.Settings$Task$" + getClass().getSimpleName();
            Class c = Class.forName(s);
            return ((int[]) c.getField("INTERVAL").get(c))[state];

        } catch (Exception e) {
            Log.d(this.getClass().getName(), e.toString());
        }
        return Settings.OFF;
    }


}
