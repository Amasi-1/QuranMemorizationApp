package com.example.quranmemorizationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingActivity extends AppCompatActivity {

    SwitchCompat switchDarkMode;

    SeekBar seekBarTextSize;

    View darkOverlay;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        switchDarkMode =
                findViewById(R.id.switchDarkMode);

        seekBarTextSize =
                findViewById(R.id.seekBarTextSize);

        darkOverlay =
                findViewById(R.id.darkOverlay);

        sharedPreferences =
                getSharedPreferences(
                        "SettingsPrefs",
                        MODE_PRIVATE
                );

        boolean isDark =
                sharedPreferences.getBoolean(
                        "darkMode",
                        false
                );

        switchDarkMode.setChecked(isDark);

        if (isDark) {

            darkOverlay.setVisibility(
                    View.VISIBLE
            );

        } else {

            darkOverlay.setVisibility(
                    View.GONE
            );
        }

        switchDarkMode.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    SharedPreferences.Editor editor =
                            sharedPreferences.edit();

                    editor.putBoolean(
                            "darkMode",
                            isChecked
                    );

                    editor.apply();

                    if (isChecked) {

                        darkOverlay.setVisibility(
                                View.VISIBLE
                        );

                    } else {

                        darkOverlay.setVisibility(
                                View.GONE
                        );
                    }
                });

        seekBarTextSize.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser
                    ) {

                        Toast.makeText(
                                SettingActivity.this,
                                "حجم الخط: " + progress,
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar
                    ) {

                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar
                    ) {

                    }
                });
    }
}