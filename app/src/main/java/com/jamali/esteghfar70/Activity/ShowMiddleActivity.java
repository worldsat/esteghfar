package com.jamali.esteghfar70.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jamali.esteghfar70.Adapter.ShowItemListAdapter;
import com.jamali.esteghfar70.Domain.ShowList;
import com.jamali.esteghfar70.Helper.ScrollToPosition;
import com.jamali.esteghfar70.Kernel.Activity.BaseActivity;
import com.jamali.esteghfar70.Kernel.Controller.Bll.SettingsBll;
import com.jamali.esteghfar70.Kernel.Controller.Domain.Filter;
import com.jamali.esteghfar70.Kernel.Controller.Interface.CallbackGet;
import com.jamali.esteghfar70.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class ShowMiddleActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<ShowList> response = new ArrayList<>();
    private MediaPlayer medPlayer;
    private ImageView playBtn;
    private TextView timeTxt;
    private TextView mainTitleTxt;
    private SeekBar seekBar;
    private Switch switch1;
    private ImageView settingBtn;
    private Handler mHandler = new Handler();
    private Runnable updateSeekBar;
    private int savedPosition = -1;
    private boolean translate = true;
    boolean doubleBackToExitPressedOnce = false;
    private static final String PREF_NAME = "RecyclerViewPrefs";
    private static final String KEY_SAVED_POSITION = "saved_position";
    private SharedPreferences sp;
    private ScrollToPosition scrollToPosition;
    private LinearLayoutManager linearLayoutManager;
    private boolean darkMode;
    private ImageView mainBackground;
    private View viewtop, viewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        sp = getApplicationContext().getSharedPreferences("Token", 0);

        initView();
        setupDarkMode();
        restoreSavedPosition();
        translator();
        setupSettingsButton();

    }

    private void setupDarkMode() {
        darkMode = sp.getBoolean("darkMode", false);
        if (darkMode) {
            mainBackground.setImageResource(R.color.black2);
            playBtn.setColorFilter(ContextCompat.getColor(ShowMiddleActivity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            settingBtn.setColorFilter(ContextCompat.getColor(ShowMiddleActivity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            timeTxt.setTextColor(getResources().getColor(R.color.white));
            mainTitleTxt.setTextColor(getResources().getColor(R.color.white));
            switch1.setTextColor(getResources().getColor(R.color.white));
            viewtop.setBackgroundColor(getResources().getColor(R.color.black3));
            viewBottom.setBackgroundColor(getResources().getColor(R.color.black3));
        } else {
            mainBackground.setImageResource(R.mipmap.back1);
            playBtn.setColorFilter(ContextCompat.getColor(ShowMiddleActivity.this, R.color.brown_800), android.graphics.PorterDuff.Mode.MULTIPLY);
            settingBtn.setColorFilter(ContextCompat.getColor(ShowMiddleActivity.this, R.color.brown_800), android.graphics.PorterDuff.Mode.MULTIPLY);
            timeTxt.setTextColor(getResources().getColor(R.color.black));
            mainTitleTxt.setTextColor(getResources().getColor(R.color.black));
            switch1.setTextColor(getResources().getColor(R.color.black));
            viewtop.setBackgroundColor(Color.parseColor("#00000000"));
            viewBottom.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.view);
        playBtn = findViewById(R.id.playBtn);
        timeTxt = findViewById(R.id.timeTxt);
        seekBar = findViewById(R.id.seekBar);
        switch1 = findViewById(R.id.switch1);
        settingBtn = findViewById(R.id.settingsBtn);
        mainBackground = findViewById(R.id.mainBackground);
        mainTitleTxt = findViewById(R.id.mainTitleTxt);
        viewtop = findViewById(R.id.viewtop);
        viewBottom = findViewById(R.id.viewBottom);
    }

    private void restoreSavedPosition() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        savedPosition = prefs.getInt(KEY_SAVED_POSITION, -1);
        if (savedPosition != -1) {
            new AlertDialog.Builder(this)
                    .setTitle("ادامه نمایش")
                    .setMessage("آیا مایلید از ادامه آخرین ردیف مشاهده شده، نمایش را ادامه دهید؟")
                    .setPositiveButton("بله", (dialog, which) -> recyclerView.scrollToPosition(savedPosition))
                    .setNegativeButton("خیر", (dialog, which) -> clearSavedPosition())
                    .show();
        }
    }

    private void translator() {

        switch1.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            translate = isChecked; // تنظیم مقدار translate

            if (medPlayer != null) {
                medPlayer.stop();
                medPlayer.release();
                medPlayer = null;
            }
            playBtn.setImageResource(R.mipmap.play_icon);
            seekBar.setProgress(0);

            setupMediaPlayer();
            setupDarkMode();
            getData(!isChecked, isChecked);
            setDefaultValue();
            sp.edit().putBoolean("translate", translate).apply();
        });

        if (sp.getBoolean("translate", true)) {
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
            translate = false;

            setupMediaPlayer();
            getData(true, false);
        }
    }

    private void setupMediaPlayer() {
        int initialAudioResource = translate ? R.raw.aac_full4 : R.raw.without_translate;

        if (medPlayer != null) {
            medPlayer.release();
            medPlayer = null;
        }

        medPlayer = MediaPlayer.create(this, initialAudioResource); // مقداردهی اولیه
        seekBar.setMax(medPlayer.getDuration() / 1000);

        // محاسبه زمان کل صوت
        String totalTime = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(medPlayer.getDuration()) % 60);

        // مقداردهی اولیه timeTxt
        timeTxt.setText("00:00/" + totalTime);

        medPlayer.setOnCompletionListener(mp -> playBtn.setImageResource(R.mipmap.play_icon));

        // بازگرداندن ScrollToPosition
        scrollToPosition = new ScrollToPosition(recyclerView, response, new ScrollToPosition.CallbackChanged() {
            @Override
            public void done() {
                adapter.notifyDataSetChanged();
            }
        });

        playBtn.setOnClickListener(view -> {
            if (medPlayer.isPlaying()) {
                medPlayer.pause();
                playBtn.setImageResource(R.mipmap.play_icon); // تغییر آیکون به Play
            } else {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("Token", 0);
                float speed = sp.getFloat("speed", 1.0f); // تنظیم سرعت
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    medPlayer.setPlaybackParams(medPlayer.getPlaybackParams().setSpeed(speed));
                }
                medPlayer.start();
                playBtn.setImageResource(R.mipmap.pause); // تغییر آیکون به Pause
                startSeekBarUpdate();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // پخش صدا در هنگام جابه‌جایی موقتاً متوقف می‌شود
//                if (medPlayer != null && medPlayer.isPlaying()) {
//                    medPlayer.pause();
//                    playBtn.setImageResource(R.mipmap.play_icon); // به‌روزرسانی آیکون
//                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // فقط موقعیت صدا تغییر می‌کند، پخش خودکار انجام نمی‌شود
                if (medPlayer != null) {
                    medPlayer.seekTo(seekBar.getProgress() * 1000);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && medPlayer != null) {
                    medPlayer.seekTo(progress * 1000);
                    Log.i(TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                    // محاسبه زمان جدید
                    String currentTime = String.format("%02d:%02d",
                            TimeUnit.SECONDS.toMinutes(progress),
                            progress % 60);

                    String totalTime = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()),
                            TimeUnit.MILLISECONDS.toSeconds(medPlayer.getDuration()) % 60);

                    // به‌روزرسانی textView
                    timeTxt.setText(currentTime + "/" + totalTime);
                }
            }
        });


        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (medPlayer != null && medPlayer.isPlaying()) {
                    int currentPos = medPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPos);

                    // به‌روزرسانی ScrollToPosition
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("Token", 0);
                    if (sp.getBoolean("scrollToPosition", true)) {
                        if (!translate) {

                            float speed = sp.getFloat("speed", 1.0f); // تنظیم سرعت
                            scrollToPosition.GoTo(scrollToPosition.getPosFromSound(((currentPos))));
                        }
                    }

                    String time = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(medPlayer.getCurrentPosition()),
                            TimeUnit.MILLISECONDS.toSeconds(medPlayer.getCurrentPosition()) % 60);

                    timeTxt.setText(time + "/" + totalTime); // به‌روزرسانی مقدار زمان
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
    }


    private void startSeekBarUpdate() {
        mHandler.post(updateSeekBar);
    }

    private void stopSeekBarUpdate() {
        mHandler.removeCallbacks(updateSeekBar);
    }

    private void setupSettingsButton() {
        settingBtn.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void savePosition(int position) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_SAVED_POSITION, position);
        editor.apply();
    }

    private void clearSavedPosition() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        prefs.edit().remove(KEY_SAVED_POSITION).apply();
    }

    private void getData(boolean filter, boolean switching) {
        SettingsBll settingsBll = new SettingsBll(this);
        settingsBll.setMode(false);

        ArrayList<Filter> filters = new ArrayList<>();
        if (filter) {
            filters.add(new Filter("Kind", "text"));
        }
        controller().Get(ShowList.class, filters, 1, 1, true, new CallbackGet() {
            @Override
            public <T> void onSuccess(ArrayList<T> result, int count) {
                linearLayoutManager = new LinearLayoutManager(ShowMiddleActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);

                response.clear();
                response.addAll((Collection<? extends ShowList>) result);

                for (ShowList item : response) {
                    item.setSeleccted("0");
                    if (item.getKind().equals("text")) {
                        item.setSize(sp.getFloat("textSizeArabic", 24));
                    } else if (item.getKind().equals("row")) {
                        item.setSize(sp.getFloat("textSizePersian", 16));
                    }
                }

                if (switching && adapter != null) {
                    adapter.notifyDataSetChanged();
                    return;
                }
                adapter = new ShowItemListAdapter(response, darkMode, pos -> {
                    Log.i(TAG, "onClick: " + pos);
                    if (sp.getFloat("speed", 1.0f) == 1.0f) {
                        if (!medPlayer.isPlaying()) {
                            medPlayer.start();
                            playBtn.setImageResource(R.mipmap.pause); // تغییر آیکون به Pause
                            startSeekBarUpdate();
                        }
                        medPlayer.seekTo(scrollToPosition.getPosFromSoundIndex(pos) * 1000);
                    } else {
                        Toast.makeText(ShowMiddleActivity.this, "برش به بند مورد نظر فقط در سرعت پخش استاندارد در دسترس است", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ShowMiddleActivity.this, "خطا در دریافت داده‌ها", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            assert layoutManager != null;
            savePosition(layoutManager.findLastVisibleItemPosition());
        }
        if (medPlayer != null) {
            medPlayer.pause();
            playBtn.setImageResource(R.mipmap.play_icon); // تغییر آیکون به Play
        }

        stopSeekBarUpdate();
        setupDarkMode();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMediaPlayer();
        setupDarkMode();
        setDefaultValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (medPlayer != null) {
            medPlayer.release();
            medPlayer = null;
        }
        stopSeekBarUpdate();
    }

    private void setDefaultValue() {
        if (adapter != null) {
            for (int i = 0; i < response.size(); i++) {
                if (response.get(i).getKind().equals("text")) {
                    response.get(i).setSize(sp.getFloat("textSizeArabic", 24));
                } else if (response.get(i).getKind().equals("row")) {
                    response.get(i).setSize(sp.getFloat("textSizePersian", 16));
                }
                response.get(i).setDarkMode(darkMode);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "جهت خروج از برنامه، دوباره دکمه برگشت را فشار دهید", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
