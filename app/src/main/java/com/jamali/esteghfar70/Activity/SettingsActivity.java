package com.jamali.esteghfar70.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jamali.esteghfar70.Kernel.Activity.BaseActivity;
import com.jamali.esteghfar70.R;

public class SettingsActivity extends BaseActivity {

    RadioGroup rg;
    CheckBox checkBox;
    private TextView warningScroll;
    private TextView arabicText, persianText;
    private TextView plusPersian, plusArabic, minusArabic, minusPersian;
    private float textSizeArabic = 24, textSizePersian = 16; // سایز پیش‌فرض متن


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        initview();
        setVariable();

    }

    private void setVariable() {

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Token", 0);
        String speed = String.valueOf(sp.getFloat("speed", 1.0f));
        Log.i(TAG, "setVariable: " + speed);
        switch (speed) {
            case "1.0":
                rg.check(R.id.radioButton);
                break;
            case "1.1":
                rg.check(R.id.radioButton2);
                break;
            case "1.2":
                rg.check(R.id.radioButton3);
                break;
            case "1.3":
                rg.check(R.id.radioButton4);
                break;
            case "1.4":
                rg.check(R.id.radioButton5);
                break;
            case "1.5":
                rg.check(R.id.radioButton6);
                break;
        }


        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButton) {
                sp.edit().putFloat("speed", 1.0f).apply();
                checkBox.setEnabled(true);
                checkBox.setChecked(true);
                warningScroll.setVisibility(View.GONE);
                sp.edit().putBoolean("scrollToPosition", true).apply();
            } else if (checkedId == R.id.radioButton2) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
                warningScroll.setVisibility(View.VISIBLE);
                sp.edit().putBoolean("scrollToPosition", false).apply();
                sp.edit().putFloat("speed", 1.1f).apply();
            } else if (checkedId == R.id.radioButton3) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
                warningScroll.setVisibility(View.VISIBLE);
                sp.edit().putBoolean("scrollToPosition", false).apply();
                sp.edit().putFloat("speed", 1.2f).apply();
            } else if (checkedId == R.id.radioButton4) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
                warningScroll.setVisibility(View.VISIBLE);
                sp.edit().putBoolean("scrollToPosition", false).apply();
                sp.edit().putFloat("speed", 1.3f).apply();
            } else if (checkedId == R.id.radioButton5) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
                warningScroll.setVisibility(View.VISIBLE);
                sp.edit().putBoolean("scrollToPosition", false).apply();
                sp.edit().putFloat("speed", 1.4f).apply();
            } else if (checkedId == R.id.radioButton6) {
                checkBox.setChecked(false);
                checkBox.setEnabled(false);
                warningScroll.setVisibility(View.VISIBLE);
                sp.edit().putFloat("speed", 1.5f).apply();
            }
        });
        checkBox.setChecked(sp.getBoolean("scrollToPosition", true));
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> sp.edit().putBoolean("scrollToPosition", b).apply());


        //set size font
        textSizeArabic = sp.getFloat("textSizeArabic", 24);
        arabicText.setTextSize(textSizeArabic);

        textSizePersian = sp.getFloat("textSizePersian", 16);
        persianText.setTextSize(textSizePersian);


        plusPersian.setOnClickListener(v -> {
            if (textSizePersian < 28) {
                textSizePersian += 2;
                persianText.setTextSize(textSizePersian);
                saveTextSizePersian();
            }
        });

        // تنظیم اکشن برای دکمه کاهش سایز
        minusPersian.setOnClickListener(v -> {
            if (textSizePersian > 14) { // جلوگیری از سایز بیش از حد کوچک
                textSizePersian -= 2;
                persianText.setTextSize(textSizePersian);
                saveTextSizePersian();
            }
        });

        plusArabic.setOnClickListener(v -> {
            if (textSizePersian < 28) {
                textSizeArabic += 2;
                arabicText.setTextSize(textSizeArabic);
                saveTextSizeArabic();
            }
        });

        // تنظیم اکشن برای دکمه کاهش سایز
        minusArabic.setOnClickListener(v -> {
            if (textSizeArabic > 18) { // جلوگیری از سایز بیش از حد کوچک
                textSizeArabic -= 2;
                arabicText.setTextSize(textSizeArabic);
                saveTextSizeArabic();
            }
        });

    }

    private void saveTextSizePersian() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Token", 0);
        sp.edit().putFloat("textSizePersian", textSizePersian).apply();
    }

    private void saveTextSizeArabic() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Token", 0);
        sp.edit().putFloat("textSizeArabic", textSizeArabic).apply();
    }

    private void initview() {
        rg = findViewById(R.id.radioGroup);
        checkBox = findViewById(R.id.scrollToPosition);
        minusArabic = findViewById(R.id.minusArabic);
        minusPersian = findViewById(R.id.minusPersian);
        plusPersian = findViewById(R.id.plusPersian);
        plusArabic = findViewById(R.id.plusArabic);
        arabicText = findViewById(R.id.arabicText);
        persianText = findViewById(R.id.persianText);
        warningScroll = findViewById(R.id.warningScroll);
    }
}
