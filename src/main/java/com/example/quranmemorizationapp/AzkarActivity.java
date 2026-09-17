package com.example.quranmemorizationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AzkarActivity extends AppCompatActivity {

    View darkOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar);

        darkOverlay = findViewById(R.id.darkOverlay);

        SharedPreferences sharedPreferences =
                getSharedPreferences("SettingsPrefs", MODE_PRIVATE);

        boolean isDarkModeOn =
                sharedPreferences.getBoolean("darkMode", false);

        if (isDarkModeOn) {
            darkOverlay.setVisibility(View.VISIBLE);
        } else {
            darkOverlay.setVisibility(View.GONE);
        }
    }
}