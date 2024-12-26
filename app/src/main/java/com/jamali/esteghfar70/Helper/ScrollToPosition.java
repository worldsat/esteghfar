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

    public int getPosFromSoundWithoutTranslate(int pos) {

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

    public int getPosFromSoundIndexWithoutTranslate(int i) {

        int[] rangeStarts = {0, 8, 158, 235, 281, 344, 383, 434, 522, 744, 804, 851, 887, 922, 958, 992, 1025, 1065, 1103, 1162, 1201, 1254, 1300, 1336, 1379, 1409, 1435, 1466, 1499, 1560, 1590, 1642, 1715, 1767, 1838, 1869, 1916, 1957, 1990, 2020, 2080, 2112, 2149, 2244, 2282, 2317, 2343, 2375, 2405, 2442, 2483, 2516, 2558, 2593, 2623, 2654, 2704, 2755, 2785, 2834, 2871, 2913, 2946, 2980, 3040, 3080, 3128, 3157, 3212, 3252, 3289};

        return (rangeStarts[i]);
    }

    public int getPosFromSoundIndexTranslate(int i) {

        int[] rangeStarts = {0, 8, 158, 232, 310, 353, 400, 429, 491, 525, 567, 593, 644, 671, 759, 806, 1028, 1107, 1166, 1194, 1240, 1267, 1303, 1329, 1364, 1391, 1427, 1453, 1487, 1511, 1545, 1571, 1611, 1639, 1677, 1707, 1766, 1806, 1846, 1872, 1924, 1956, 2002, 2031, 2066, 2089, 2132, 2162, 2192, 2212, 2238, 2258, 2289, 2309, 2344, 2309, 2430, 2468, 2499, 2520, 2572, 2600, 2674, 2721, 2770, 2800, 2874, 2912, 2940, 2961, 3010, 3036, 3077, 3102, 3135, 3158, 3187, 3209, 3269, 3307, 3339, 3357, 3394, 3418, 3513, 3566, 3603, 3629, 3663, 3686, 3712, 3735, 3768, 3792, 3822, 3844, 3878, 3901, 3944, 3972, 4005, 4027, 4070, 4097, 4132, 4157, 4185, 4207, 4237, 4258, 4309, 4343, 4394, 4423, 4452, 4473, 4522, 4557, 4593, 4613, 4656, 4679, 4712, 4734, 4768, 4791, 4851, 4886, 4926, 4951, 4999, 5034, 5063, 5083, 5139, 5171, 5211, 5237, 5274, 5298, 5398};

        return (rangeStarts[i]);
    }


    public int getPosFromSoundTranslate(int pos) {

        if (pos >= 0 && pos < 80) {
            i = 0;
        } else if (pos >= 80 && pos < 158) {
            i = 1;
        } else if (pos >= 158 && pos < 232) {
            i = 2;
        } else if (pos >= 232 && pos < 310) {
            i = 3;
        } else if (pos >= 310 && pos < 353) {
            i = 4;
        } else if (pos >= 353 && pos < 400) {
            i = 5;
        } else if (pos >= 400 && pos < 429) {
            i = 6;
        } else if (pos >= 429 && pos < 491) {
            i = 7;
        } else if (pos >= 491 && pos < 525) {
            i = 8;
        } else if (pos >= 525 && pos < 567) {
            i = 9;
        } else if (pos >= 567 && pos < 593) {
            i = 10;
        } else if (pos >= 593 && pos < 644) {
            i = 11;
        } else if (pos >= 644 && pos < 671) {
            i = 12;
        } else if (pos >= 671 && pos < 759) {
            i = 13;
        } else if (pos >= 759 && pos < 806) {
            i = 14;
        } else if (pos >= 806 && pos < 1028) {
            i = 15;
        } else if (pos >= 1028 && pos < 1107) {
            i = 16;
        } else if (pos >= 1107 && pos < 1166) {
            i = 17;
        } else if (pos >= 1166 && pos < 1194) {
            i = 18;
        } else if (pos >= 1194 && pos < 1240) {
            i = 19;
        } else if (pos >= 1240 && pos < 1267) {
            i = 20;
        } else if (pos >= 1267 && pos < 1303) {
            i = 21;
        } else if (pos >= 1303 && pos < 1329) {
            i = 22;
        } else if (pos >= 1329 && pos < 1364) {
            i = 23;
        } else if (pos >= 1364 && pos < 1391) {
            i = 24;
        } else if (pos >= 1391 && pos < 1427) {
            i = 25;
        } else if (pos >= 1427 && pos < 1453) {
            i = 26;
        } else if (pos >= 1453 && pos < 1487) {
            i = 27;
        } else if (pos >= 1487 && pos < 1511) {
            i = 28;
        } else if (pos >= 1511 && pos < 1545) {
            i = 29;
        } else if (pos >= 1545 && pos < 1571) {
            i = 30;
        } else if (pos >= 1571 && pos < 1611) {
            i = 31;
        } else if (pos >= 1611 && pos < 1639) {
            i = 32;
        } else if (pos >= 1639 && pos < 1677) {
            i = 33;
        } else if (pos >= 1677 && pos < 1707) {
            i = 34;
        } else if (pos >= 1707 && pos < 1766) {
            i = 35;
        } else if (pos >= 1766 && pos < 1806) {
            i = 36;
        } else if (pos >= 1806 && pos < 1846) {
            i = 37;
        } else if (pos >= 1846 && pos < 1872) {
            i = 38;
        } else if (pos >= 1872 && pos < 1924) {
            i = 39;
        } else if (pos >= 1924 && pos < 1956) {
            i = 40;
        } else if (pos >= 1956 && pos < 2002) {
            i = 41;
        } else if (pos >= 2002 && pos < 2031) {
            i = 42;
        } else if (pos >= 2031 && pos < 2066) {
            i = 43;
        } else if (pos >= 2066 && pos < 2089) {
            i = 44;
        } else if (pos >= 2089 && pos < 2132) {
            i = 45;
        } else if (pos >= 2132 && pos < 2162) {
            i = 46;
        } else if (pos >= 2162 && pos < 2192) {
            i = 47;
        } else if (pos >= 2192 && pos < 2212) {
            i = 48;
        } else if (pos >= 2212 && pos < 2238) {
            i = 49;
        } else if (pos >= 2238 && pos < 2258) {
            i = 50;
        } else if (pos >= 2258 && pos < 2289) {
            i = 51;
        } else if (pos >= 2289 && pos < 2309) {
            i = 52;
        } else if (pos >= 2309 && pos < 2344) {
            i = 53;
        } else if (pos >= 2344 && pos < 2409) {
            i = 54;
        } else if (pos >= 2409 && pos < 2430) {
            i = 55;
        } else if (pos >= 2430 && pos < 2468) {
            i = 56;
        } else if (pos >= 2468 && pos < 2499) {
            i = 57;
        } else if (pos >= 2499 && pos < 2520) {
            i = 58;
        } else if (pos >= 2520 && pos < 2572) {
            i = 59;
        } else if (pos >= 2572 && pos < 2600) {
            i = 60;
        } else if (pos >= 2600 && pos < 2674) {
            i = 61;
        } else if (pos >= 2674 && pos < 2721) {
            i = 62;
        } else if (pos >= 2721 && pos < 2770) {
            i = 63;
        } else if (pos >= 2770 && pos < 2800) {
            i = 64;
        } else if (pos >= 2800 && pos < 2874) {
            i = 65;
        } else if (pos >= 2874 && pos < 2912) {
            i = 66;
        } else if (pos >= 2912 && pos < 2940) {
            i = 67;
        } else if (pos >= 2940 && pos < 2961) {
            i = 68;
        } else if (pos >= 2961 && pos < 3010) {
            i = 69;
        } else if (pos >= 3010 && pos < 3036) {
            i = 70;
        } else if (pos >= 3036 && pos < 3077) {
            i = 71;
        } else if (pos >= 3077 && pos < 3102) {
            i = 72;
        } else if (pos >= 3102 && pos < 3135) {
            i = 73;
        } else if (pos >= 3135 && pos < 3158) {
            i = 74;
        } else if (pos >= 3158 && pos < 3187) {
            i = 75;
        } else if (pos >= 3187 && pos < 3209) {
            i = 76;
        } else if (pos >= 3209 && pos < 3269) {
            i = 77;
        } else if (pos >= 3269 && pos < 3307) {
            i = 78;
        } else if (pos >= 3307 && pos < 3339) {
            i = 79;
        } else if (pos >= 3339 && pos < 3357) {
            i = 80;
        } else if (pos >= 3357 && pos < 3394) {
            i = 81;
        } else if (pos >= 3394 && pos < 3418) {
            i = 82;
        } else if (pos >= 3418 && pos < 3513) {
            i = 83;
        } else if (pos >= 3513 && pos < 3566) {
            i = 84;
        } else if (pos >= 3566 && pos < 3603) {
            i = 85;
        } else if (pos >= 3603 && pos < 3629) {
            i = 86;
        } else if (pos >= 3629 && pos < 3663) {
            i = 87;
        } else if (pos >= 3663 && pos < 3686) {
            i = 88;
        } else if (pos >= 3686 && pos < 3712) {
            i = 89;
        } else if (pos >= 3712 && pos < 3735) {
            i = 90;
        } else if (pos >= 3735 && pos < 3768) {
            i = 91;
        } else if (pos >= 3768 && pos < 3792) {
            i = 92;
        } else if (pos >= 3792 && pos < 3822) {
            i = 93;
        } else if (pos >= 3822 && pos < 3844) {
            i = 94;
        } else if (pos >= 3844 && pos < 3878) {
            i = 95;
        } else if (pos >= 3878 && pos < 3901) {
            i = 96;
        } else if (pos >= 3901 && pos < 3944) {
            i = 97;
        } else if (pos >= 3944 && pos < 3972) {
            i = 98;
        } else if (pos >= 3972 && pos < 4005) {
            i = 99;
        } else if (pos >= 4005 && pos < 4027) {
            i = 100;
        } else if (pos >= 4027 && pos < 4070) {
            i = 101;
        } else if (pos >= 4070 && pos < 4097) {
            i = 102;
        } else if (pos >= 4097 && pos < 4132) {
            i = 103;
        } else if (pos >= 4132 && pos < 4157) {
            i = 104;
        } else if (pos >= 4157 && pos < 4185) {
            i = 105;
        } else if (pos >= 4185 && pos < 4207) {
            i = 106;
        } else if (pos >= 4207 && pos < 4237) {
            i = 107;
        } else if (pos >= 4237 && pos < 4258) {
            i = 108;
        } else if (pos >= 4258 && pos < 4309) {
            i = 109;
        } else if (pos >= 4309 && pos < 4343) {
            i = 110;
        } else if (pos >= 4343 && pos < 4394) {
            i = 111;
        } else if (pos >= 4394 && pos < 4423) {
            i = 112;
        } else if (pos >= 4423 && pos < 4452) {
            i = 113;
        } else if (pos >= 4452 && pos < 4473) {
            i = 114;
        } else if (pos >= 4473 && pos < 4522) {
            i = 115;
        } else if (pos >= 4522 && pos < 4557) {
            i = 116;
        } else if (pos >= 4557 && pos < 4593) {
            i = 117;
        } else if (pos >= 4593 && pos < 4613) {
            i = 118;
        } else if (pos >= 4613 && pos < 4656) {
            i = 119;
        } else if (pos >= 4656 && pos < 4679) {
            i = 120;
        } else if (pos >= 4679 && pos < 4712) {
            i = 121;
        } else if (pos >= 4712 && pos < 4734) {
            i = 122;
        } else if (pos >= 4734 && pos < 4768) {
            i = 123;
        } else if (pos >= 4768 && pos < 4791) {
            i = 124;
        } else if (pos >= 4791 && pos < 4851) {
            i = 125;
        } else if (pos >= 4851 && pos < 4886) {
            i = 126;
        } else if (pos >= 4886 && pos < 4926) {
            i = 127;
        } else if (pos >= 4926 && pos < 4951) {
            i = 128;
        } else if (pos >= 4951 && pos < 4999) {
            i = 129;
        } else if (pos >= 4999 && pos < 5034) {
            i = 130;
        } else if (pos >= 5034 && pos < 5063) {
            i = 131;
        } else if (pos >= 5063 && pos < 5083) {
            i = 132;
        } else if (pos >= 5083 && pos < 5139) {
            i = 133;
        } else if (pos >= 5139 && pos < 5171) {
            i = 134;
        } else if (pos >= 5171 && pos < 5211) {
            i = 135;
        } else if (pos >= 5211 && pos < 5237) {
            i = 136;
        } else if (pos >= 5237 && pos < 5274) {
            i = 137;
        } else if (pos >= 5274 && pos < 5298) {
            i = 138;
        } else if (pos >= 5298 && pos < 5398) {
            i = 139;
        } else if (pos >= 5398) {
            i = 140;
        }


        return i;
    }

    public int getPosFromSoundTranslate3(int pos) {
        // آرایه‌ای از بازه‌ها، هر بازه شامل مقدار شروع و پایان است
        int[][] ranges = {
                {0, 80}, {80, 158}, {158, 232}, {232, 310}, {310, 353},
                {353, 400}, {400, 429}, {429, 491}, {491, 525}, {525, 567},
                {567, 593}, {593, 644}, {644, 671}, {671, 759}, {759, 806},
                {806, 1028}, {1028, 1107}, {1107, 1166}, {1166, 1194}, {1194, 1240},
                {1240, 1267}, {1267, 1303}, {1303, 1329}, {1329, 1364}, {1364, 1391},
                {1391, 1427}, {1427, 1453}, {1453, 1487}, {1487, 1511}, {1511, 1545},
                {1545, 1571}, {1571, 1611}, {1611, 1639}, {1639, 1677}, {1677, 1707},
                {1707, 1766}, {1766, 1806}, {1806, 1846}, {1846, 1872}, {1872, 1924},
                {1924, 1956}, {1956, 2002}, {2002, 2031}, {2031, 2066}, {2066, 2089},
                {2089, 2132}, {2132, 2162}, {2162, 2192}, {2192, 2212}, {2212, 2238},
                {2238, 2258}, {2258, 2289}, {2289, 2309}, {2309, 2344}, {2344, 2430},
                {2430, 2468}, {2468, 2499}, {2499, 2520}, {2520, 2572}, {2572, 2600},
                {2600, 2674}, {2674, 2721}, {2721, 2770}, {2770, 2800}, {2800, 2874},
                {2874, 2912}, {2912, 2940}, {2940, 2961}, {2961, 3010}, {3010, 3036},
                {3036, 3077}, {3077, 3135}, {3077, 3102}, {3102, 3135}, {3135, 3158}, {3158, 3187}, {3187, 3209},
                {3209, 3269}, {3269, 3307}, {3307, 3339}, {3339, 3394}, {3394, 3418},
                {3418, 3513}, {3513, 3566}, {3566, 3603}, {3603, 3629}, {3629, 3663},
                {3663, 3686}, {3686, 3712}, {3712, 3735}, {3735, 3768}, {3768, 3792},
                {3792, 3822}, {3822, 3844}, {3844, 3878}, {3878, 3901}, {3901, 3944},
                {3944, 3972}, {3972, 4005}, {4005, 4027}, {4027, 4070}, {4070, 4097},
                {4097, 4132}, {4132, 4157}, {4157, 4185}, {4185, 4207}, {4207, 4237},
                {4237, 4258}, {4258, 4309}, {4309, 4343}, {4343, 4394}, {4394, 4423},
                {4423, 4452}, {4452, 4473}, {4473, 4522}, {4522, 4557}, {4557, 4593},
                {4593, 4613}, {4613, 4656}, {4656, 4679}, {4679, 4712}, {4712, 4734},
                {4734, 4768}, {4768, 4791}, {4791, 4851}, {4851, 4886}, {4886, 4926},
                {4926, 4951}, {4951, 4999}, {4999, 5034}, {5034, 5063}, {5063, 5083},
                {5083, 5139}, {5139, 5171}, {5171, 5211}, {5211, 5237}, {5237, 5274},
                {5274, 5298}, {5398, Integer.MAX_VALUE} // برای مقدارهای بیشتر از 5398
        };

        // حلقه‌ای برای پیدا کردن بازه مناسب
        for (int i = 0; i < ranges.length; i++) {
            if (pos >= ranges[i][0] && pos < ranges[i][1]) {
                return i; // مقدار بازه پیدا شده را برمی‌گرداند
            }
        }

        return -1; // مقدار پیش‌فرض در صورت پیدا نشدن بازه
    }

    public int getPosFromSoundTranslate2(int pos) {
        // تعریف آرایه‌های بازه‌ها و مقادیر i
        int[] ranges = {0, 8, 158, 232, 310, 353, 400, 429, 491, 525, 567, 593, 644, 671, 759, 806, 1028, 1107, 1166, 1194, 1241,
                1267, 1303, 1329, 1364, 1391, 1427, 1453, 1487, 1511, 1545, 1571, 1611, 1639, 1677, 1707, 1767, 1806, 1846,
                1872, 1924, 1956, 2002, 2037, 2066, 2089, 2132, 2162, 2192, 2212, 2238, 2258, 2289, 2311, 2344, 2430, 2468,
                2499, 2520, 2572, 2600, 2674, 2721, 2770, 2800, 2874, 2912, 2940, 2961, 3010, 3036, 3077, 3102, 3135, 3158, 3187,
                3209, 3269, 3307, 3339, 3394, 3418, 3513, 3566, 3603, 3629, 3663, 3686, 3712, 3735, 3768, 3792, 3822, 3844,
                3878, 3901, 3944, 3972, 4005, 4027, 4070, 4097, 4132, 4157, 4185, 4207, 4237, 4258, 4309, 4343, 4394, 4423,
                4452, 4473, 4522, 4557, 4593, 4613, 4656, 4679, 4712, 4734, 4768, 4791, 4851, 4886, 4926, 4951, 4999, 5034,
                5063, 5083, 5139, 5171, 5211, 5237, 5274, 5298, 5398};

        // مقدار i
        int i = 1;

        // جستجو برای پیدا کردن مقدار i بر اساس pos
        for (int j = 0; j < ranges.length; j++) {
            if (pos < ranges[j]) {
                i = j;
                break;
            }
        }

        // بازگشت مقدار i
        return i;
    }

    public interface CallbackChanged {

        void done();

    }
}
