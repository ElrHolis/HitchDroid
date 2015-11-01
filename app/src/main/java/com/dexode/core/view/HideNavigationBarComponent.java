package com.dexode.core.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;

/**
 *
 * @author Dawid Drozd
 *
 *         Please check before adding this fragment simply call
 *         isFragmentAvailable.
 *
 *         You must manually call onKeyDown and onWindowFocusChanged.
 *
 */
public class HideNavigationBarComponent extends Fragment implements Runnable {

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setImmersiveMode();

        final View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {

            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    setImmersiveMode();
                }
            }
        });
    }

    public static boolean isFragmentAvailable() {
        return android.os.Build.VERSION.SDK_INT >= 19;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            _handler.removeCallbacks(this);
            _handler.postDelayed(this, 300);
        }
        else {
            _handler.removeCallbacks(this);
        }
    }

    public void onKeyDown(int keyCode) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            _handler.removeCallbacks(this);
            _handler.postDelayed(this, 500);
        }
    }

    @Override
    public void onStop() {
        _handler.removeCallbacks(this);
        super.onStop();
    }

    @Override
    public void run() {
        setImmersiveMode();
    }

    @SuppressLint("NewApi")
    public void setImmersiveMode() {
        setImmersiveMode(getActivity());
    }

    @SuppressLint("NewApi")
    public void setImmersiveMode(Activity activity) {
        activity.getWindow()
                .getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private Handler _handler = new Handler();
}