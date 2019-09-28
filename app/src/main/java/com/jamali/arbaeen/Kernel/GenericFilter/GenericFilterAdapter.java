package com.jamali.arbaeen.Kernel.GenericFilter;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jamali.arbaeen.Kernel.Controller.Domain.DomainInfo;
import com.jamali.arbaeen.Kernel.Controller.Domain.FilteredDomain;
import com.jamali.arbaeen.Kernel.Controller.Domain.SpinnerDomain;
import com.jamali.arbaeen.Kernel.Controller.Domain.ViewType;
import com.jamali.arbaeen.Kernel.Controller.GenericListBll;
import com.jamali.arbaeen.Kernel.Controller.Interface.CallBackSpinner;
import com.jamali.arbaeen.Kernel.Helper.Waiting;
import com.jamali.arbaeen.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class GenericFilterAdapter extends RecyclerView.Adapter<GenericFilterAdapter.ViewHolder> {

    private ArrayList<DomainInfo> filters;
    private HashMap<Integer, FilteredDomain> filterResult;
    private HashMap<Integer, ArrayList<SpinnerDomain>> spinnerData = new HashMap<>(); //loaded spinner spinnerData
    private HashMap<Integer, SpinnerDomain> spinnerSelectedMap = new HashMap<>();

    public GenericFilterAdapter(ArrayList<DomainInfo> filters, HashMap<Integer, FilteredDomain> filteredList) {
        this.filters = filters;
        filterResult = filteredList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_filter, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.columnName.setText(filters.get(position).getTitle());

        //check if the row is using either a spinner or an editText
        if (!TextUtils.isEmpty(filters.get(position).getApiAddress())) {
            holder.valueEditTex.setVisibility(View.GONE);
            holder.spinner.setVisibility(View.VISIBLE);

            //progressBar before the spinner is filled
            Waiting waiting = new Waiting(holder.itemView.getContext());
            MaterialDialog wait = waiting.alertWaiting();
            wait.show();


            //get the spinnerData to fill the spinner
            GenericListBll bll = new GenericListBll(holder.itemView.getContext());
            DomainInfo domain = filters.get(holder.getAdapterPosition());
            bll.populateSpinner(domain.getId(), domain.getViewType(), domain.getApiAddress(), new CallBackSpinner() {
                @Override
                public void onSuccess(ArrayList<SpinnerDomain> result) {
                    int adapterPosition = holder.getAdapterPosition();
                    spinnerData.put(adapterPosition, result);
                    ArrayAdapter adapter = new ArrayAdapter<>(holder.itemView.getContext(), R.layout.spinner_item_blue, result);

                    holder.spinner.setAdapter(adapter);
                    holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //save selected row
                            SpinnerDomain selectedRow = result.get(position);
                            Log.i("position", String.valueOf(position));
                            spinnerSelectedMap.put(holder.getAdapterPosition(), selectedRow);
                            Log.i("selectedId", selectedRow.getValue());
                            if (holder.switchKey.isChecked()) {
                                filterResult.remove(adapterPosition);
                                SpinnerDomain spinnerDomain = spinnerSelectedMap.get(adapterPosition);
                                filterResult.put(
                                        adapterPosition,
                                        new FilteredDomain(
                                                Objects.requireNonNull(spinnerDomain).getParentId(),
                                                spinnerDomain.getValue())
                                );
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    wait.dismiss();

                    //search for values that have already been selected
                    if (!filterResult.isEmpty()) {
                        ArrayList<Integer> positions = new ArrayList<>(filterResult.keySet());
                        for (int i = 0; i < filterResult.size(); i++) {
                            if (Objects.requireNonNull(positions.get(i)).equals(adapterPosition)) {
                                FilteredDomain item = filterResult.get(adapterPosition);
                                ArrayList<SpinnerDomain> spinnerDomains = spinnerData.get(adapterPosition);

                                for (int j = 0; j < Objects.requireNonNull(spinnerDomains).size(); j++) {
                                    String selectedValue = Objects.requireNonNull(item).getValue();
                                    String rowValue = spinnerDomains.get(j).getValue();

                                    if (selectedValue.equals(rowValue)) {
                                        holder.spinner.setSelection(j, true);
                                        SpinnerDomain selectedRow = result.get(j);
                                        spinnerSelectedMap.put(adapterPosition, selectedRow);
                                        Log.i("selectedId", selectedRow.getValue());
                                        holder.switchKey.setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    wait.dismiss();
                    Toast.makeText(holder.itemView.getContext(), "خطا در دریافت اطلاعات", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            holder.valueEditTex.setVisibility(View.VISIBLE);
            holder.spinner.setVisibility(View.GONE);
            holder.switchKey.setChecked(false);
        }

        holder.switchKey.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                int adapterPosition = holder.getAdapterPosition();
                if (!holder.valueEditTex.getText().toString().isEmpty()) {
                    DomainInfo domainInfo = filters.get(adapterPosition);
                    filterResult.put(adapterPosition, new FilteredDomain(domainInfo.getId(), holder.valueEditTex.getText().toString()));
                } else if (spinnerSelectedMap.get(adapterPosition) != null) {

                    SpinnerDomain spinnerDomain = spinnerSelectedMap.get(adapterPosition);
                    filterResult.put(adapterPosition, new FilteredDomain(Objects.requireNonNull(spinnerDomain).getParentId(), spinnerDomain.getValue()));

                } else {

                    holder.switchKey.setChecked(false);
                    Toast.makeText(holder.itemView.getContext(), "لطفا مقدار را وارد نمائید", Toast.LENGTH_SHORT).show();
                }
            }
            //undo the filter
            else {
                //empty out editText
                if (filters.get(holder.getAdapterPosition()).getViewType().isEmpty()) {
                    holder.valueEditTex.setText("");
                }
                filterResult.remove(holder.getAdapterPosition());
            }

        });

        //select rows of already spinnerSelectedMap
        if (!filterResult.isEmpty()) {
            int adapterPosition = holder.getAdapterPosition();
            ArrayList<Integer> postions = new ArrayList<>(filterResult.keySet());

            FilteredDomain filteredDomain = null;
            for (int i = 0; i < filterResult.size(); i++) {
                // if current adapter position happens to be the same as the saved postion modify that row
                if (Objects.requireNonNull(postions.get(i)).equals(adapterPosition)) {
                    filteredDomain = filterResult.get(adapterPosition);
                    DomainInfo domain = filters.get(adapterPosition);

                    //editText
                    if (domain.getViewType().equals(ViewType.EDIT_TEXT.name()) || domain.getViewType().equals(ViewType.TEXT_VIEW.name())) {
                        holder.valueEditTex.setText(Objects.requireNonNull(filteredDomain).getValue());
                        holder.switchKey.setChecked(true);
                        holder.valueEditTex.setOnClickListener(v -> {
                            holder.switchKey.setChecked(false);
                            //pass a null object to click listener to make sure it'll get triggered only once
                            holder.valueEditTex.setOnClickListener(null);
                        });
                    }
                }
            }
        }


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText valueEditTex;
        Switch switchKey;
        Spinner spinner;
        private TextView columnName;

        public ViewHolder(View itemView) {
            super(itemView);
            columnName = itemView.findViewById(R.id.columnName);
            switchKey = itemView.findViewById(R.id.switchKey);
            valueEditTex = itemView.findViewById(R.id.value);
            spinner = itemView.findViewById(R.id.spinner);
        }
    }
}
