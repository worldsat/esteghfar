package com.jamali.esteghfar70.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.jamali.esteghfar70.Adapter.AfterListAdapter;
import com.jamali.esteghfar70.Domain.AfterList;
import com.jamali.esteghfar70.Kernel.Activity.BaseActivity;
import com.jamali.esteghfar70.Kernel.Controller.Interface.CallbackGet;
import com.jamali.esteghfar70.R;

import java.util.ArrayList;
import java.util.Collection;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsActivity extends BaseActivity {

    RadioGroup rg;
    CheckBox checkBox;

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
            switch (checkedId) {
                case R.id.radioButton:
                    sp.edit().putFloat("speed", 1.0f).apply();
                    break;
                case R.id.radioButton2:
                    sp.edit().putFloat("speed", 1.1f).apply();
                    break;
                case R.id.radioButton3:
                    sp.edit().putFloat("speed", 1.2f).apply();
                    break;
                case R.id.radioButton4:
                    sp.edit().putFloat("speed", 1.3f).apply();
                    break;
                case R.id.radioButton5:
                    sp.edit().putFloat("speed", 1.4f).apply();
                    break;
                case R.id.radioButton6:
                    sp.edit().putFloat("speed", 1.5f).apply();
                    break;
            }
        });
        checkBox.setChecked(sp.getBoolean("scrollToPosition", true));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sp.edit().putBoolean("scrollToPosition", b).apply();
            }
        });


    }


    private void initview() {
        rg = findViewById(R.id.radioGroup);
        checkBox = findViewById(R.id.scrollToPosition);
    }
}
