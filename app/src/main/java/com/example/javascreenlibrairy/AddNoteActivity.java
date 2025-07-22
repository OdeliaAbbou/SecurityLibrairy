package com.example.javascreenlibrairy;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securitylib.SecurityConfig;
import com.example.securitylib.SecuritySwitchDetector;
import com.example.securitylib.SecurityUtils;
import com.google.firebase.database.FirebaseDatabase;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private Button btnSave;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // lib sécurité
        SecurityConfig config = new SecurityConfig.Builder()
                .disableScreenshots(false)
                .disableCopyPaste(true)
                .disableRecentAppsPreview(false)
                .build();
        SecurityUtils.applySecurity(this, config);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE); //annule Screen + AppEnFond


        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSaveNote);

        btnSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String content = editContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir le titre et le contenu.", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = FirebaseDatabase.getInstance().getReference("notes").push().getKey();
            Note note = new Note(id, title, content);

            FirebaseDatabase.getInstance().getReference("notes").child(id).setValue(note)
                    .addOnSuccessListener(task -> finish());
        });
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
