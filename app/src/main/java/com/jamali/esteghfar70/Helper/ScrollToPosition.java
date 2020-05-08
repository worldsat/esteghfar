package com.jamali.esteghfar70.Helper;

import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class ScrollToPosition {
    private RecyclerView recyclerView;
    private int currentPos = -1;

    public ScrollToPosition(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void GoTo(int position) {
        int pos = 0;
        if (currentPos != position) {
            recyclerView.scrollToPosition(position);

            try {
                Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView.performClick();

            } catch (Exception ignored) {

            }
            currentPos = position;
        }
    }
}
