package com.jamali.arbaeen.Kernel.GenericFilter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jamali.arbaeen.Kernel.Controller.Domain.DomainInfo;
import com.jamali.arbaeen.Kernel.Controller.Domain.FilteredDomain;
import com.jamali.arbaeen.Kernel.Controller.GenericListBll;
import com.jamali.arbaeen.R;

import java.util.ArrayList;
import java.util.HashMap;

public class GenericFilterDialog extends Dialog {
    private final Class domain;
    private final OnApplyFilterListener onApply;
    private HashMap<Integer, FilteredDomain> filteredDomain;

    public GenericFilterDialog(@NonNull Context context, Class domain, HashMap<Integer, FilteredDomain> filteredMap, OnApplyFilterListener listener) {
        super(context);
        this.domain = domain;
        this.onApply = listener;
        this.filteredDomain = filteredMap;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_filter_dynamic);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        initViews();
    }

    private void initViews() {

        TextView apply = findViewById(R.id.apply_filter);
        TextView title = findViewById(R.id.textView13);
        title.setText("فیلتر");
        //create adapter
        GenericListBll bll = new GenericListBll(getContext());
        ArrayList<DomainInfo> domainInfos = bll.getDomainInfo(domain);

        //filter data where mode is either FILTER or ALL
        ArrayList<DomainInfo> filterDomains = bll.getFilterDomains(domainInfos);

        GenericFilterAdapter genericFilterAdapter = new GenericFilterAdapter(
                filterDomains,
                filteredDomain);

        //set adapter to recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_dialog);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setAdapter(genericFilterAdapter);


        apply.setOnClickListener(v -> {
            dismiss();
            onApply.onApply(filteredDomain);
        });

    }

}
