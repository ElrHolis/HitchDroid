package com.ecet1012.c80.hitchdroid.utils;

import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.ecet1012.c80.hitchdroid.R;

/**
 * Created by pc_mh on 10/30/2015.
 * this class is derived and based on the work of Joisar
 * https://github.com/Joisar/LockScreenApp/blob/master/LockScreenApp/src/com/mehuljoisar/lockscreen/utils/LockscreenUtils.java
 */
public class LockerUtils {

    private OverlayDialog _OverlayDialog;
    private OnLockStatusChangedListener _lockStatusChangedListener;

    public interface OnLockStatusChangedListener
    {
        public void onLockStatusChanged(boolean isLocked);
    }

    public LockerUtils()
    {
        reset();
    }

    public void lock(AppCompatActivity appCompatActivity) {
        if(_OverlayDialog == null)
        {
            _OverlayDialog = new OverlayDialog(appCompatActivity);
            _OverlayDialog.show();
            _lockStatusChangedListener = (OnLockStatusChangedListener) appCompatActivity;
        }
    }

    public void unlock() {
        if(_OverlayDialog != null)
        {
            _OverlayDialog.dismiss();
            _OverlayDialog = null;
            if(_lockStatusChangedListener != null)
            {
                _lockStatusChangedListener.onLockStatusChanged(false);
            }
        }
    }

    public void reset()
    {
        if(_OverlayDialog != null)
        {
            _OverlayDialog.dismiss();
            _OverlayDialog = null;
        }
    }

    private static class OverlayDialog extends AlertDialog {
        public OverlayDialog(AppCompatActivity appCompatActivity) {
            super(appCompatActivity, R.style.FullscreenTheme);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.type = LayoutParams.TYPE_SYSTEM_ERROR;
            params.dimAmount = 0.0f;
            params.width = 0;
            params.height = 0;
            params.gravity = Gravity.BOTTOM;
            getWindow().setAttributes(params);
            getWindow().setFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED | LayoutParams.FLAG_NOT_TOUCH_MODAL, 0xffffff);
            setOwnerActivity(appCompatActivity);
            setCancelable(false);
        }

        public final boolean dispatchTouchEvent(MotionEvent motionEvent)
        {
            return false;
        }
    }


}
