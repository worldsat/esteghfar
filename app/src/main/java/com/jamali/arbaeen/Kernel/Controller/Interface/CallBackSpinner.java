package com.jamali.arbaeen.Kernel.Controller.Interface;


import com.jamali.arbaeen.Kernel.Controller.Domain.SpinnerDomain;

import java.util.ArrayList;

public interface CallBackSpinner {
    void onSuccess(ArrayList<SpinnerDomain> result);

    void onError(String error);
}
