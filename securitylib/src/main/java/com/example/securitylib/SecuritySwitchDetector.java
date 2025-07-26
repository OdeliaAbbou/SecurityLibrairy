package com.example.securitylib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SecuritySwitchDetector {

    private static final String PREF_NAME = "SecurityPrefs";
    private static final String KEY_COUNT = "switchCount";
    private static final String KEY_LAST_TS = "lastSwitchTimestamp";
    private static final String KEY_FIRST_LAUNCH_DONE = "firstLaunchDone";

    private static final int SWITCH_THRESHOLD = 4;
    private static final long TIME_WINDOW_MS = 60 * 1000; // 1 minute

    private static boolean isAppInForeground = true;
    private static int activeActivitiesCount = 0;
    private static Handler delayHandler = new Handler();
    private static Runnable backgroundCheckRunnable;

    public static synchronized void onAppResume(Activity activity) {
        activeActivitiesCount++;

        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean firstLaunchDone = prefs.getBoolean(KEY_FIRST_LAUNCH_DONE, false);

        if (backgroundCheckRunnable != null) {
            delayHandler.removeCallbacks(backgroundCheckRunnable);
            backgroundCheckRunnable = null;
        }

        if (!firstLaunchDone) {
            Log.d("SwitchDetector", "First launch - initialisation");
            prefs.edit()
                    .putBoolean(KEY_FIRST_LAUNCH_DONE, true)
                    .putLong(KEY_LAST_TS, System.currentTimeMillis())
                    .putInt(KEY_COUNT, 0)
                    .apply();
            isAppInForeground = true;
            return;
        }

        if (!isAppInForeground) {
            registerSwitch(activity, prefs);
            Log.d("SwitchDetector", "RETURN to the application from outside");
        } else {
            Log.d("SwitchDetector", "Internal change of activity (no accounting)");
        }

        isAppInForeground = true;
    }

    public static synchronized void onAppPause(Activity activity) {
        activeActivitiesCount--;

        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean firstLaunchDone = prefs.getBoolean(KEY_FIRST_LAUNCH_DONE, false);

        if (!firstLaunchDone) {
            return;
        }

        // if last activity
        if (activeActivitiesCount <= 0) {
            activeActivitiesCount = 0;

            backgroundCheckRunnable = new Runnable() {
                @Override
                public void run() {
                    if (activeActivitiesCount == 0) {
                        isAppInForeground = false;
                        Log.d("SwitchDetector", "Application moved to background");
                    }
                }
            };

            delayHandler.postDelayed(backgroundCheckRunnable, 100);
        } else {
            Log.d("SwitchDetector", "Activity pause (other activities still active)");
        }
    }

    private static void registerSwitch(Activity activity, SharedPreferences prefs) {
        long now = System.currentTimeMillis();
        long lastTimestamp = prefs.getLong(KEY_LAST_TS, 0);
        int switchCount = prefs.getInt(KEY_COUNT, 0);

        if (now - lastTimestamp < TIME_WINDOW_MS) {
            switchCount++;
        } else {
            switchCount = 1;
        }

        prefs.edit()
                .putLong(KEY_LAST_TS, now)
                .putInt(KEY_COUNT, switchCount)
                .apply();

        Log.d("SwitchDetector", "APP CHANGES detected: " + switchCount + "/" + SWITCH_THRESHOLD);

        if (switchCount >= SWITCH_THRESHOLD) {
            Log.w("SwitchDetector", "WARNING: Too many application changes!");
            showSecurityAlertAndExit(activity);
            // Reset after alerte
            prefs.edit()
                    .putInt(KEY_COUNT, 0)
                    .putLong(KEY_LAST_TS, now)
                    .apply();
        }
    }

    private static void showSecurityAlertAndExit(Activity activity) {
        new Handler(activity.getMainLooper()).post(() -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Suspicious activity detected")
                    .setMessage("Too many application changes have been detected in a short period of time.\n" +
                            "\n" +
                            "For security reasons, the application will close..")
                    .setCancelable(false)
                    .setPositiveButton("Close", (dialog, which) -> {
                        activity.finishAffinity();
                        System.exit(0);
                    })
                    .show();
        });
    }

    public static void reset(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        isAppInForeground = true;
        activeActivitiesCount = 0;
        if (backgroundCheckRunnable != null) {
            delayHandler.removeCallbacks(backgroundCheckRunnable);
            backgroundCheckRunnable = null;
        }
        Log.d("SwitchDetector", "Reset detector");
    }

    public static void logCurrentStats(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int count = prefs.getInt(KEY_COUNT, 0);
        long lastTs = prefs.getLong(KEY_LAST_TS, 0);

        Log.d("SwitchDetector", "Stats: count=" + count +
                ", lastTs=" + lastTs +
                ", isAppInForeground=" + isAppInForeground +
                ", activeActivities=" + activeActivitiesCount);
    }
}