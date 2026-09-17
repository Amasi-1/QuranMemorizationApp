package com.example.quranmemorizationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    ImageButton btnStart, btnSettings, btnAzkar;
    View darkOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnStart = findViewById(R.id.btnStart);
        btnSettings = findViewById(R.id.btnSettings);
        btnAzkar = findViewById(R.id.btnAzkar);

        btnStart.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SelectionActivity.class));
        });

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });

        btnAzkar.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AzkarActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences =
                getSharedPreferences("SettingsPrefs", MODE_PRIVATE);

        boolean isDarkModeOn =
                sharedPreferences.getBoolean("darkMode", false);

        if (darkOverlay != null) {
            darkOverlay.setVisibility(isDarkModeOn ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            return true;

        } else if (id == R.id.menu_about) {
            showAboutDialog();
            return true;

        } else if (id == R.id.menu_child_mode) {
            Toast.makeText(this, "Child Mode Activated", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About App");
        builder.setMessage("Quran Memorization App for children with repetition and Azkar.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}