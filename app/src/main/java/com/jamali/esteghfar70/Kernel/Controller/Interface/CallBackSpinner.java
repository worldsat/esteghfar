package com.jamali.esteghfar70.Kernel.Controller.Interface;


import com.jamali.esteghfar70.Kernel.Controller.Domain.SpinnerDomain;

import java.util.ArrayList;

public interface CallBackSpinner {
    void onSuccess(ArrayList<SpinnerDomain> result);

    void onError(String error);
}
