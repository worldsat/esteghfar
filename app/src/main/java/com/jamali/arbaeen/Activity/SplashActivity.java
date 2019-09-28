package com.jamali.arbaeen.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.jamali.arbaeen.Kernel.Controller.Module.Glide.Glide_module;
import com.jamali.arbaeen.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timer();
        animLogo();
    }

    private void animLogo() {
        ImageView esf = findViewById(R.id.circle);
        Glide_module.with(getApplicationContext())
                .Loadimage(R.mipmap.loading, esf);
    }

    public void timer() {

        new Thread(() -> {

            try {

                mHandler.post(() -> {
                    final Handler handler = new Handler();

                    handler.postDelayed(() -> {

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);

                    }, 2000);
//
                });
                runOnUiThread(() -> {

                });
            } catch (Exception ignored) {

            }
        }).start();

    }

}
