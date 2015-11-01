package com.ecet1012.c80.hitchdroid;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.os.SystemClock;

import com.ecet1012.c80.hitchdroid.services.SensorWake;
import com.ecet1012.c80.hitchdroid.utils.TaskerService;

public class MainActivity extends AppCompatActivity {

    public static final int MSG_UNCOLOR_START = 0;
    public static final int MSG_UNCOLOR_STOP = 1;
    public static final int MSG_SERVICE_OBJ = 2;


    public static long _lastActionTime;

    private OnNavigationBarChangeListener _onNavigationBarChangeListener;
    private DevicePolicyManager _devicePolicyManager;

    private ComponentName _SensorWakeComponent;
    private SensorWake _SensorWakeService;


    private interface OnNavigationBarChangeListener extends View.OnSystemUiVisibilityChangeListener
    {
        @Override
        void onSystemUiVisibilityChange(int isVisible);

    }

    @Override
    public void onAttachedToWindow()
    {
        getWindow().addFlags(
                LayoutParams.FLAG_FULLSCREEN
                        | LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | LayoutParams.FLAG_KEEP_SCREEN_ON
                        | LayoutParams.FLAG_DISMISS_KEYGUARD
        );
        super.onAttachedToWindow();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        MainActivity._lastActionTime = SystemClock.elapsedRealtime();

        this._devicePolicyManager = (DevicePolicyManager) this.getSystemService(Activity.DEVICE_POLICY_SERVICE);
        IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        this._devicePolicyManager.addPersistentPreferredActivity(DeviceOwnerReceiver.getComponentName(this), filter, DeviceOwnerReceiver.getComponentName(this));
        String[] packages = {this.getPackageName()};
        this._devicePolicyManager.setLockTaskPackages(DeviceOwnerReceiver.getComponentName(this), packages);
        startLockTask();

        this._SensorWakeComponent = new ComponentName(this, SensorWake.class);
        startService(new Intent(this, SensorWake.class));

        this._onNavigationBarChangeListener = new NavigationListener();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(this._onNavigationBarChangeListener);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

    }

    @Override
    public void onResume()
    {
        super.onResume();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }


    private class NavigationListener implements OnNavigationBarChangeListener
    {
        @Override
        public void onSystemUiVisibilityChange(int visibility)
        {


            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
            Log.d("NavigationListener", "Something happened");

        }
    }
    }


