package com.example.quranmemorizationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    Spinner spinnerSurah;

    Button btnStartMemorize;

    View darkOverlay;

    String[] surahNames = {
            "الفاتحة",
            "الإخلاص",
            "الفلق",
            "الناس"
    };

    int[] surahNumbers = {
            1,
            112,
            113,
            114
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selection);

        darkOverlay =
                findViewById(R.id.darkOverlay);

        SharedPreferences sharedPreferences =
                getSharedPreferences(
                        "SettingsPrefs",
                        MODE_PRIVATE
                );

        boolean isDarkModeOn =
                sharedPreferences.getBoolean(
                        "darkMode",
                        false
                );

        if (isDarkModeOn) {

            darkOverlay.setVisibility(
                    View.VISIBLE
            );

        } else {

            darkOverlay.setVisibility(
                    View.GONE
            );
        }

        spinnerSurah =
                findViewById(R.id.spinnerSurah);

        btnStartMemorize =
                findViewById(R.id.btnStartMemorize);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        surahNames
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerSurah.setAdapter(adapter);

        btnStartMemorize.setOnClickListener(v -> {

            int position =
                    spinnerSurah.getSelectedItemPosition();

            Intent intent =
                    new Intent(
                            SelectionActivity.this,
                            MemorizeActivity.class
                    );

            intent.putExtra(
                    "SURAH_NAME",
                    surahNames[position]
            );

            intent.putExtra(
                    "SURAH_NUMBER",
                    surahNumbers[position]
            );

            startActivity(intent);
        });
    }
}