package com.ecet1012.c80.hitchdroid;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.support.v7.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by pc_mh on 10/31/2015.
 */
public class DeviceOwnerReceiver extends DeviceAdminReceiver {
    @Override
    public void onProfileProvisioningComplete(Context context,Intent intent) {
        Log.d("DeviceOwnerReceiver", "GOT HERE~~~~~~~~~~");
        DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = getComponentName(context);
        manager.setProfileName(componentName, context.getString(R.string.profile_name));

        Intent launch = new Intent(context, MainActivity.class);
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launch);
    }

    public static ComponentName getComponentName(Context context){
        return new ComponentName(context.getApplicationContext(), DeviceOwnerReceiver.class);

    }
}
