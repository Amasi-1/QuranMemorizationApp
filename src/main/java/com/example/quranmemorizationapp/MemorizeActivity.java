package com.example.quranmemorizationapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MemorizeActivity extends AppCompatActivity {

    String[] surahText;

    TextView tvSurahName, tvVerse;

    EditText editFrom,
            editTo,
            editRepeat,
            editMinutes;

    Button btnPrevious,
            btnNext,
            btnPlay,
            btnStop;

    View darkOverlay;

    int surahNumber;

    int currentAyah = 1;

    int fromAyah = 1;

    int toAyah = 1;

    int repeatCount = 1;

    int currentRepeat = 0;

    int stopMinutes = 1;

    String audioUrl = "";

    String currentAyahText = "";

    MediaPlayer mediaPlayer;

    boolean isRepeating = false;

    Handler handler = new Handler();

    Runnable stopRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memorize);

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

        tvSurahName =
                findViewById(R.id.tvSurahName);

        tvVerse =
                findViewById(R.id.tvVerse);

        editFrom =
                findViewById(R.id.editFrom);

        editTo =
                findViewById(R.id.editTo);

        editRepeat =
                findViewById(R.id.editRepeat);

        editMinutes =
                findViewById(R.id.editMinutes);

        btnPrevious =
                findViewById(R.id.btnPrevious);

        btnNext =
                findViewById(R.id.btnNext);

        btnPlay =
                findViewById(R.id.btnPlay);

        btnStop =
                findViewById(R.id.btnStop);

        String surahName =
                getIntent().getStringExtra(
                        "SURAH_NAME"
                );

        surahNumber =
                getIntent().getIntExtra(
                        "SURAH_NUMBER",
                        1
                );

        if (surahName != null) {

            tvSurahName.setText(
                    "سورة " + surahName
            );
        }

        loadSurahText();

        btnPlay.setOnClickListener(v -> {
            startRepeating();
        });

        btnStop.setOnClickListener(v -> {

            stopRepeating();

            Toast.makeText(
                    this,
                    "تم إيقاف التكرار",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnNext.setOnClickListener(v -> {

            if (currentAyah < toAyah) {

                currentAyah++;

                currentRepeat = 0;

                stopAudio();

                loadAyahFromApi(currentAyah);

            } else {

                Toast.makeText(
                        this,
                        "وصلت لآخر آية",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnPrevious.setOnClickListener(v -> {

            if (currentAyah > fromAyah) {

                currentAyah--;

                currentRepeat = 0;

                stopAudio();

                loadAyahFromApi(currentAyah);

            } else {

                Toast.makeText(
                        this,
                        "هذه أول آية",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void loadSurahText() {

        if (surahNumber == 1) {

            surahText = new String[]{

                    "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                    "الرَّحْمَٰنِ الرَّحِيمِ",
                    "مَالِكِ يَوْمِ الدِّينِ",
                    "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                    "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                    "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ "
                            + "غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ "
                            + "وَلَا الضَّالِّينَ"
            };

        } else if (surahNumber == 112) {

            surahText = new String[]{

                    "قُلْ هُوَ اللَّهُ أَحَدٌ",
                    "اللَّهُ الصَّمَدُ",
                    "لَمْ يَلِدْ وَلَمْ يُولَدْ",
                    "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ"
            };

        } else if (surahNumber == 113) {

            surahText = new String[]{

                    "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
                    "مِن شَرِّ مَا خَلَقَ",
                    "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
                    "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
                    "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ"
            };

        } else if (surahNumber == 114) {

            surahText = new String[]{

                    "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
                    "مَلِكِ النَّاسِ",
                    "إِلَٰهِ النَّاسِ",
                    "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
                    "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
                    "مِنَ الْجِنَّةِ وَالنَّاسِ"
            };

        } else {

            surahText = null;
        }
    }

    private void startRepeating() {

        String fromText =
                editFrom.getText().toString();

        String toText =
                editTo.getText().toString();

        String repeatText =
                editRepeat.getText().toString();

        String minutesText =
                editMinutes.getText().toString();

        if (fromText.isEmpty()
                || toText.isEmpty()
                || repeatText.isEmpty()
                || minutesText.isEmpty()) {

            Toast.makeText(
                    this,
                    "الرجاء تعبئة جميع الحقول",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        fromAyah =
                Integer.parseInt(fromText);

        toAyah =
                Integer.parseInt(toText);

        repeatCount =
                Integer.parseInt(repeatText);

        stopMinutes =
                Integer.parseInt(minutesText);

        currentAyah = fromAyah;

        currentRepeat = 0;

        isRepeating = true;

        stopRunnable = () -> {

            stopRepeating();

            tvVerse.setText(
                    "تم إيقاف التكرار تلقائياً"
            );

            Toast.makeText(
                    MemorizeActivity.this,
                    "انتهت مدة التكرار",
                    Toast.LENGTH_LONG
            ).show();
        };

        handler.postDelayed(
                stopRunnable,
                stopMinutes * 60L * 1000L
        );

        loadAyahFromApi(currentAyah);

        Toast.makeText(
                this,
                "سيتم الإيقاف بعد "
                        + stopMinutes
                        + " دقائق",
                Toast.LENGTH_LONG
        ).show();
    }

    private void loadAyahFromApi(int ayah) {

        String ref =
                surahNumber + ":" + ayah;

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

                                currentAyahText =
                                        textObj.getString(
                                                "text"
                                        );

                                audioUrl =
                                        audioObj.getString(
                                                "audio"
                                        );

                                updateText();

                                if (isRepeating) {

                                    playAudio();
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

    private void updateText() {

        if (surahText == null) {

            tvVerse.setText(currentAyahText);

            return;
        }

        StringBuilder fullSurah =
                new StringBuilder();

        for (String ayah : surahText) {

            fullSurah.append(ayah)
                    .append("\n\n");
        }

        SpannableString spannable =
                new SpannableString(
                        fullSurah.toString()
                );

        int start = 0;

        for (int i = 0; i < surahText.length; i++) {

            int end =
                    start + surahText[i].length();

            if (i == currentAyah - 1) {

                spannable.setSpan(

                        new ForegroundColorSpan(
                                Color.parseColor("#D4AF37")
                        ),

                        start,

                        end,

                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            start = end + 2;
        }

        tvVerse.setText(spannable);
    }

    private void playAudio() {

        try {

            stopAudio();

            mediaPlayer = new MediaPlayer();

            mediaPlayer.setDataSource(audioUrl);

            mediaPlayer.setOnPreparedListener(
                    MediaPlayer::start
            );

            mediaPlayer.setOnCompletionListener(mp -> {

                currentRepeat++;

                if (currentRepeat < repeatCount) {

                    updateText();

                    playAudio();

                } else {

                    currentRepeat = 0;

                    if (currentAyah < toAyah
                            && isRepeating) {

                        currentAyah++;

                        loadAyahFromApi(currentAyah);

                    } else {

                        stopRepeating();

                        tvVerse.setText(
                                "تم الانتهاء من التكرار"
                        );
                    }
                }
            });

            mediaPlayer.prepareAsync();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Audio Error",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void stopRepeating() {

        isRepeating = false;

        stopAudio();

        tvVerse.setTextColor(
                Color.BLACK
        );

        if (stopRunnable != null) {

            handler.removeCallbacks(
                    stopRunnable
            );
        }
    }

    private void stopAudio() {

        try {

            if (mediaPlayer != null) {

                if (mediaPlayer.isPlaying()) {

                    mediaPlayer.stop();
                }

                mediaPlayer.release();

                mediaPlayer = null;
            }

        } catch (Exception e) {

            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        stopRepeating();
    }
}