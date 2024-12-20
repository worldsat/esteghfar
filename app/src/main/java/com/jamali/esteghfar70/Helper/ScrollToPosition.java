package com.jamali.esteghfar70.Helper;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.jamali.esteghfar70.Domain.ShowList;

import java.util.ArrayList;

public class ScrollToPosition {
    private RecyclerView recyclerView;
    private ArrayList<ShowList> response;
    private RecyclerView.Adapter adapter;
    private int currentPos = -1;
    int i = 0;
    private CallbackChanged callbackChanged;

    public ScrollToPosition(RecyclerView recyclerView, ArrayList<ShowList> response, CallbackChanged callbackChanged) {
        this.recyclerView = recyclerView;
        this.response = response;
        this.adapter = adapter;
        this.callbackChanged = callbackChanged;
    }

    public void GoTo(int position) {
        if (currentPos != position) {

            for (int j = 0; j < response.size(); j++) {
                if (j != position) {
                    response.get(j).setSeleccted("0");
                } else {
                    response.get(j).setSeleccted("1");
                }
            }
            recyclerView.scrollToPosition(position);

            callbackChanged.done();
            currentPos = position;
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
        } else if (pos >= 235 && pos < 281) {
            i = 3;
        } else if (pos >= 281 && pos < 344) {
            i = 4;
        } else if (pos >= 344 && pos < 383) {
            i = 5;
        } else if (pos >= 383 && pos < 434) {
            i = 6;
        } else if (pos >= 434 && pos < 522) {
            i = 7;
        } else if (pos >= 522 && pos < 744) {
            i = 8;
        } else if (pos >= 744 && pos < 804) {
            i = 9;
        } else if (pos >= 804 && pos < 851) {
            i = 10;
        } else if (pos >= 851 && pos < 887) {
            i = 11;
        } else if (pos >= 887 && pos < 922) {
            i = 12;
        } else if (pos >= 922 && pos < 958) {
            i = 13;
        } else if (pos >= 958 && pos < 992) {
            i = 14;
        } else if (pos >= 992 && pos < 1025) {
            i = 15;
        } else if (pos >= 1025 && pos < 1065) {
            i = 16;
        } else if (pos >= 1065 && pos < 1103) {
            i = 17;
        } else if (pos >= 1103 && pos < 1162) {
            i = 18;
        } else if (pos >= 1162 && pos < 1201) {
            i = 19;
        } else if (pos >= 1201 && pos < 1254) {
            i = 20;
        } else if (pos >= 1254 && pos < 1300) {
            i = 21;
        } else if (pos >= 1300 && pos < 1336) {
            i = 22;
        } else if (pos >= 1336 && pos < 1379) {
            i = 23;
        } else if (pos >= 1379 && pos < 1409) {
            i = 24;
        } else if (pos >= 1409 && pos < 1435) {
            i = 25;
        } else if (pos >= 1435 && pos < 1466) {
            i = 26;
        } else if (pos >= 1466 && pos < 1499) {
            i = 27;
        } else if (pos >= 1499 && pos < 1560) {
            i = 28;
        } else if (pos >= 1560 && pos < 1590) {
            i = 29;
        } else if (pos >= 1590 && pos < 1642) {
            i = 30;
        } else if (pos >= 1642 && pos < 1715) {
            i = 31;
        } else if (pos >= 1715 && pos < 1767) {
            i = 32;
        } else if (pos >= 1767 && pos < 1838) {
            i = 33;
        } else if (pos >= 1838 && pos < 1869) {
            i = 34;
        } else if (pos >= 1869 && pos < 1916) {
            i = 35;
        } else if (pos >= 1916 && pos < 1957) {
            i = 36;
        } else if (pos >= 1957 && pos < 1990) {
            i = 37;
        } else if (pos >= 1990 && pos < 2020) {
            i = 38;
        } else if (pos >= 2020 && pos < 2080) {
            i = 39;
        } else if (pos >= 2080 && pos < 2112) {
            i = 40;
        } else if (pos >= 2112 && pos < 2149) {
            i = 41;
        } else if (pos >= 2149 && pos < 2244) {
            i = 42;
        } else if (pos >= 2244 && pos < 2282) {
            i = 43;
        } else if (pos >= 2282 && pos < 2317) {
            i = 44;
        } else if (pos >= 2317 && pos < 2343) {
            i = 45;
        } else if (pos >= 2343 && pos < 2375) {
            i = 46;
        } else if (pos >= 2375 && pos < 2405) {
            i = 47;
        } else if (pos >= 2405 && pos < 2442) {
            i = 48;
        } else if (pos >= 2442 && pos < 2483) {
            i = 49;
        } else if (pos >= 2483 && pos < 2516) {
            i = 50;
        } else if (pos >= 2516 && pos < 2558) {
            i = 51;
        } else if (pos >= 2558 && pos < 2593) {
            i = 52;
        } else if (pos >= 2593 && pos < 2623) {
            i = 53;
        } else if (pos >= 2623 && pos < 2654) {
            i = 54;
        } else if (pos >= 2654 && pos < 2704) {
            i = 55;
        } else if (pos >= 2704 && pos < 2755) {
            i = 56;
        } else if (pos >= 2755 && pos < 2785) {
            i = 57;
        } else if (pos >= 2785 && pos < 2834) {
            i = 58;
        } else if (pos >= 2834 && pos < 2871) {
            i = 59;
        } else if (pos >= 2871 && pos < 2913) {
            i = 60;
        } else if (pos >= 2913 && pos < 2946) {
            i = 61;
        } else if (pos >= 2946 && pos < 2980) {
            i = 62;
        } else if (pos >= 2980 && pos < 3040) {
            i = 63;
        } else if (pos >= 3040 && pos < 3080) {
            i = 64;
        } else if (pos >= 3080 && pos < 3128) {
            i = 65;
        } else if (pos >= 3128 && pos < 3157) {
            i = 66;
        } else if (pos >= 3157 && pos < 3212) {
            i = 67;
        } else if (pos >= 3212 && pos < 3252) {
            i = 68;
        } else if (pos >= 3252 && pos < 3289) {
            i = 69;
        } else if (pos >= 3289) {
            i = 70;
        }
        return i;
    }

    public int getPosFromSoundIndex(int i) {

        int[] rangeStarts = {0, 8, 158, 235, 281, 344, 383, 434, 522, 744, 804, 851, 887, 922, 958, 992, 1025, 1065, 1103, 1162, 1201, 1254, 1300, 1336, 1379, 1409, 1435, 1466, 1499, 1560, 1590, 1642, 1715, 1767, 1838, 1869, 1916, 1957, 1990, 2020, 2080, 2112, 2149, 2244, 2282, 2317, 2343, 2375, 2405, 2442, 2483, 2516, 2558, 2593, 2623, 2654, 2704, 2755, 2785, 2834, 2871, 2913, 2946, 2980, 3040, 3080, 3128, 3157, 3212, 3252, 3289};

        return (rangeStarts[i]);
    }

    public interface CallbackChanged {

        void done();

    }
}
