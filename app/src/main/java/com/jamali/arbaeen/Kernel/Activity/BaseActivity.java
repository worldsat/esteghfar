package com.jamali.arbaeen.Kernel.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jamali.arbaeen.Kernel.Controller.Controller;
import com.jamali.arbaeen.Kernel.Controller.Module.SnakBar.SnakBar;
import com.jamali.arbaeen.Kernel.Helper.ExceptionHandler;
import com.jamali.arbaeen.Kernel.Helper.ListBuilder;
import com.jamali.arbaeen.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "moh3n";

    {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    public static String getCurrentTimeStamp(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
    }

    public MaterialDialog alertWaiting(Context context) {

        MaterialDialog waiting_dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.alert_waiting, false)
                .autoDismiss(false)
                .backgroundColor(Color.parseColor("#01000000"))
                .build();

        ImageView loading_circle = (ImageView) waiting_dialog.findViewById(R.id.loading_circle_alert);

        Glide.with(context)
                .load(R.mipmap.loading)
                .into(loading_circle);
        return waiting_dialog;
    }

    protected <T extends BaseActivity> T getActivity() {
        return (T) this;
    }

    public void SnakBar(String str) {
        SnakBar snakBar = new SnakBar();
        snakBar.snakShow(BaseActivity.this, str);
    }

    public Controller controller() {

        return new Controller(BaseActivity.this);
    }


    public ListBuilder listBuilder() {
        return new ListBuilder(BaseActivity.this);
    }
}
