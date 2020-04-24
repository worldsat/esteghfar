package com.jamali.esteghfar70.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jamali.esteghfar70.Activity.ShowMiddleActivity;
import com.jamali.esteghfar70.Domain.MiddleList;
import com.jamali.esteghfar70.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MiddleListAdapter extends RecyclerView.Adapter<MiddleListAdapter.ViewHolder> {

    private final ArrayList<MiddleList> array_object;

    public MiddleListAdapter(ArrayList<MiddleList> result) {

        this.array_object = result;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

       holder.title.setText( array_object.get(position).getSubject());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), ShowMiddleActivity.class);
                intent.putExtra("Position", position+1);
                intent.putExtra("List", 2);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array_object == null ? 0 : array_object.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        private ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);

        }
    }
}