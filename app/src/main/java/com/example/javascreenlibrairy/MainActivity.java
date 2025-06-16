package com.example.javascreenlibrairy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.securitylib.SecurityUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Une seule ligne pour activer toute la sécurité sur l'activité :
        SecurityUtils.enableFullSecurity(this);
    }
}
