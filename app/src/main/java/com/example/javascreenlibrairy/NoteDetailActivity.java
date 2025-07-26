package com.example.javascreenlibrairy;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.securitylib.SecurityConfig;
import com.example.securitylib.SecuritySwitchDetector;
import com.example.securitylib.SecurityUtils;

public class NoteDetailActivity extends BaseSecureActivity {

    @Override
    protected SecurityConfig provideSecurityConfig() {
        return new SecurityConfig.Builder()
                .disableScreenshotsAndRecentApps(true)
                .disableCopyPaste(true)
                .build();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        TextView titleView = findViewById(R.id.detailTitle);
        TextView contentView = findViewById(R.id.detailContent);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        titleView.setText(title);
        contentView.setText(content);
    }



}
