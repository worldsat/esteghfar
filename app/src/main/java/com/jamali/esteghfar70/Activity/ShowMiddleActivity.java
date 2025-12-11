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
import android.support.v4.media.session.MediaSessionCompat; // ممکن است نیاز باشد
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

    // متغیرهای مربوط به نوتیفیکیشن
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "media_playback_channel";
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";

    // رسیور برای دریافت کلیک‌های دکمه‌های نوتیفیکیشن
    private final BroadcastReceiver mediaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_PLAY:
                        if (medPlayer != null && !medPlayer.isPlaying()) {
                            float speed = sp.getFloat("speed", 1.0f);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                try {
                                    medPlayer.setPlaybackParams(medPlayer.getPlaybackParams().setSpeed(speed));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            medPlayer.start();
                            playBtn.setImageResource(R.mipmap.pause);
                            startSeekBarUpdate();
                            showNotification(true);
                        }
                        break;
                    case ACTION_PAUSE:
                        if (medPlayer != null && medPlayer.isPlaying()) {
                            medPlayer.pause();
                            playBtn.setImageResource(R.mipmap.play_icon);
                            // stopSeekBarUpdate(); // اختیاری: اگر می‌خواهید سیک‌بار در نوار اعلان هم متوقف شود
                            showNotification(false);
                        }
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // اندروید 13 و بالاتر
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        // تنظیمات تمام صفحه و شفاف کردن نوارها
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        sp = getApplicationContext().getSharedPreferences("Token", 0);

        initView();
// ---------------------------------------------------------
        // --- جایگزین جدید برای onBackPressed (شروع کد جدید) ---
        // ---------------------------------------------------------
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                    System.exit(0);
                    return; // مهم: بعد از خروج ادامه ندهد
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(ShowMiddleActivity.this, "جهت خروج از برنامه، دوباره دکمه برگشت را فشار دهید", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        });
        // ---------------------------------------------------------
        // --- (پایان کد جدید) ---
        // ---------------------------------------------------------
        // راه‌اندازی کانال نوتیفیکیشن و ثبت رسیور
        createNotificationChannel();
        registerMediaReceiver();

        setupDarkMode();
        restoreSavedPosition();
        translator();
        setupSettingsButton();
        setPaddingBottomLayout();
        setBazaarScore();
    }

    private void registerMediaReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PAUSE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            registerReceiver(mediaReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(mediaReceiver, filter);
        }
    }

    private void setBazaarScore() {
        boolean isRated = sp.getBoolean("isRated", false);
        if (!isRated) {
            try {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse("bazaar://details?id=" + "com.jamali.esteghfar70"));
                intent.setPackage("com.farsitel.bazaar");
                startActivity(intent);
                sp.edit().putBoolean("isRated", true).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setPaddingBottomLayout() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        ViewCompat.setOnApplyWindowInsetsListener(linearLayout, (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) v.getLayoutParams();
            // اضافه کردن فاصله از پایین برای جلوگیری از تداخل با نوار ناوبری
            params.bottomMargin = systemBars.bottom;
            v.setLayoutParams(params);
            return insets;
        });

        // تنظیم فاصله برای نوار وضعیت بالا (Status Bar)
        ViewCompat.setOnApplyWindowInsetsListener(viewtop, (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.getLayoutParams().height = systemBars.top;
            v.setLayoutParams(v.getLayoutParams());
            return insets;
        });
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
        // اینتنت برای باز کردن برنامه
        Intent intent = new Intent(this, ShowMiddleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // اینتنت برای دکمه Play/Pause
        Intent actionIntent = new Intent(isPlaying ? ACTION_PAUSE : ACTION_PLAY);
        actionIntent.setPackage(getPackageName()); // جهت اطمینان از کارکرد دکمه
        PendingIntent pendingActionIntent = PendingIntent.getBroadcast(this, 1, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        int icon = isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
        String title = isPlaying ? "توقف" : "پخش";

        // تعیین متن ثابت برای نمایش در نوتیفیکیشن
        String statusText = isPlaying ? "در حال پخش..." : "متوقف شده";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("استغفار 70 بند امیر المومنین") // عنوان اصلی
                .setContentText(statusText) // <--- تغییر: متن ثابت به جای تایمر
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .addAction(icon, title, pendingActionIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0))
                .setOngoing(isPlaying); // اگر پخش است، نوتیفیکیشن حذف نشود

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
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
            translate = isChecked;
            if (medPlayer != null) {
                medPlayer.stop();
                medPlayer.release();
                medPlayer = null;
            }
            playBtn.setImageResource(R.mipmap.play_icon);
            seekBar.setProgress(0);

            // حذف نوتیفیکیشن هنگام تغییر مود
            if(notificationManager != null) notificationManager.cancel(1);

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

        medPlayer = MediaPlayer.create(this, initialAudioResource);
        seekBar.setMax(medPlayer.getDuration() / 1000);

        String totalTime = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(medPlayer.getDuration()) % 60);

        timeTxt.setText("00:00/" + totalTime);

        medPlayer.setOnCompletionListener(mp -> {
            playBtn.setImageResource(R.mipmap.play_icon);
            showNotification(false); // بروزرسانی نوتیفیکیشن هنگام اتمام آهنگ
        });

        scrollToPosition = new ScrollToPosition(recyclerView, response, () -> adapter.notifyDataSetChanged());

        playBtn.setOnClickListener(view -> {
            if (medPlayer.isPlaying()) {
                medPlayer.pause();
                playBtn.setImageResource(R.mipmap.play_icon);
                showNotification(false);
            } else {
                float speed = sp.getFloat("speed", 1.0f);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    medPlayer.setPlaybackParams(medPlayer.getPlaybackParams().setSpeed(speed));
                }
                medPlayer.start();
                playBtn.setImageResource(R.mipmap.pause);
                startSeekBarUpdate();
                showNotification(true);
            }
        });

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
                    String currentTime = String.format("%02d:%02d",
                            TimeUnit.SECONDS.toMinutes(progress),
                            progress % 60);
                    String totalTime = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()),
                            TimeUnit.MILLISECONDS.toSeconds(medPlayer.getDuration()) % 60);
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

                    if (sp.getBoolean("scrollToPosition", true)) {
                        if (translate) {
                            scrollToPosition.GoTo(scrollToPosition.getPosFromSoundTranslate(((currentPos))));
                        } else {
                            scrollToPosition.GoTo(scrollToPosition.getPosFromSoundWithoutTranslate(((currentPos))));
                        }
                    }

                    String time = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(medPlayer.getCurrentPosition()),
                            TimeUnit.MILLISECONDS.toSeconds(medPlayer.getCurrentPosition()) % 60);

                    String total = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(medPlayer.getDuration()),
                            TimeUnit.MILLISECONDS.toSeconds(medPlayer.getDuration()) % 60);

                    timeTxt.setText(time + "/" + total);
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
        prefs.edit().putInt(KEY_SAVED_POSITION, position).apply();
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
                    if (sp.getFloat("speed", 1.0f) == 1.0f) {
                        if (!medPlayer.isPlaying()) {
                            medPlayer.start();
                            playBtn.setImageResource(R.mipmap.pause);
                            startSeekBarUpdate();
                            showNotification(true);
                        }
                        if (translate) {
                            medPlayer.seekTo(scrollToPosition.getPosFromSoundIndexTranslate(pos) * 1000);
                        } else {
                            medPlayer.seekTo(scrollToPosition.getPosFromSoundIndexWithoutTranslate(pos) * 1000);
                        }
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

        // نکته مهم: اینجا مدیا پلیر متوقف نمی‌شود تا در پس‌زمینه پخش ادامه یابد
        // فقط آپدیت سیک‌بار را متوقف می‌کنیم تا مصرف باتری بهینه شود
        stopSeekBarUpdate();
        setupDarkMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // اگر موزیک از قبل ساخته نشده، بسازد
        if (medPlayer == null) {
            setupMediaPlayer();
        } else {
            // اگر موزیک در حال پخش است، سیک بار را مجددا فعال کند
            if (medPlayer.isPlaying()) {
                playBtn.setImageResource(R.mipmap.pause);
                startSeekBarUpdate();
            }
        }
        setupDarkMode();
        setDefaultValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (medPlayer != null) {
            if (medPlayer.isPlaying()) {
                medPlayer.stop();
            }
            medPlayer.release();
            medPlayer = null;
        }

        // حذف نوتیفیکیشن
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }

        // لغو ثبت رسیور
        try {
            unregisterReceiver(mediaReceiver);
        } catch (Exception e) {
            e.printStackTrace();
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


}