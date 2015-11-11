package com.ecet1012.c80.hitchdroid.task;

import android.app.AlarmManager;
import android.content.Intent;
import android.util.Log;

import com.ecet1012.c80.hitchdroid.MainActivity;
import com.ecet1012.c80.hitchdroid.power.PowerState;
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

    public abstract void ScheduleNextAlarm();


    public boolean ShouldSetAlarm() {
        if (GetTaskInterval() > 0) {

        }

        if (this.IsTaskFlagSet(Settings.Task.ACTION, Settings.Task.FLAG_ACTION_RUN_AT_INTERVAL)) {
            if (powerState.GetState() == PowerState.STATE_AWAKE) {
                if (IsTaskFlagSet(Settings.Task.ACTION, Settings.Task.FLAG_ACTION_RUN_WHILE_AWAKE)) {
                    return true;
                }
            } else {
                if (IsTaskFlagSet(Settings.Task.ACTION, Settings.Task.FLAG_ACTION_CAN_WAKE)) {
                    switch (powerState.GetState()) {
                        case PowerState.STATE_IDLE_SHORT:
                            if (GetTaskInterval(powerState.GetState()) != Settings.OFF)
                                return true;
                        case PowerState.STATE_IDLE_MID:
                            if (GetTaskInterval(powerState.GetState()) != Settings.OFF)
                                return true;
                        case PowerState.STATE_IDLE_LONG:
                            if (GetTaskInterval(powerState.GetState()) != Settings.OFF)
                                return true;
                        case PowerState.STATE_NIGHT:
                            if (GetTaskInterval(powerState.GetState()) != Settings.OFF)
                                return true;
                        default:
                            return false;
                    }
                }
            }
        }


        return false;
    }


    public boolean IsTaskFlagSet(String flagGroup, byte flag) {

        try {
            String s = "com.ecet1012.c80.hitchdroid.utils.Settings$task$" + getClass().getSimpleName();
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
            String s = "com.ecet1012.c80.hitchdroid.utils.Settings$task$" + getClass().getSimpleName();
            Class c = Class.forName(s);
            return ((int[]) c.getField("INTERVAL").get(c))[state];

        } catch (Exception e) {
            Log.d(this.getClass().getName(), e.toString());
        }
        return Settings.OFF;
    }


}
