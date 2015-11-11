package com.ecet1012.c80.hitchdroid.utils;

import com.ecet1012.c80.hitchdroid.services.TaskManager;
import com.ecet1012.c80.hitchdroid.task.Accelerometer;
import com.ecet1012.c80.hitchdroid.task.Awake;
import com.ecet1012.c80.hitchdroid.task.Camera;
import com.ecet1012.c80.hitchdroid.task.GenericTask;
import com.ecet1012.c80.hitchdroid.task.Gps;
import com.ecet1012.c80.hitchdroid.task.IdleLong;
import com.ecet1012.c80.hitchdroid.task.IdleMid;
import com.ecet1012.c80.hitchdroid.task.IdleShort;
import com.ecet1012.c80.hitchdroid.task.Network;
import com.ecet1012.c80.hitchdroid.task.Night;
import com.ecet1012.c80.hitchdroid.task.Proximity;

/**
 * Created by pc_mh on 11/8/2015.
 */
public class Settings {

    public static final int OFF = -1;
    public static final int MSEC = 1000;
    public static final int SEC = 1000;
    public static final int MIN = SEC * 60;
    public static final int HOUR = MIN * 60;
    public static final int DAY = HOUR * 24;
    public static final int WEEK = DAY * 7;

    public static class Task {

        public static final String ACTION = "ACTION_FLAGS";
        public static final String BATTERY = "BATTERY_FLAGS";

        public static final byte FLAG_BATTERY_EXTREME_TEMP = 0b1000000;
        public static final byte FLAG_BATTERY_STATUS_ERROR = 0b0100000;
        public static final byte FLAG_BATTERY_FULL = 0b0010000;
        public static final byte FLAG_BATTERY_GREAT = 0b0001000;
        public static final byte FLAG_BATTERY_HIGH = 0b0000100;
        public static final byte FLAG_BATTERY_LOW = 0b0000010;
        public static final byte FLAG_BATTERY_CRITICAL = 0b0000001;

        public static final byte FLAG_ACTION_CAN_WAKE = 0b1000000;
        public static final byte FLAG_ACTION_RUN_AT_INTERVAL = 0b0100000;
        public static final byte FLAG_ACTION_REPORTED = 0b0010000;
        public static final byte FLAG_ACTION_LOG_LOCALLY = 0b0001000;
        public static final byte FLAG_ACTION_RUN_WHILE_AWAKE = 0b0000100;
        public static final byte FLAG_ACTION_KEEP_AWAKE = 0b0000010;
        public static final byte FLAG_ACTION_SPECIAL = 0b0000001;

        public static class Accelerometer {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.Accelerometer";
            public static final String INTENT = "HitchDroid_Accelerometer";
            public static final int[] INTERVAL = {OFF, SEC, OFF, OFF, OFF};
            public static final byte BATTERY_FLAGS = 0b1111100;
            public static final byte ACTION_FLAGS = 0b1100010;

        }

        public static class Proximity {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.Proximity";
            public static final String INTENT = "HitchDroid_Proximity";
            public static final int[] INTERVAL = {OFF, OFF, OFF, OFF, OFF};
            public static final byte BATTERY_FLAGS = 0b1111111;
            public static final byte ACTION_FLAGS = 0b1011111;

        }

        public static class Camera {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.Camera";
            public static final String INTENT = "HitchDroid_Camera";
            public static final int[] INTERVAL = {OFF, SEC * 5, OFF, OFF, OFF};
            public static final byte BATTERY_FLAGS = 0b1111100;
            public static final byte ACTION_FLAGS = 0b1100000;
            public static final String SPECIAL_CLASS = "";
        }

        public static class Network {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.Network";
            public static final String INTENT = "HitchDroid_Network";
            public static final int[] INTERVAL = {MIN * 15, HOUR, HOUR, HOUR * 4, OFF};
            public static final byte BATTERY_FLAGS = 0b1111111;
            public static final byte ACTION_FLAGS = 0b0111101;
            public static final String SPECIAL_CLASS = "Network";
        }

        public static class Gps {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.Gps";
            public static final String INTENT = "HitchDroid_GPS";
            public static final int[] INTERVAL = {MIN, MIN, MIN, MIN, MIN};
            public static final byte BATTERY_FLAGS = 0b1111111;
            public static final byte ACTION_FLAGS = 0b0111101;
        }

        public static class Awake {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.Awake";
            public static final String INTENT = "HitchDroid_Awake";
            public static final int[] INTERVAL = {MIN * 2, MIN, SEC * 30, SEC * 15, SEC * 10};
            public static final byte BATTERY_FLAGS = 0b1111111;
            public static final byte ACTION_FLAGS = 0b0011101;

        }

        public static class IdleShort {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.IdleShort";
            public static final String INTENT = "HitchDroid_IdleShort";
            public static final int[] INTERVAL = {MIN * 5, MIN * 5, MIN * 5, MIN * 5, MIN * 5};
            public static final byte BATTERY_FLAGS = 0b1111111;
            public static final byte ACTION_FLAGS = 0b0001001;

        }

        public static class IdleMid {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.IdleMid";
            public static final String INTENT = "HitchDroid_IdleMid";
            public static final int[] INTERVAL = {MIN * 30, MIN * 30, MIN * 30, MIN * 30, MIN * 30};
            public static final byte BATTERY_FLAGS = 0b1111111;
            public static final byte ACTION_FLAGS = 0b0000001;

        }

        public static class IdleLong {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.IdleLong";
            public static final String INTENT = "HitchDroid_IdleLong";
            public static final int[] INTERVAL = {HOUR, HOUR, HOUR, HOUR, HOUR};
            public static final byte BATTERY_FLAGS = 0b1111111;
            public static final byte ACTION_FLAGS = 0b0000001;

        }

        public static class Night {
            public static final String TASK_CLASS = "com.ecet1012.c80.hitchdroid.task.Night";
            public static final String INTENT = "HitchDroid_Night";
            public static final int[] INTERVAL = {HOUR * 11, HOUR * 11, HOUR * 11, HOUR * 11, HOUR * 11};
            public static final byte BATTERY_FLAGS = 0b1111111;
            public static final byte ACTION_FLAGS = 0b0000001;

        }
    }

}
