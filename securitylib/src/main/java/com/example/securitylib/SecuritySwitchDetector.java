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

    // Variables statiques pour tracker l'état global de l'app
    private static boolean isAppInForeground = true;
    private static int activeActivitiesCount = 0;
    private static Handler delayHandler = new Handler();
    private static Runnable backgroundCheckRunnable;

    /**
     * À appeler dans onResume() de chaque activité
     */
    public static synchronized void onAppResume(Activity activity) {
        activeActivitiesCount++;

        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean firstLaunchDone = prefs.getBoolean(KEY_FIRST_LAUNCH_DONE, false);

        // Annuler la vérification de mise en arrière-plan si elle était prévue
        if (backgroundCheckRunnable != null) {
            delayHandler.removeCallbacks(backgroundCheckRunnable);
            backgroundCheckRunnable = null;
        }

        // Si c'est le premier lancement
        if (!firstLaunchDone) {
            Log.d("SwitchDetector", "⏳ Premier lancement - initialisation");
            prefs.edit()
                    .putBoolean(KEY_FIRST_LAUNCH_DONE, true)
                    .putLong(KEY_LAST_TS, System.currentTimeMillis())
                    .putInt(KEY_COUNT, 0)
                    .apply();
            isAppInForeground = true;
            return;
        }

        // Si l'app était en arrière-plan et qu'on revient
        if (!isAppInForeground) {
            registerSwitch(activity, prefs);
            Log.d("SwitchDetector", "🔄 RETOUR dans l'application depuis l'extérieur");
        } else {
            Log.d("SwitchDetector", "📱 Changement d'activité interne (pas de comptabilisation)");
        }

        isAppInForeground = true;
    }

    /**
     * À appeler dans onPause() de chaque activité
     */
    public static synchronized void onAppPause(Activity activity) {
        activeActivitiesCount--;

        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean firstLaunchDone = prefs.getBoolean(KEY_FIRST_LAUNCH_DONE, false);

        if (!firstLaunchDone) {
            return;
        }

        // Si c'est la dernière activité qui se met en pause
        if (activeActivitiesCount <= 0) {
            activeActivitiesCount = 0; // Protection contre les valeurs négatives

            // Attendre un peu pour voir si une nouvelle activité ne va pas reprendre
            backgroundCheckRunnable = new Runnable() {
                @Override
                public void run() {
                    // Si après le délai, aucune activité n'est revenue au premier plan
                    if (activeActivitiesCount == 0) {
                        isAppInForeground = false;
                        Log.d("SwitchDetector", "🚪 Application mise en arrière-plan");
                    }
                }
            };

            // Délai plus court pour une détection plus réactive
            delayHandler.postDelayed(backgroundCheckRunnable, 100);
        } else {
            Log.d("SwitchDetector", "📱 Pause d'activité (autres activités encore actives)");
        }
    }

    /**
     * Méthode privée pour enregistrer un changement d'application
     */
    private static void registerSwitch(Activity activity, SharedPreferences prefs) {
        long now = System.currentTimeMillis();
        long lastTimestamp = prefs.getLong(KEY_LAST_TS, 0);
        int switchCount = prefs.getInt(KEY_COUNT, 0);

        // Si le dernier changement était dans la fenêtre de temps, incrémenter
        if (now - lastTimestamp < TIME_WINDOW_MS) {
            switchCount++;
        } else {
            // Sinon, commencer un nouveau cycle
            switchCount = 1;
        }

        prefs.edit()
                .putLong(KEY_LAST_TS, now)
                .putInt(KEY_COUNT, switchCount)
                .apply();

        Log.d("SwitchDetector", "🔢 CHANGEMENTS D'APP détectés: " + switchCount + "/" + SWITCH_THRESHOLD);

        if (switchCount >= SWITCH_THRESHOLD) {
            Log.w("SwitchDetector", "🚨 ALERTE: Trop de changements d'application!");
            showSecurityAlertAndExit(activity);
            // Reset après alerte
            prefs.edit()
                    .putInt(KEY_COUNT, 0)
                    .putLong(KEY_LAST_TS, now)
                    .apply();
        }
    }

    /**
     * Affiche l'alerte de sécurité et ferme l'application
     */
    private static void showSecurityAlertAndExit(Activity activity) {
        new Handler(activity.getMainLooper()).post(() -> {
            new AlertDialog.Builder(activity)
                    .setTitle("⚠️ Activité suspecte détectée")
                    .setMessage("Trop de changements d'application ont été détectés dans un court laps de temps.\n\nPour des raisons de sécurité, l'application va se fermer.")
                    .setCancelable(false)
                    .setPositiveButton("Fermer", (dialog, which) -> {
                        activity.finishAffinity();
                        System.exit(0);
                    })
                    .show();
        });
    }

    /**
     * Remet à zéro les compteurs
     */
    public static void reset(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
        isAppInForeground = true;
        activeActivitiesCount = 0;
        if (backgroundCheckRunnable != null) {
            delayHandler.removeCallbacks(backgroundCheckRunnable);
            backgroundCheckRunnable = null;
        }
        Log.d("SwitchDetector", "🔄 Détecteur remis à zéro");
    }

    /**
     * Méthode pour obtenir les statistiques actuelles (pour debug)
     */
    public static void logCurrentStats(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int count = prefs.getInt(KEY_COUNT, 0);
        long lastTs = prefs.getLong(KEY_LAST_TS, 0);

        Log.d("SwitchDetector", "📊 Stats: count=" + count +
                ", lastTs=" + lastTs +
                ", isAppInForeground=" + isAppInForeground +
                ", activeActivities=" + activeActivitiesCount);
    }
}