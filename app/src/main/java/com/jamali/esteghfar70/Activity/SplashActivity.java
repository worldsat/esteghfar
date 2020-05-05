package com.jamali.esteghfar70.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

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

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Token", 0);
        sp.edit().putFloat("speed",1.0f).apply();
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

                    }, 3000);
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
//            if (!f.exists())
            try {

                InputStream is = getAssets().open("arbaeen.db");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();



                FileOutputStream fos = new FileOutputStream(f);
                fos.write(buffer);
                fos.close();
//                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
//                Toast.makeText(this, "1"+e.toString(), Toast.LENGTH_SHORT).show();
//                throw new RuntimeException(e);
            }
            try {
                FileManager.copyFile(src, des);
            } catch (IOException e) {
//                Toast.makeText(this, "2"+e.toString(), Toast.LENGTH_SHORT).show();
            }
//        }
    }

}
