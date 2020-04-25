package com.jamali.esteghfar70.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jamali.esteghfar70.Adapter.ShowItemListAdapter;
import com.jamali.esteghfar70.Domain.ShowList;
import com.jamali.esteghfar70.Kernel.Activity.BaseActivity;
import com.jamali.esteghfar70.Kernel.Controller.Domain.Filter;
import com.jamali.esteghfar70.Kernel.Controller.Interface.CallbackGet;
import com.jamali.esteghfar70.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowMiddleActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<ShowList> response = new ArrayList<>();
    private int position, List;
    private MediaPlayer medPlayer;
    private Button playBtn;
    private TextView timeTxt;
    private SeekBar seekBar;
    private Switch switch1;
    private boolean isPlaying = false;
    private Handler mHandler = new Handler();
    private int mFileDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        initview();
        media();
        timing();
        translator();
        getData(false, false);
    }

    private void translator() {
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    getData(false, true);
                }else{
                    getData(true, false);
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
        medPlayer.pause();
        Toast.makeText(ShowMiddleActivity.this, "موزیک موقتا متوقف شد", Toast.LENGTH_SHORT).show();
        playBtn.setBackgroundResource(R.mipmap.play_icon);
    }

    private void media() {
        if (medPlayer == null) {
            medPlayer = MediaPlayer.create(this, R.raw.without_translate);
        }
        seekBar.setMax(mFileDuration/1000);
        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(medPlayer.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()))
        );
        String time2 = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(medPlayer.getCurrentPosition()),
                TimeUnit.MILLISECONDS.toSeconds(medPlayer.getCurrentPosition()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(medPlayer.getCurrentPosition()))
        );

        timeTxt.setText("00:00/" + time);
//        medPlayer.setLooping(true);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!medPlayer.isPlaying()) {

                    medPlayer.start();
                    isPlaying = true;
                    Toast.makeText(ShowMiddleActivity.this, "موزیک در حال پخش", Toast.LENGTH_SHORT).show();
                    playBtn.setBackgroundResource(R.mipmap.pause);


                    //Make sure you update Seekbar on UI thread
                    ShowMiddleActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (medPlayer != null) {
                                int mCurrentPosition = medPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mCurrentPosition);
                            }
//                            mHandler.postDelayed(this, 1000);

                            String time = String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()),
                                    TimeUnit.MILLISECONDS.toSeconds(medPlayer.getDuration()) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()))
                            );
                            String time2 = String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(medPlayer.getCurrentPosition()),
                                    TimeUnit.MILLISECONDS.toSeconds(medPlayer.getCurrentPosition()) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(medPlayer.getCurrentPosition()))
                            );

                            timeTxt.setText(time2 + "/" + time);

                            // Running this thread after 100 milliseconds
                            mHandler.postDelayed(this, 1000);

                        }
                    });
                } else {
                    medPlayer.pause();
                    isPlaying = false;
                    Toast.makeText(ShowMiddleActivity.this, "موزیک موقتا متوقف شد", Toast.LENGTH_SHORT).show();
                    playBtn.setBackgroundResource(R.mipmap.play_icon);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (medPlayer != null && fromUser) {
                    medPlayer.seekTo(progress * 1000);
                }
            }
        });
    }

    private void getData(boolean filter, boolean switching) {
        position = getIntent().getIntExtra("Position", 0);
        List = getIntent().getIntExtra("List", 0);
        ArrayList<Filter> filters = new ArrayList<Filter>();
        if (filter) {
            filters.add(new Filter("Kind", "text"));
        }
        controller().Get(ShowList.class, filters, 1, 1, true, new CallbackGet() {
            @Override
            public <T> void onSuccess(ArrayList<T> result, int count) {
                Log.i(TAG, "onSuccess: " + result);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowMiddleActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                response.clear();
                response.addAll((Collection<? extends ShowList>) result);
                if (switching) {
                    adapter.notifyDataSetChanged();
                    return;
                }
                adapter = new ShowItemListAdapter(response);
                recyclerView.setAdapter(adapter);
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
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
