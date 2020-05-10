package com.jamali.esteghfar70.Helper;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.jamali.esteghfar70.Domain.ShowList;

import java.util.ArrayList;
import java.util.Objects;

public class ScrollToPosition {
    private RecyclerView recyclerView;
    private ArrayList<ShowList> response;
    private RecyclerView.Adapter adapter;
    private int currentPos = -1;
    int i = 0;

    public ScrollToPosition(RecyclerView recyclerView, ArrayList<ShowList> response, RecyclerView.Adapter adapter) {
        this.recyclerView = recyclerView;
        this.response = response;
        this.adapter = adapter;
    }

    public void GoTo(int position) {
        int pos = i;
        if (currentPos != pos) {
            recyclerView.scrollToPosition(pos);
            for (int j = 0; j < response.size(); j++) {
                if (j != pos) {
                    response.get(j).setSeleccted("0");
                } else {
                    response.get(j).setSeleccted("1");
                }
            }
            adapter.notifyDataSetChanged();
            currentPos = pos;
            try {
//                Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(pos)).itemView.performClick();

            } catch (Exception ignored) {
                Log.i("moh3n", "GoTo: ");
            }

        }
    }

    public int getPosFromSound(int pos) {

        if (pos >= 0 && pos < 8) {
            i = 0;
        } else if (pos >= 8 && pos < 158) {
            i = 1;
        } else if (pos >= 158 && pos < 235) {
            i = 2;
        } else if (pos >= 235 && pos < 282) {
            i = 3;
        } else if (pos >= 282 && pos < 344) {
            i = 4;
        } else if (pos >= 344 && pos < 385) {
            i = 5;
        } else if (pos >= 385 && pos < 437) {
            i = 6;
        } else if (pos >= 437 && pos < 527) {
            i = 7;
        } else if (pos >= 524 && pos < 748) {
            i = 8;
        } else if (pos >= 748 && pos < 808) {
            i = 9;
        } else if (pos >= 808 && pos < 854) {
            i = 10;
        } else if (pos >= 854 && pos < 891) {
            i = 11;
        } else if (pos >= 891 && pos < 926) {
            i = 12;
        } else if (pos >= 926 && pos < 963) {
            i = 13;
        } else if (pos >= 963 && pos < 997) {
            i = 14;
        } else if (pos >= 997 && pos < 1030) {
            i = 15;
        } else if (pos >= 1030 && pos < 1070) {
            i = 16;
        } else if (pos >= 1070 && pos < 1108) {
            i = 17;
        } else if (pos >= 1108 && pos < 1166) {
            i = 18;
        } else if (pos >= 1166 && pos < 1206) {
            i = 19;
        } else if (pos >= 1206 && pos < 1258) {
            i = 20;
        } else if (pos >= 1258 && pos < 1304) {
            i = 21;
        } else if (pos >= 1304 && pos < 1339) {
            i = 22;
        } else if (pos >= 1339 && pos < 1382) {
            i = 23;
        } else if (pos >= 1382 && pos < 1412) {
            i = 24;
        } else if (pos >= 1412 && pos < 1438) {
            i = 25;
        } else if (pos >= 1438 && pos < 1469) {
            i = 26;
        } else if (pos >= 1469 && pos < 1502) {
            i = 27;
        } else if (pos >= 1502 && pos < 1563) {
            i = 28;
        } else if (pos >= 1563 && pos < 1593) {
            i = 29;
        } else if (pos >= 1593 && pos < 1645) {
            i = 30;
        } else if (pos >= 1645 && pos < 1718) {
            i = 31;
        } else if (pos >= 1718 && pos < 1767) {
            i = 32;
        } else if (pos >= 1767 && pos < 1844) {
            i = 33;
        } else if (pos >= 1844 && pos < 1872) {
            i = 34;
        } else if (pos >= 1872 && pos < 1921) {
            i = 35;
        } else if (pos >= 1912 && pos < 1961) {
            i = 36;
        } else if (pos >= 1961 && pos < 1996) {
            i = 37;
        } else if (pos >= 1996 && pos < 2025) {
            i = 38;
        } else if (pos >= 2025 && pos < 2080) {
            i = 39;
        } else if (pos >= 2080 && pos < 2117) {
            i = 40;
        } else if (pos >= 2117 && pos < 2154) {
            i = 41;
        } else if (pos >= 2154 && pos < 2249) {
            i = 42;
        } else if (pos >= 2249 && pos < 2286) {
            i = 43;
        } else if (pos >= 2286 && pos < 2321) {
            i = 44;
        } else if (pos >= 2321 && pos < 2347) {
            i = 45;
        } else if (pos >= 2347 && pos < 2382) {
            i = 46;
        } else if (pos >= 2382 && pos < 2407) {
            i = 47;
        } else if (pos >= 2407 && pos < 2442) {
            i = 48;
        } else if (pos >= 2442 && pos < 2484) {
            i = 49;
        } else if (pos >= 2484 && pos < 2517) {
            i = 50;
        } else if (pos >= 2517 && pos < 2561) {
            i = 51;
        } else if (pos >= 2561 && pos < 2597) {
            i = 52;
        } else if (pos >= 2597 && pos < 2620) {
            i = 53;
        } else if (pos >= 2620 && pos < 2650) {
            i = 54;
        } else if (pos >= 2650 && pos < 2701) {
            i = 55;
        } else if (pos >= 2701 && pos < 2751) {
            i = 56;
        } else if (pos >= 2751 && pos < 2780) {
            i = 57;
        } else if (pos >= 2780 && pos < 2829) {
            i = 58;
        } else if (pos >= 2829 && pos < 2866) {
            i = 59;
        } else if (pos >= 2866 && pos < 2908) {
            i = 60;
        } else if (pos >= 2908 && pos < 2949) {
            i = 61;
        } else if (pos >= 2949 && pos < 2981) {
            i = 62;
        } else if (pos >= 2981 && pos < 3041) {
            i = 63;
        } else if (pos >= 3041 && pos < 3082) {
            i = 64;
        } else if (pos >= 3082 && pos < 3131) {
            i = 65;
        } else if (pos >= 3131 && pos < 3171) {
            i = 66;
        } else if (pos >= 3171 && pos < 3215) {
            i = 67;
        } else if (pos >= 3215 && pos < 3255) {
            i = 68;
        } else if (pos >= 3255 && pos < 3292) {
            i = 69;
        } else if (pos >= 3292) {
            i = 70;
        }
        return i;
    }
}
