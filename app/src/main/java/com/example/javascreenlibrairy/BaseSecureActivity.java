package com.example.javascreenlibrairy;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securitylib.SecurityConfig;
import com.example.securitylib.SecuritySwitchDetector;
import com.example.securitylib.SecurityUtils;

public abstract class BaseSecureActivity extends AppCompatActivity {

    protected abstract SecurityConfig provideSecurityConfig();

    private SecurityConfig config;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = provideSecurityConfig();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SecurityUtils.applySecurity(this, config);

        if (!config.isBlockScreenshots() && !config.isBlockRecentAppsPreview()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }

        Log.d(getClass().getSimpleName(), "Security applied via BaseSecureActivity (onPostCreate)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SecuritySwitchDetector.onAppResume(this);
        Log.d(getClass().getSimpleName(), "Activity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SecuritySwitchDetector.onAppPause(this);
        Log.d(getClass().getSimpleName(), "Activity paused");
    }
}
