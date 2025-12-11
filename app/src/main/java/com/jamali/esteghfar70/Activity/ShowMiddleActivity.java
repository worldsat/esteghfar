package com.jamali.esteghfar70.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media.app.NotificationCompat.MediaStyle;
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

    // --- Constants ---
    private static final String PREF_NAME = "RecyclerViewPrefs";
    private static final String KEY_SAVED_POSITION = "saved_position";
    private static final String PREF_TOKEN = "Token";
    private static final String CHANNEL_ID = "media_playback_channel";
    private static final String ACTION_PLAY = "action_play";
    private static final String ACTION_PAUSE = "action_pause";
    private static final int NOTIFICATION_ID = 1;
    private static final int PERMISSION_REQ_CODE = 101;

    // --- UI Components ---
    private RecyclerView recyclerView;
    private ImageView playBtn, settingBtn, mainBackground;
    private TextView timeTxt, mainTitleTxt;
    private SeekBar seekBar;
    private Switch switch1;
    private View viewtop, viewBottom;
    private LinearLayout controlLayout; // نام‌گذاری بهتر برای linearLayout

    // --- Logic & Data ---
    private RecyclerView.Adapter adapter;
    private ArrayList<ShowList> response = new ArrayList<>();
    private SharedPreferences sp;
    private ScrollToPosition scrollToPosition;
    private LinearLayoutManager linearLayoutManager;
    private boolean darkMode;
    private boolean translate = true;
    private int savedPosition = -1;
    private boolean doubleBackToExitPressedOnce = false;

    // --- Media & Notification ---
    private MediaPlayer medPlayer;
    private NotificationManager notificationManager;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    // Runnable برای آپدیت SeekBar (تعریف به صورت فیلد کلاس برای جلوگیری از ایجاد مداوم)
    private final Runnable updateSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (medPlayer != null && medPlayer.isPlaying()) {
                int currentPos = medPlayer.getCurrentPosition() / 1000;
                seekBar.setProgress(currentPos);

                // اسکرول خودکار
                if (sp.getBoolean("scrollToPosition", true)) {
                    int targetPos = translate ?
                            scrollToPosition.getPosFromSoundTranslate(currentPos) :
                            scrollToPosition.getPosFromSoundWithoutTranslate(currentPos);
                    scrollToPosition.GoTo(targetPos);
                }

                // آپدیت متن زمان
                timeTxt.setText(formatTime(medPlayer.getCurrentPosition()) + "/" + formatTime(medPlayer.getDuration()));

                mHandler.postDelayed(this, 1000);
            }
        }
    };

    // BroadcastReceiver برای کنترل مدیا از طریق نوتیفیکیشن
    private final BroadcastReceiver mediaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null || medPlayer == null) return;

            switch (action) {
                case ACTION_PLAY:
                    if (!medPlayer.isPlaying()) {
                        setPlaybackSpeed();
                        medPlayer.start();
                        updatePlayPauseUI(true);
                    }
                    break;
                case ACTION_PAUSE:
                    if (medPlayer.isPlaying()) {
                        medPlayer.pause();
                        updatePlayPauseUI(false);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);

        sp = getApplicationContext().getSharedPreferences(PREF_TOKEN, 0);

        initViews();
        setupEdgeToEdgeUI();
        checkPermissions();
        setupBackPressHandler();
        setupNotificationSystem();

        setupDarkMode();
        restoreSavedPosition();
        setupTranslatorSwitch();
        setupSettingsButton();
        checkBazaarRate();
    }

    // --- Initialization Methods ---

    private void initViews() {
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
        controlLayout = findViewById(R.id.linearLayout);
    }

    private void setupEdgeToEdgeUI() {
        // تنظیم شفافیت نوارها
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // اعمال Padding داینامیک
        ViewCompat.setOnApplyWindowInsetsListener(controlLayout, (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) v.getLayoutParams();
            params.bottomMargin = systemBars.bottom;
            v.setLayoutParams(params);
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(viewtop, (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.getLayoutParams().height = systemBars.top;
            v.setLayoutParams(v.getLayoutParams());
            return insets;
        });
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQ_CODE);
            }
        }
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                    System.exit(0);
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(ShowMiddleActivity.this, "جهت خروج از برنامه، دوباره دکمه برگشت را فشار دهید", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        });
    }

    // --- Media & Notification Setup ---

    private void setupNotificationSystem() {
        createNotificationChannel();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mediaReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(mediaReceiver, filter);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "پخش صوت",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("کنترل پخش صوت");
            channel.setSound(null, null);
            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        } else {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    private void showNotification(boolean isPlaying) {
        Intent intent = new Intent(this, ShowMiddleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent actionIntent = new Intent(isPlaying ? ACTION_PAUSE : ACTION_PLAY);
        actionIntent.setPackage(getPackageName());
        PendingIntent pendingActionIntent = PendingIntent.getBroadcast(this, 1, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        int icon = isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
        String title = isPlaying ? "توقف" : "پخش";
        String statusText = isPlaying ? "در حال پخش..." : "متوقف شده";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("استغفار 70 بندی امیر المومنین(ع)")
                .setContentText(statusText)
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .addAction(icon, title, pendingActionIntent)
                .setStyle(new MediaStyle().setShowActionsInCompactView(0))
                .setOngoing(isPlaying);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void setupMediaPlayer() {
        int initialAudioResource = translate ? R.raw.aac_full4 : R.raw.without_translate;

        if (medPlayer != null) {
            medPlayer.release();
        }

        medPlayer = MediaPlayer.create(this, initialAudioResource);
        seekBar.setMax(medPlayer.getDuration() / 1000);
        timeTxt.setText("00:00/" + formatTime(medPlayer.getDuration()));

        medPlayer.setOnCompletionListener(mp -> updatePlayPauseUI(false));

        scrollToPosition = new ScrollToPosition(recyclerView, response, () -> adapter.notifyDataSetChanged());

        playBtn.setOnClickListener(view -> {
            if (medPlayer.isPlaying()) {
                medPlayer.pause();
                updatePlayPauseUI(false);
            } else {
                setPlaybackSpeed();
                medPlayer.start();
                updatePlayPauseUI(true);
            }
        });

        setupSeekBarListener();
    }

    private void setupSeekBarListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (medPlayer != null) {
                    medPlayer.seekTo(seekBar.getProgress() * 1000);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && medPlayer != null) {
                    medPlayer.seekTo(progress * 1000);
                    timeTxt.setText(formatTime(progress * 1000L) + "/" + formatTime(medPlayer.getDuration()));
                }
            }
        });
    }

    private void updatePlayPauseUI(boolean isPlaying) {
        playBtn.setImageResource(isPlaying ? R.mipmap.pause : R.mipmap.play_icon);
        showNotification(isPlaying);
        if (isPlaying) {
            mHandler.post(updateSeekBarRunnable);
        } else {
            mHandler.removeCallbacks(updateSeekBarRunnable);
        }
    }

    private void setPlaybackSpeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            float speed = sp.getFloat("speed", 1.0f);
            try {
                medPlayer.setPlaybackParams(medPlayer.getPlaybackParams().setSpeed(speed));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // --- Feature Methods ---

    private void checkBazaarRate() {
        if (!sp.getBoolean("isRated", false)) {
            try {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse("bazaar://details?id=com.jamali.esteghfar70"));
                intent.setPackage("com.farsitel.bazaar");
                startActivity(intent);
                sp.edit().putBoolean("isRated", true).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupTranslatorSwitch() {
        switch1.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            translate = isChecked;
            cleanupMediaPlayer();
            playBtn.setImageResource(R.mipmap.play_icon);
            seekBar.setProgress(0);

            if (notificationManager != null) notificationManager.cancel(NOTIFICATION_ID);

            setupMediaPlayer();
            setupDarkMode();
            getData(!isChecked, isChecked);
            setDefaultValue();
            sp.edit().putBoolean("translate", translate).apply();
        });

        // تنظیم وضعیت اولیه سوییچ
        boolean savedTranslate = sp.getBoolean("translate", true);
        if (savedTranslate != switch1.isChecked()) {
            switch1.setChecked(savedTranslate);
        } else {
            // اگر تغییر وضعیت نداریم اما باید دیتا لود شود
            translate = savedTranslate;
            setupMediaPlayer();
            getData(true, false);
        }
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
                    if ("text".equals(item.getKind())) {
                        item.setSize(sp.getFloat("textSizeArabic", 24));
                    } else if ("row".equals(item.getKind())) {
                        item.setSize(sp.getFloat("textSizePersian", 16));
                    }
                }

                if (switching && adapter != null) {
                    adapter.notifyDataSetChanged();
                    return;
                }

                adapter = new ShowItemListAdapter(response, darkMode, pos -> {
                    if (sp.getFloat("speed", 1.0f) == 1.0f) {
                        if (!medPlayer.isPlaying()) {
                            setPlaybackSpeed();
                            medPlayer.start();
                            updatePlayPauseUI(true);
                        }
                        int targetTime = translate ?
                                scrollToPosition.getPosFromSoundIndexTranslate(pos) :
                                scrollToPosition.getPosFromSoundIndexWithoutTranslate(pos);
                        medPlayer.seekTo(targetTime * 1000);
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

    // --- Utility Methods ---

    private void setupDarkMode() {
        darkMode = sp.getBoolean("darkMode", false);
        int textColor = darkMode ? getResources().getColor(R.color.white) : getResources().getColor(R.color.black);
        int tintColor = darkMode ? R.color.white : R.color.brown_800;

        mainBackground.setImageResource(darkMode ? R.color.black2 : R.mipmap.back1);
        playBtn.setColorFilter(ContextCompat.getColor(this, tintColor), android.graphics.PorterDuff.Mode.MULTIPLY);
        settingBtn.setColorFilter(ContextCompat.getColor(this, tintColor), android.graphics.PorterDuff.Mode.MULTIPLY);
        timeTxt.setTextColor(textColor);
        mainTitleTxt.setTextColor(textColor);
        switch1.setTextColor(textColor);

        int viewBgColor = darkMode ? getResources().getColor(R.color.black3) : Color.parseColor("#00000000");
        viewtop.setBackgroundColor(viewBgColor);
        viewBottom.setBackgroundColor(viewBgColor);
    }

    private void setDefaultValue() {
        if (adapter != null) {
            for (ShowList item : response) {
                if ("text".equals(item.getKind())) {
                    item.setSize(sp.getFloat("textSizeArabic", 24));
                } else if ("row".equals(item.getKind())) {
                    item.setSize(sp.getFloat("textSizePersian", 16));
                }
                item.setDarkMode(darkMode);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void setupSettingsButton() {
        settingBtn.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void savePosition(int position) {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putInt(KEY_SAVED_POSITION, position).apply();
    }

    private void clearSavedPosition() {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().remove(KEY_SAVED_POSITION).apply();
    }

    private void cleanupMediaPlayer() {
        if (medPlayer != null) {
            if (medPlayer.isPlaying()) medPlayer.stop();
            medPlayer.release();
            medPlayer = null;
        }
    }

    private String formatTime(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }

    // --- Lifecycle Overrides ---

    @Override
    protected void onPause() {
        super.onPause();
        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            savePosition(layoutManager.findLastVisibleItemPosition());
        }
        // توقف آپدیت UI برای صرفه جویی در منابع (پخش صدا ادامه دارد)
        mHandler.removeCallbacks(updateSeekBarRunnable);
        setupDarkMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (medPlayer == null) {
            setupMediaPlayer();
        } else if (medPlayer.isPlaying()) {
            updatePlayPauseUI(true);
        }
        setupDarkMode();
        setDefaultValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanupMediaPlayer();
        if (notificationManager != null) notificationManager.cancel(NOTIFICATION_ID);
        try {
            unregisterReceiver(mediaReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.removeCallbacks(updateSeekBarRunnable);
    }
}