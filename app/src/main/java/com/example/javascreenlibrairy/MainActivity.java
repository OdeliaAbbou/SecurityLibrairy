package com.example.javascreenlibrairy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.securitylib.SecuritySwitchDetector;
import com.example.securitylib.SecurityUtils;
import com.example.securitylib.SecurityConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Exemple d'utilisation de la configuration
        SecurityConfig config = new SecurityConfig.Builder()
                .disableScreenshots(true)
                .disableCopyPaste(true)
                .disableRecentAppsPreview(true)
                .build();

        SecurityUtils.applySecurity(this, config);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        SecuritySwitchDetector.register(this);
    }

}