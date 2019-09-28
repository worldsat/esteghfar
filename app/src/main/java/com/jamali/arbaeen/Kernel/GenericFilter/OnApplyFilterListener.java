package com.jamali.arbaeen.Kernel.GenericFilter;


import com.jamali.arbaeen.Kernel.Controller.Domain.FilteredDomain;

import java.util.HashMap;

public interface OnApplyFilterListener {
    void onApply(HashMap<Integer, FilteredDomain> domainInfos);
}
