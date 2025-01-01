package com.jamali.esteghfar70.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
    private CallbackClicked callbackClicked;
    private Context context;
    private boolean darkMode;
    private static final int TEXT = 0;
    private static final int SUBJECT = 1;
    private static final int ROW = 2;
    private static final int ROW2 = 3;
    private static final int SUBJECT_SUB = 4;

    private int row_index = -1;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;
    public ShowItemListAdapter(ArrayList<ShowList> result, boolean darkMode, CallbackClicked callbackClicked) {

        this.array_object = result;
        this.callbackClicked = callbackClicked;
        this.darkMode = darkMode;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        if (viewType == TEXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_text, parent, false);
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
        }


    }

    private void initText(TextViewHolder holder, int position) {

        holder.title.setText((array_object.get(position).getSubject()));
        holder.title.setTextSize(array_object.get(position).getSize());
        if (array_object.get(position).isDarkMode()) {
            holder.title.setTextColor(context.getResources().getColor(R.color.yellow_800));
        }else{
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
        }
        if (array_object.get(position).getCategory() == null || array_object.get(position).getCategory().equals("null")) {
            holder.catLayout.setVisibility(View.GONE);
        } else {
            holder.catLayout.setVisibility(View.VISIBLE);
        }
        holder.category.setText("بند " + array_object.get(position).getCategory());
        holder.Linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;

                Log.i("moh3n", "onPos: "+row_index);
                if (position != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = selectedPosition;
                    selectedPosition = position;
                    notifyItemChanged(lastSelectedPosition);
                    notifyItemChanged(selectedPosition);
                }
                notifyDataSetChanged();
                callbackClicked.onClick(position);
            }
        });
        if (array_object.get(position).getSeleccted().equals("0")) {
            holder.Linear.setBackgroundColor(Color.parseColor("#00ffffff"));
        } else {
            if(array_object.get(position).isDarkMode()){
                holder.Linear.setBackgroundColor(context.getResources().getColor(R.color.black4));
            }else{
            holder.Linear.setBackgroundColor(Color.parseColor("#78A4FD7A"));
            }
        }
        if (selectedPosition == position) {

            if(array_object.get(position).isDarkMode()){
                holder.Linear.setBackgroundColor(context.getResources().getColor(R.color.black4));
            }else{
                holder.Linear.setBackgroundColor(Color.parseColor("#78A4FD7A"));
            }
        }else{
            holder.Linear.setBackgroundColor(Color.parseColor("#00ffffff"));
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
        holder.title.setTextSize(array_object.get(position).getSize());

        holder.Linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                row_index = position;
                Log.i("moh3n", "onPos: "+row_index);
                if (position != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = selectedPosition;
                    selectedPosition = position;
                    notifyItemChanged(lastSelectedPosition);
                    notifyItemChanged(selectedPosition);
                }
                notifyDataSetChanged();
                callbackClicked.onClick(position);
            }
        });
        if (array_object.get(position).getSeleccted().equals("0")) {
            holder.Linear.setBackgroundColor(Color.parseColor("#00ffffff"));
        } else {
            if(array_object.get(position).isDarkMode()){
                holder.Linear.setBackgroundColor(context.getResources().getColor(R.color.black4));
            }else{
                holder.Linear.setBackgroundColor(Color.parseColor("#78A4FD7A"));
            }
        }
        if (selectedPosition == position) {

            if(array_object.get(position).isDarkMode()){
                holder.Linear.setBackgroundColor(context.getResources().getColor(R.color.black4));
            }else{
                holder.Linear.setBackgroundColor(Color.parseColor("#78A4FD7A"));
            }
        }else{
            holder.Linear.setBackgroundColor(Color.parseColor("#00ffffff"));
        }
    }

    private void initRow2(Row2ViewHolder holder, int position) {
        holder.title.setText(array_object.get(position).getSubject());
    }

    private void initSubjectSub(SubjectSubViewHolder holder, int position) {
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
        } else {
            return TEXT;
        }

    }


    class TextViewHolder extends RecyclerView.ViewHolder {
        TextView title, category;
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
        LinearLayout Linear;
        private RowViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            Linear = itemView.findViewById(R.id.Linear);
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


    private SpannableString getColoredArabicText(String text) {
        SpannableString spannable = new SpannableString(text);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // بررسی کنید که آیا کاراکتر اعراب (حرکت) است یا نه
            if (isArabicDiacritic(c)) {
                spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red_900)), i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }
        return spannable;
    }


    // بررسی اینکه کاراکتر جزء اعراب (حرکات) است یا خیر
    private boolean isArabicDiacritic(char c) {
        // محدوده کد یونیکد برای اعراب عربی: U+0610 تا U+061A و U+064B تا U+065F
        return (c >= 0x0610 && c <= 0x061A) || (c >= 0x064B && c <= 0x065F);
    }

    public interface CallbackClicked {

        public void onClick(int pos);

    }
}