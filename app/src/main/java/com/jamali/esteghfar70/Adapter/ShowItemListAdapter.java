package com.jamali.esteghfar70.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jamali.esteghfar70.Domain.ShowList;
import com.jamali.esteghfar70.R;

import java.util.ArrayList;

public class ShowItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<ShowList> array_object;
    private int position;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            row_index = position;
            notifyDataSetChanged();
        }
    };
    private static final int TEXT = 0;
    private static final int SUBJECT = 1;
    private static final int ROW = 2;
    private static final int ROW2 = 3;
    private static final int SUBJECT_SUB = 4;
    private static final int ROW_SUB = 5;
    private static final int WARNING = 6;
    private int row_index = -1;

    public ShowItemListAdapter(ArrayList<ShowList> result) {

        this.array_object = result;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_text, parent, false);
            view.setOnClickListener(mOnClickListener);
            return new TextViewHolder(view);
        } else if (viewType == SUBJECT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder, parent, false);
            return new SubjectViewHolder(view);
        } else if (viewType == ROW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_row, parent, false);
            return new RowViewHolder(view);
        } else if (viewType == ROW2) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_row, parent, false);
            return new Row2ViewHolder(view);
        } else if (viewType == SUBJECT_SUB) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder, parent, false);
            return new SubjectSubViewHolder(view);
        } else if (viewType == ROW_SUB) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_row, parent, false);
            return new RowSubViewHolder(view);
        } else if (viewType == WARNING) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_warning, parent, false);
            return new WarningViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_text, parent, false);
            return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            initText((TextViewHolder) holder, position);
        } else if (holder instanceof SubjectViewHolder) {
            initSubject((SubjectViewHolder) holder, position);

        } else if (holder instanceof RowViewHolder) {
            initRow((RowViewHolder) holder, position);

        } else if (holder instanceof Row2ViewHolder) {
            initRow2((Row2ViewHolder) holder, position);
        } else if (holder instanceof SubjectSubViewHolder) {
            initSubjectSub((SubjectSubViewHolder) holder, position);
        } else if (holder instanceof RowSubViewHolder) {
            initRowSub((RowSubViewHolder) holder, position);
        } else if (holder instanceof WarningViewHolder) {
            initWarning((WarningViewHolder) holder, position);
        }


    }

    private void initText(TextViewHolder holder, int position) {

        holder.title.setText(array_object.get(position).getSubject());
        if(array_object.get(position).getCategory()==null || array_object.get(position).getCategory().equals("null")){
            holder.catLayout.setVisibility(View.GONE);
        }else{
            holder.catLayout.setVisibility(View.VISIBLE);
        }
        holder.category.setText("بند "+array_object.get(position).getCategory());
        holder.Linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if (array_object.get(position).getSeleccted().equals("0")) {
            holder.Linear.setBackgroundColor(Color.parseColor("#00ffffff"));
        } else {
            holder.Linear.setBackgroundColor(Color.parseColor("#78A4FD7A"));
        }


//        if (row_index == position) {
////            holder.Linear.setBackgroundColor(Color.parseColor("#FFF59D"));
//            holder.Linear.setBackgroundColor(Color.parseColor("#78A4FD7A"));
////            holder.title.setTextColor(Color.parseColor("#00ffffff"));
//        } else {
//            holder.Linear.setBackgroundColor(Color.parseColor("#00ffffff"));
////            holder.title.setTextColor(Color.parseColor("#000000"));
//        }
    }

    private void initSubject(SubjectViewHolder holder, int position) {
        holder.title.setText(array_object.get(position).getSubject());
    }

    private void initRow(RowViewHolder holder, int position) {
        holder.title.setText(array_object.get(position).getSubject());
    }

    private void initRow2(Row2ViewHolder holder, int position) {
        holder.title.setText(array_object.get(position).getSubject());
    }

    private void initSubjectSub(SubjectSubViewHolder holder, int position) {
        holder.title.setText(array_object.get(position).getSubject());
    }

    private void initRowSub(RowSubViewHolder holder, int position) {
        holder.title.setText(array_object.get(position).getSubject());
    }

    private void initWarning(WarningViewHolder holder, int position) {
        holder.title.setText(array_object.get(position).getSubject());
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

        if (array_object.get(position).getKind().equals("text")) {
            return TEXT;
        } else if (array_object.get(position).getKind().equals("subject")) {
            return SUBJECT;
        } else if (array_object.get(position).getKind().equals("row")) {
            return ROW;
        } else if (array_object.get(position).getKind().equals("row2")) {
            return ROW2;
        } else if (array_object.get(position).getKind().equals("subject_sub")) {
            return SUBJECT_SUB;
        } else if (array_object.get(position).getKind().equals("row_sub")) {
            return ROW_SUB;
        } else if (array_object.get(position).getKind().equals("warning")) {
            return WARNING;
        } else {
            return TEXT;
        }

    }


    class TextViewHolder extends RecyclerView.ViewHolder {
        TextView title,category;
        LinearLayout Linear;
ConstraintLayout catLayout;
        private TextViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.categoryTxt);
            Linear = itemView.findViewById(R.id.Linear);
            catLayout = itemView.findViewById(R.id.CatLayout);
        }
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        private SubjectViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    class RowViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        private RowViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    class Row2ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        private Row2ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    class SubjectSubViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        private SubjectSubViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    class RowSubViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        private RowSubViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    class WarningViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        private WarningViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}