package com.example.javascreenlibrairy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.securitylib.SecurityUtils;
import com.example.securitylib.SecurityConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SecurityConfig config = new SecurityConfig.Builder()
                .disableScreenshots(true)
                .disableCopyPaste(true)
                .build();

        SecurityUtils.applySecurity(this, config);
    }
}
