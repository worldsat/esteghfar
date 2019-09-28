package com.jamali.arbaeen.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jamali.arbaeen.Adapter.ShowItemListAdapter;
import com.jamali.arbaeen.Domain.ShowList;
import com.jamali.arbaeen.Kernel.Activity.BaseActivity;
import com.jamali.arbaeen.Kernel.Controller.Interface.CallbackGet;
import com.jamali.arbaeen.R;

import java.util.ArrayList;
import java.util.Collection;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowMiddleActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<ShowList> response = new ArrayList<>();
    private int position, List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        initview();
        setVariable();
    }

    private void setVariable() {
        position = getIntent().getIntExtra("Position", 0);
        List = getIntent().getIntExtra("List", 0);

        controller().Get(ShowList.class, null, List, position, true, new CallbackGet() {
            @Override
            public <T> void onSuccess(ArrayList<T> result, int count) {
                Log.i(TAG, "onSuccess: " + result);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowMiddleActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                response.addAll((Collection<? extends ShowList>) result);
                adapter = new ShowItemListAdapter(response,List);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {

            }
        });

    }

    private void initview() {
        recyclerView = findViewById(R.id.view);
    }
}
