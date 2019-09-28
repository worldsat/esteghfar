package com.jamali.arbaeen.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jamali.arbaeen.Kernel.Activity.BaseActivity;
import com.jamali.arbaeen.Kernel.Controller.Bll.SettingsBll;
import com.jamali.arbaeen.Kernel.Helper.FileManager;
import com.jamali.arbaeen.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseActivity {
    private Button btn1, btn2, btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        initView();
        setVariable();
        moveDB();
    }

    private void moveDB() {
        String src = getCacheDir() + "/arbaeen.db";
        String des = getFilesDir().getParent() + "/databases/arbaeen.db";
        File n = new File(des);
        if (!n.exists()) {
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
                throw new RuntimeException(e);
            }
            try {
                FileManager.copyFile(src, des);
            } catch (IOException e) {

            }
        }
    }

    private void setVariable() {

        SettingsBll settingsBll = new SettingsBll(this);
        settingsBll.setMode(false);

        btn1.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BeforeActivity.class);

            startActivity(intent);
        });
        btn2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MiddleActivity.class);

            startActivity(intent);
        });
        btn3.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AfterActivity.class);

            startActivity(intent);
        });
    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
    }

}
