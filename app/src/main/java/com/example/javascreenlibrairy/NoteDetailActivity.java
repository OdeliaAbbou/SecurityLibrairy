package com.example.javascreenlibrairy;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securitylib.SecurityConfig;
import com.example.securitylib.SecuritySwitchDetector;
import com.example.securitylib.SecurityUtils;

public class NoteDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        // Sécurité (lib perso)
        SecurityConfig config = new SecurityConfig.Builder()
                .disableScreenshots(true)
                .disableCopyPaste(true)
                .disableRecentAppsPreview(true)
                .build();
        SecurityUtils.applySecurity(this, config);

        TextView titleView = findViewById(R.id.detailTitle);
        TextView contentView = findViewById(R.id.detailContent);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        titleView.setText(title);
        contentView.setText(content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SecuritySwitchDetector.onAppResume(this);
        Log.d(getClass().getSimpleName(), "🟢 Activity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SecuritySwitchDetector.onAppPause(this);
        Log.d(getClass().getSimpleName(), "🟡 Activity paused");
    }


}
