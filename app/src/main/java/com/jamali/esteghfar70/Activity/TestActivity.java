package com.jamali.esteghfar70.Activity;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jamali.esteghfar70.R;

public class TestActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Button btnPlayPause;
    private TextView tvTime;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnPlayPause = findViewById(R.id.btnPlayPause);
        seekBar = findViewById(R.id.seekBar);
        tvTime = findViewById(R.id.tvTime);

        // بارگذاری فایل صوتی از پوشه raw
        mediaPlayer = MediaPlayer.create(this, R.raw.full2);

        // تنظیم مدت زمان نوار لغزنده
        seekBar.setMax(mediaPlayer.getDuration());

        // بروزرسانی نوار لغزنده
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);

                    // بروزرسانی زمان
                    String currentTime = formatTime(currentPosition);
                    String totalTime = formatTime(mediaPlayer.getDuration());
                    tvTime.setText(currentTime + " / " + totalTime);

                    handler.postDelayed(this, 1000);
                }
            }
        };

        // دکمه پلی/توقف
        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setText("Play");
                handler.removeCallbacks(updateSeekBar);
            } else {
                mediaPlayer.start();
                btnPlayPause.setText("Pause");
                handler.post(updateSeekBar);
            }
        });

        // تعامل با نوار لغزنده
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
