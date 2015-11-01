package com.ecet1012.c80.hitchdroid.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ecet1012.c80.hitchdroid.MainActivity;
/**
 * Created by pc_mh on 10/30/2015.
 * this class is derived and based off the work of Joisar
 * https://github.com/Joisar/LockScreenApp/blob/master/LockScreenApp/src/com/mehuljoisar/lockscreen/utils/LockscreenIntentReceiver.java
 *
 */
public class LockerReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)
                || intent.getAction().equals(Intent.ACTION_SCREEN_ON)
                || intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent tIntent = new Intent(context, MainActivity.class);
            tIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(tIntent);
        }
    }
}
