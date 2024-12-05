package com.jamali.esteghfar70.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowMiddleActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<ShowList> response = new ArrayList<>();
    private MediaPlayer medPlayer;
    private Button playBtn;
    private TextView timeTxt;
    private SeekBar seekBar;
    private Switch switch1;
    private ImageView settingBtn;
    private Handler mHandler = new Handler();
    private Runnable updateSeekBar;
    private int savedPosition = -1;
    private boolean translate = true;

    private static final String PREF_NAME = "RecyclerViewPrefs";
    private static final String KEY_SAVED_POSITION = "saved_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        initView();
        setupMediaPlayer();
        restoreSavedPosition();
        translator();
        getData(false, false);
        setupSettingsButton();
    }

    private void initView() {
        recyclerView = findViewById(R.id.view);
        playBtn = findViewById(R.id.playBtn);
        timeTxt = findViewById(R.id.timeTxt);
        seekBar = findViewById(R.id.seekBar);
        switch1 = findViewById(R.id.switch1);
        settingBtn = findViewById(R.id.settingsBtn);
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
            int audioResource = isChecked ? R.raw.full2 : R.raw.without_translate;
            if (medPlayer != null) {
                medPlayer.stop();
                medPlayer.release();
                medPlayer = null;
            }
            medPlayer = MediaPlayer.create(this, audioResource);
            setupMediaPlayer();
            getData(!isChecked, isChecked); // بروزرسانی لیست بر اساس وضعیت
        });
    }

    private void setupMediaPlayer() {
        int initialAudioResource = translate ? R.raw.full2 : R.raw.without_translate;

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

        medPlayer.setOnCompletionListener(mp -> playBtn.setBackgroundResource(R.mipmap.play_icon));

        playBtn.setOnClickListener(view -> {
            if (medPlayer.isPlaying()) {
                medPlayer.pause();
                playBtn.setBackgroundResource(R.mipmap.play_icon);
            } else {
                medPlayer.start();
                playBtn.setBackgroundResource(R.mipmap.pause);
                startSeekBarUpdate();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && medPlayer != null) {
                    medPlayer.seekTo(progress * 1000);
                }
            }
        });

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (medPlayer != null && medPlayer.isPlaying()) {
                    int currentPos = medPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(currentPos);

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
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowMiddleActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                response.clear();
                response.addAll((Collection<? extends ShowList>) result);
                for (ShowList item : response) {
                    item.setSeleccted("0");
                }
                if (switching) {
                    adapter.notifyDataSetChanged();
                    return;
                }
                adapter = new ShowItemListAdapter(response);
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
            savePosition(layoutManager.findLastVisibleItemPosition());
        }
        if (medPlayer != null) medPlayer.pause();
        stopSeekBarUpdate();
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
}
