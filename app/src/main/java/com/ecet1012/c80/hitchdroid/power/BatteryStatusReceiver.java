package com.ecet1012.c80.hitchdroid.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

/**
 * Created by pc_mh on 11/7/2015.
 */
public class BatteryStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryStatusReceiver";

    public static final String INIT_BATTERY_INTENT = "HitchDroid_initBatteryStatus";
    public static final String REQUEST_REPORT_INTENT = "HitchDroid_requestBatteryStatus";


    private static BatteryManager batteryManager;

    private static boolean initialized = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_BATTERY_LOW:

                break;
            case Intent.ACTION_BATTERY_OKAY:
                break;
            case Intent.ACTION_POWER_CONNECTED:
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                break;
            case REQUEST_REPORT_INTENT:

                break;

            case INIT_BATTERY_INTENT:
                if (initialized)
                    return;
                initialized = true;
                batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);


                break;


        }
    }


}
