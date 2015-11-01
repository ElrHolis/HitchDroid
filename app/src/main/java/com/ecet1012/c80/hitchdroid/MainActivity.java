package com.ecet1012.c80.hitchdroid;



import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.app.ActionBar.OnMenuVisibilityListener;

import com.ecet1012.c80.hitchdroid.utils.LockerService;
import com.ecet1012.c80.hitchdroid.utils.LockerUtils;

public class MainActivity extends AppCompatActivity implements LockerUtils.OnLockStatusChangedListener {

    private LockerUtils _LockerUtils;
    private OnMenuVisibleListener _onMenuVisibilityListener;
    private OnNavigationBarChangeListener _onNavigationBarChangeListener;

    private DrawerLayout _drawerLayout;

    private DevicePolicyManager _devicePolicyManager;

    private interface OnMenuVisibleListener extends OnMenuVisibilityListener
    {
        @Override
        void onMenuVisibilityChanged(boolean isVisible);
    }

    private interface OnNavigationBarChangeListener extends View.OnSystemUiVisibilityChangeListener
    {
        @Override
        void onSystemUiVisibilityChange(int isVisible);

    }

    @Override
    public void onAttachedToWindow()
    {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );
        super.onAttachedToWindow();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }


    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                || (keyCode == KeyEvent.KEYCODE_POWER)
                || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
                || (keyCode == KeyEvent.KEYCODE_CAMERA)) {
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {

            return true;
        }

        return false;

    }


    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP
                || (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
                || (event.getKeyCode() == KeyEvent.KEYCODE_POWER)) {
            return false;
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)) {

            return true;
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);



        this._devicePolicyManager = (DevicePolicyManager) this.getSystemService(Activity.DEVICE_POLICY_SERVICE);
        IntentFilter filter = new IntentFilter(Intent.ACTION_MAIN);
        filter.addCategory(Intent.CATEGORY_HOME);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        this._devicePolicyManager.addPersistentPreferredActivity(DeviceOwnerReceiver.getComponentName(this), filter, DeviceOwnerReceiver.getComponentName(this));
        String[] packages = {this.getPackageName()};
        this._devicePolicyManager.setLockTaskPackages(DeviceOwnerReceiver.getComponentName(this), packages);
        startLockTask();



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

    @Override
    public void onBackPressed()
    {
        return;
    }

    @Override
    public void onLockStatusChanged(boolean isLocked) {
        if (!isLocked)
            finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        _LockerUtils.unlock();
    }

    public void unlockDevice(Activity activity)
    {
        activity.finish();
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
            Log.v("NavigationListener", "Something happend");

        }
    }
    }


