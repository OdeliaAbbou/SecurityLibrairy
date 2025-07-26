package com.example.javascreenlibrairy;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.securitylib.SecurityConfig;
import com.example.securitylib.SecuritySwitchDetector;
import com.example.securitylib.SecurityUtils;

public abstract class BaseSecureActivity extends AppCompatActivity {

    protected abstract SecurityConfig provideSecurityConfig();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SecurityConfig config = provideSecurityConfig();
        SecurityUtils.applySecurity(this, config);
        Log.d(getClass().getSimpleName(), "Security applied via BaseSecureActivity");
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