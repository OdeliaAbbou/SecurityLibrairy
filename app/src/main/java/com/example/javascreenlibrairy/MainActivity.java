package com.example.javascreenlibrairy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.securitylib.SecurityUtils;

public class MainActivity extends AppCompatActivity {

    private EditText editText1, editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer les EditText
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        // Activer la sécurité
        SecurityUtils.disableScreenshots(this);
        SecurityUtils.disableCopyPaste(editText1);
        SecurityUtils.disableCopyPaste(editText2);
        SecurityUtils.clearClipboard(this);
    }
}
