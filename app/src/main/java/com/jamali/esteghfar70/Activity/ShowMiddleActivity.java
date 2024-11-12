package com.jamali.esteghfar70.Activity;

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
import android.widget.ScrollView;
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
    private int position, List;
    private MediaPlayer medPlayer;
    private Button playBtn;
    private TextView timeTxt;
    private SeekBar seekBar;
    private Switch switch1;
    private ImageView SettingBtn;
    private Handler mHandler = new Handler();
    private int mFileDuration;
    private boolean translate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        initview();


        translator();
        getData(false, false);
        setVariable();
    }

    private void setVariable() {
        SettingBtn.setOnClickListener(view -> startActivity(new Intent(ShowMiddleActivity.this, SettingsActivity.class)));
    }

    private void translator() {
        switch1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                getData(false, true);

                if (medPlayer != null) {
                    if (medPlayer.isPlaying()) {
                        medPlayer.stop();
                        playBtn.setBackgroundResource(R.mipmap.pause);
                    }
                    medPlayer = MediaPlayer.create(ShowMiddleActivity.this, R.raw.full);
                    timing();
                    setup();
                    translate = true;
                }

            } else {
                getData(true, false);

                if (medPlayer != null) {
                    if (medPlayer.isPlaying()) {
                        medPlayer.stop();
                        playBtn.setBackgroundResource(R.mipmap.play_icon);
                    }
                    medPlayer = MediaPlayer.create(ShowMiddleActivity.this, R.raw.without_translate);
                    setup();
                    timing();
                    translate = false;
                }
            }
        });
    }

    private void timing() {
        this.mFileDuration = this.medPlayer.getDuration();
        this.seekBar.setMax(this.mFileDuration / 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (medPlayer != null) {
            medPlayer.pause();
            playBtn.setBackgroundResource(R.mipmap.play_icon);
            mHandler.removeCallbacks(updateSeekBar); // توقف به‌روزرسانی SeekBar هنگام مکث
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (medPlayer != null) {
            medPlayer.release(); // تخریب MediaPlayer هنگام نابودی اکتیویتی
            medPlayer = null;
            mHandler.removeCallbacks(updateSeekBar); // توقف Handler هنگام نابودی اکتیویتی
        }
    }

    private void setup() {
        if (medPlayer != null) {
            mFileDuration = medPlayer.getDuration();
            seekBar.setMax(mFileDuration / 1000);

            String time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()),
                    TimeUnit.MILLISECONDS.toSeconds(medPlayer.getDuration()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()))
            );
            timeTxt.setText("00:00/" + time);
        }
    }

    private void media() {
        ScrollToPosition scrollToPosition = new ScrollToPosition(recyclerView, response, adapter);
        seekBar.setRotation(180);

        if (medPlayer == null) {
            medPlayer = MediaPlayer.create(this, R.raw.full);
        }
        setup();

        playBtn.setOnClickListener(view -> {
            if (medPlayer != null && !medPlayer.isPlaying()) {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("Token", 0);
                float speed = sp.getFloat("speed", 1.0f);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    medPlayer.setPlaybackParams(medPlayer.getPlaybackParams().setSpeed(speed));
                }
                medPlayer.start();
                Toast.makeText(ShowMiddleActivity.this, "صوت در حال پخش", Toast.LENGTH_SHORT).show();
                playBtn.setBackgroundResource(R.mipmap.pause);

                mHandler.post(updateSeekBar);
            } else if (medPlayer != null) {
                medPlayer.pause();
                Toast.makeText(ShowMiddleActivity.this, "صوت متوقف شد", Toast.LENGTH_SHORT).show();
                playBtn.setBackgroundResource(R.mipmap.play_icon);
                mHandler.removeCallbacks(updateSeekBar); // توقف به‌روزرسانی SeekBar
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (medPlayer != null && fromUser) {
                    medPlayer.seekTo(progress * 1000);
                }
            }
        });
    }
    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (medPlayer != null && medPlayer.isPlaying()) {
                int currentPosition = medPlayer.getCurrentPosition() / 1000;
                seekBar.setProgress(currentPosition);

                String time = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(mFileDuration),
                        TimeUnit.MILLISECONDS.toSeconds(mFileDuration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mFileDuration))
                );
                String time2 = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(medPlayer.getCurrentPosition()),
                        TimeUnit.MILLISECONDS.toSeconds(medPlayer.getCurrentPosition()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(medPlayer.getCurrentPosition()))
                );
                timeTxt.setText(time2 + "/" + time);

                mHandler.postDelayed(this, 1000);
            }
        }
    };
    private void getData(boolean filter, boolean switching) {
        SettingsBll settingsBll = new SettingsBll(this);
        settingsBll.setMode(false);
        position = getIntent().getIntExtra("Position", 0);
        List = getIntent().getIntExtra("List", 0);
        ArrayList<Filter> filters = new ArrayList<Filter>();
        if (filter) {
            filters.add(new Filter("Kind", "text"));
        }
        controller().Get(ShowList.class, filters, 1, 1, true, new CallbackGet() {
            @Override
            public <T> void onSuccess(ArrayList<T> result, int count) {
//                Toast.makeText(ShowMiddleActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onSuccess: " + result);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowMiddleActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                response.clear();
                response.addAll((Collection<? extends ShowList>) result);
                for (int i = 0; i < response.size(); i++) {
                    response.get(i).setSeleccted("0");
                }
                if (switching) {
                    adapter.notifyDataSetChanged();
                    return;
                }
                adapter = new ShowItemListAdapter(response);

                recyclerView.setAdapter(adapter);
                media();
                timing();
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void initview() {
        recyclerView = findViewById(R.id.view);
        playBtn = findViewById(R.id.playBtn);
        timeTxt = findViewById(R.id.timeTxt);
        seekBar = findViewById(R.id.seekBar);
        switch1 = findViewById(R.id.switch1);
        SettingBtn = findViewById(R.id.settingsBtn);
    }


}
