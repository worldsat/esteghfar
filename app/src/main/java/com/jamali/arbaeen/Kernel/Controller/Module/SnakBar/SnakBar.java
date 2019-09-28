package com.jamali.arbaeen.Kernel.Controller.Module.SnakBar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jamali.arbaeen.R;

public class SnakBar {
    public void snakShow(Context context, String str) {


        final Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), str, Snackbar.LENGTH_SHORT);
//        snackbar.setAction("بازکردن پوشه", new View.OnClickListener() {
//            @Override
//            public void onClick(View vv) {
//                snackbar.dismiss();
//            }
//        });


        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/iransans_m.ttf");
        textView.setTypeface(font);
        snackbar.show();

    }
}
