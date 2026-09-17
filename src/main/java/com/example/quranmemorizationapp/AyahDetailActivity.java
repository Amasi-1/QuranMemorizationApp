package com.example.quranmemorizationapp;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class AyahDetailActivity extends AppCompatActivity {

    TextView tvInfo, tvAyah;

    ImageView imgPage;

    Button btnPlay, btnNext, btnPrevious;

    View darkOverlay;

    MediaPlayer mediaPlayer;

    int surahNumber = 1;

    int ayahNumber = 1;

    int pageNumber = 1;

    String audioUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ayah_detail);

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

        tvInfo =
                findViewById(R.id.tvInfo);

        tvAyah =
                findViewById(R.id.tvAyah);

        imgPage =
                findViewById(R.id.imgPage);

        btnPlay =
                findViewById(R.id.btnPlay);

        btnNext =
                findViewById(R.id.btnNext);

        btnPrevious =
                findViewById(R.id.btnPrevious);

        loadAyahFromApi(
                surahNumber,
                ayahNumber
        );

        btnPlay.setOnClickListener(v -> {

            playAudio();

        });

        btnNext.setOnClickListener(v -> {

            ayahNumber++;

            loadAyahFromApi(
                    surahNumber,
                    ayahNumber
            );
        });

        btnPrevious.setOnClickListener(v -> {

            if (ayahNumber > 1) {

                ayahNumber--;

                loadAyahFromApi(
                        surahNumber,
                        ayahNumber
                );

            } else {

                Toast.makeText(
                        this,
                        "هذه أول آية",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void loadAyahFromApi(
            int surah,
            int ayah
    ) {

        String ref =
                surah + ":" + ayah;

        String url =
                "https://api.alquran.cloud/v1/ayah/"
                        + ref
                        + "/editions/quran-uthmani,ar.alafasy";

        RequestQueue queue =
                Volley.newRequestQueue(this);

        JsonObjectRequest request =
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,

                        response -> {

                            try {

                                JSONArray data =
                                        response.getJSONArray(
                                                "data"
                                        );

                                JSONObject textObj =
                                        data.getJSONObject(0);

                                JSONObject audioObj =
                                        data.getJSONObject(1);

                                String ayahText =
                                        textObj.getString(
                                                "text"
                                        );

                                audioUrl =
                                        audioObj.getString(
                                                "audio"
                                        );

                                tvInfo.setText(
                                        "السورة: "
                                                + surahNumber
                                                + " | الآية: "
                                                + ayahNumber
                                );

                                tvAyah.setText(
                                        ayahText
                                );

                                int imageResId =
                                        getResources()
                                                .getIdentifier(
                                                        "page" + pageNumber,
                                                        "drawable",
                                                        getPackageName()
                                                );

                                if (imageResId != 0) {

                                    imgPage.setImageResource(
                                            imageResId
                                    );
                                }

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        "Parse Error",
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        },

                        error -> Toast.makeText(
                                this,
                                "API Error",
                                Toast.LENGTH_LONG
                        ).show()
                );

        queue.add(request);
    }

    private void playAudio() {

        try {

            if (audioUrl == null
                    || audioUrl.isEmpty()) {

                Toast.makeText(
                        this,
                        "الصوت غير جاهز",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (mediaPlayer != null) {

                mediaPlayer.release();
            }

            mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(
                    audioUrl
            );

            mediaPlayer.prepare();

            mediaPlayer.start();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Audio Error",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (mediaPlayer != null) {

            mediaPlayer.release();

            mediaPlayer = null;
        }
    }
}