package com.jamali.esteghfar70.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.jamali.esteghfar70.Kernel.Controller.Module.Glide.Glide_module;
import com.jamali.esteghfar70.Kernel.Helper.FileManager;
import com.jamali.esteghfar70.R;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timer();
        animLogo();
        moveDB();
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

                        Intent intent = new Intent(SplashActivity.this, ShowMiddleActivity.class);
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
    private void moveDB() {
        String src = getCacheDir() + "/arbaeen.db";
        String des = getFilesDir().getParent() + "/databases/arbaeen.db";
        File n = new File(des);
//        if (!n.exists()) {
        File f = new File(getCacheDir() + "/arbaeen.db");
        if (!f.exists()) try {

            InputStream is = getAssets().open("arbaeen.db");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
//                throw new RuntimeException(e);
        }
        try {
            FileManager.copyFile(src, des);
        } catch (IOException e) {

        }
//        }
    }

}
