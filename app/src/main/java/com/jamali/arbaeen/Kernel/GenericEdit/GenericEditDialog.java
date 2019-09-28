package com.jamali.arbaeen.Kernel.GenericEdit;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jamali.arbaeen.Kernel.Controller.Domain.DomainInfo;
import com.jamali.arbaeen.Kernel.Controller.GenericListBll;
import com.jamali.arbaeen.Kernel.GenericEdit.Interface.OnApplyEditListener;
import com.jamali.arbaeen.R;

import java.util.ArrayList;
import java.util.HashMap;


public class GenericEditDialog extends Dialog {
    private final Class domain;
    private final OnApplyEditListener listener;
    private Object object;

    public GenericEditDialog(@NonNull Context context,
                             Class domain,
                             Object data,
                             OnApplyEditListener listener) {
        super(context);
        this.domain = domain;
        this.listener = listener;
        object = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_edit_dynamic);
        getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        initViews();
    }

    private void initViews() {
        TextView apply = findViewById(R.id.apply_filter);

        //get data
        GenericListBll bll = new GenericListBll(getContext());
        ArrayList<DomainInfo> domainInfos = bll.getDomainInfo(domain);

        //filter data where mode is either EDIT or ALL
        ArrayList<DomainInfo> editDomains = bll.getEditDomains(domainInfos);


        // parse a data object and map the stored values
        HashMap<String, String> decodedData = new HashMap<>();
        if (object != null) {
            decodedData = bll.getIds(object, editDomains);
        }

        GenericEditAdapter genericEditAdapter = new GenericEditAdapter(editDomains, decodedData);

        //set adapter to recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_dialog);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setAdapter(genericEditAdapter);


        HashMap<String, String> finalDecodedData = decodedData;
        apply.setOnClickListener(v -> {
            dismiss();
            listener.onApplyEdit(finalDecodedData);
        });

    }
}
