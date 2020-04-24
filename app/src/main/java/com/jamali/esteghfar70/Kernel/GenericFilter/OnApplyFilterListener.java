package com.jamali.esteghfar70.Kernel.GenericFilter;


import com.jamali.esteghfar70.Kernel.Controller.Domain.FilteredDomain;

import java.util.HashMap;

public interface OnApplyFilterListener {
    void onApply(HashMap<Integer, FilteredDomain> domainInfos);
}
