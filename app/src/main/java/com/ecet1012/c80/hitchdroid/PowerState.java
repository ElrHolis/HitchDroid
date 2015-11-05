package com.ecet1012.c80.hitchdroid;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc_mh on 11/5/2015.
 */
public class PowerState {
    private static final String TAG = "PowerState";
    private static final String WAKE = "HitchDROID_WAKE";

    public static final String NO_WAKE = "none";
    public static final String PARTIAL_WAKE = "partial";
    public static final String DIM_WAKE = "dim";
    public static final String BRIGHT_WAKE = "bright";


    private static PowerManager powerManager;
    private static WakeLock partialWakeLock;
    private static WakeLock dimWakeLock;
    private static WakeLock brightWakeLock;

    private static WakeLock updateWakeLock;

    private List<WakeHolder> wakeHolders;

    private static String highestAllowedWakeLock = BRIGHT_WAKE;
    private static String activeWakeLock = NO_WAKE;

    public PowerState() {
        powerManager = (PowerManager) MainActivity.mainContext.getSystemService(Context.POWER_SERVICE);
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE);
        dimWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, WAKE);
        brightWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, WAKE);

        updateWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE);
        CheckPowerStatus();
        wakeHolders = new ArrayList<WakeHolder>();
    }

    public PowerReport CheckPowerStatus() {

        return new PowerReport();
    }


    private boolean SetHighestAllowedWakeLock(String type) {
        if (!GetActiveWakeLock().equals(NO_WAKE)) {
            if (IsHigherWakeLock(type, GetActiveWakeLock())) {
                highestAllowedWakeLock = type;
                UpgradeActiveWakeLock(type);
            } else {
                highestAllowedWakeLock = type;
                DowngradeWakeLocks(type);
            }

            return true;
        }
        return false;

    }

    private void DowngradeWakeLocks(String type) {
        boolean needUpdate = false;
        for (WakeHolder wh : wakeHolders) {
            if (IsHigherWakeLock(wh.trueType, type)) {
                int index = wakeHolders.indexOf(wh);
                wh.trueType = type;
                needUpdate = true;
                wakeHolders.set(index, wh);
            }
        }
        if (needUpdate)
            UpdateActiveWakeLock();
    }

    private void UpgradeActiveWakeLock(String type) {
        boolean needUpdate = false;
        String toType = NO_WAKE;
        for (WakeHolder wh : wakeHolders) {
            if (!wh.type.equals(wh.trueType)) {

                int index = wakeHolders.indexOf(wh);
                if (IsHigherWakeLock(wh.type, type)) {
                    wh.trueType = wh.type;
                    if (IsHigherWakeLock(wh.type, toType))
                        toType = wh.type;
                } else {
                    wh.trueType = type;
                    if (IsHigherWakeLock(type, toType))
                        toType = type;
                }
                wakeHolders.set(index, wh);
                needUpdate = true;
            }
        }
        if (needUpdate)
            UpdateActiveWakeLock();
    }


    private void UpdateActiveWakeLock() {
        //Log.d(TAG, "UPDATE POWER STATE");
        if (!updateWakeLock.isHeld())
            updateWakeLock.acquire();
        try {

            String highest = GetHighestWakeLockByHolder();
            if (!activeWakeLock.equals(NO_WAKE)) {
                //Log.d(TAG, "     FROM: " + activeWakeLock);
                WakeLock release = GetWakeLockByType(activeWakeLock);
                if (release.isHeld())
                    release.release();
            }

            if (!highest.equals(NO_WAKE)) {
                //Log.d(TAG, "     TO: " + highest);
                WakeLock acquire = GetWakeLockByType(highest);
                if (!acquire.isHeld()) {
                    acquire.acquire();
                    activeWakeLock = highest;
                    //Log.d(TAG, ">> " + activeWakeLock);
                }
            }


        } catch (WakeException e) {
            Log.d(TAG, "Wake Exception");
        }

        if (updateWakeLock.isHeld())
            updateWakeLock.release();

        //Log.d(TAG, "UPDATE_LOCK_STATE: " + String.valueOf(updateWakeLock.isHeld()));
    }


    private WakeLock GetWakeLockByType(String type) throws WakeException {
        switch (type) {
            case PARTIAL_WAKE:
                return partialWakeLock;
            case DIM_WAKE:
                return dimWakeLock;
            case BRIGHT_WAKE:
                return brightWakeLock;
        }
        throw new WakeException();
    }

    public String GetHighestAllowedWakeLock() {
        return highestAllowedWakeLock;
    }

    private boolean IsHigherWakeLock(String type1, String type2) {
        switch (type1) {
            case PARTIAL_WAKE:
                if (type2.equals(NO_WAKE))
                    return true;
                break;
            case DIM_WAKE:
                if (type2.equals(PARTIAL_WAKE) ||
                        type2.equals(NO_WAKE))
                    return true;
                break;
            case BRIGHT_WAKE:
                if (type2.equals(PARTIAL_WAKE) ||
                        type2.equals(DIM_WAKE) ||
                        type2.equals(NO_WAKE))
                    return true;
                break;
        }

        return false;
    }


    private String GetHighestWakeLockByHolder() {
        String highest = NO_WAKE;
        for (WakeHolder wh : wakeHolders) {
            if (IsHigherWakeLock(wh.trueType, highest))
                highest = wh.trueType;
        }

        return highest;
    }


    public String GetActiveWakeLock() {
        return activeWakeLock;
    }


    public boolean ReleaseWakeLock(String name) {

        boolean work = false;
        for (WakeHolder wh : wakeHolders) {
            if (wh.name.equals(name)) {
                wakeHolders.remove(wh);
                UpdateActiveWakeLock();

                work = true;
            }
        }

        return work;
    }

    public boolean ReleaseWakeLock(String name, String type) {
        int index = IsHoldingWakeIndex(name);
        if (index >= 0) {
            WakeHolder wh = wakeHolders.get(index);
            if (wh.type.equals(type)) {
                wakeHolders.remove(index);
                //Log.d(TAG, "RELEASE: " + name + " | " + type + " {" + wh.trueType + "}");
                UpdateActiveWakeLock();
                return true;
            }
        }


        return false;
    }


    public boolean AcquireWakeLock(String name, String type) {
        if (!IsHoldingWake(name, type)) {
            WakeHolder wh = new WakeHolder(name, type);
            if (IsHigherWakeLock(type, highestAllowedWakeLock))
                wh.trueType = highestAllowedWakeLock;
            //Log.d(TAG, "ACQUIRE: " + name + " | " + type + " {" + wh.trueType + "} <" + wakeHolders.size() + ">" );
            wakeHolders.add(wh);
            UpdateActiveWakeLock();
            return true;
        }

        return false;
    }


    public boolean IsHoldingWake(String name) {
        return IsHoldingWakeIndex(name) > 0;
    }

    public boolean IsHoldingWake(String name, String type) {
        int index = IsHoldingWakeIndex(name, type);
        if (index >= 0) {
            if (wakeHolders.get(index).type.equals(type))
                return true;
        }


        return false;
    }

    private int IsHoldingWakeIndex(String name, String type) {
        for (int i = 0; i < wakeHolders.size(); i++) {
            WakeHolder wh = wakeHolders.get(i);
            if (wh.name.equals(name) && wh.type.equals(type))
                return i;
        }
        return -1;
    }

    private int IsHoldingWakeIndex(String name) {
        for (int i = 0; i < wakeHolders.size(); i++) {
            if (wakeHolders.get(i).name.equals(name)) {
                return i;
            }

        }
        return -1;
    }


    private class WakeHolder {
        public String name;
        public long startTime = -1;
        public long endTime = -1;
        public String type = PowerState.PARTIAL_WAKE;
        public String trueType = PowerState.PARTIAL_WAKE;
        public int flags;

        public WakeHolder(String name, String type) {
            this.name = name;
            this.type = type;
            this.trueType = type;
        }

    }

    private class PowerReport {

    }

    private class WakeException extends Exception {
    }


}
