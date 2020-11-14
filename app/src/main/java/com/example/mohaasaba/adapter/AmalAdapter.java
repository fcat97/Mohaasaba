package com.example.mohaasaba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.Amal;

import java.text.DateFormat;
import java.util.Calendar;

public class AmalAdapter extends ListAdapter<Amal, AmalAdapter.ViewHolder> {
    private Context mContext;

    public AmalAdapter() {
        super(DIFF_CALLBACKS);
    }

    private static final DiffUtil.ItemCallback<Amal> DIFF_CALLBACKS = new DiffUtil.ItemCallback<Amal>() {
        @Override
        public boolean areItemsTheSame(@NonNull Amal oldItem, @NonNull Amal newItem) {
            return ! oldItem.getAmalID().equals(newItem.getAmalID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Amal oldItem, @NonNull Amal newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_amal,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Amal amal = getItem(position);

        holder.title.setText(amal.title);
        holder.stateCheckBox.setChecked(amal.amalCompleted);
        holder.typeTextView.setText(amal.getType());
        holder.ratingBar.setRating(amal.perfection);

        if (amal.amalCompleted) {
            holder.completionTimeTextView.setVisibility(View.VISIBLE);
            holder.completedOnTextView.setVisibility(View.VISIBLE);

            Calendar c = Calendar.getInstance();
            c.clear();
            c.setTimeInMillis(amal.amalCompletionTime);
            holder.completionTimeTextView.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()));
        } else {
            holder.completionTimeTextView.setVisibility(View.GONE);
            holder.completedOnTextView.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private CheckBox stateCheckBox;
        private TextView typeTextView;
        private RatingBar ratingBar;
        private TextView completedOnTextView;
        private TextView completionTimeTextView;
        private RelativeLayout expandedRL;
        private boolean expanded;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expandedRL = itemView.findViewById(R.id.expanded_RelativeLayout_ItemView_Amal);
            title = itemView.findViewById(R.id.title_TextView_itemView_Amal);
            typeTextView = itemView.findViewById(R.id.typeTextView_ItemView_Amal);
            completedOnTextView = itemView.findViewById(R.id.completed_Text_ItemView_Amal);
            completionTimeTextView = itemView.findViewById(R.id.completionTime_TextView_ItemView_Amal);

            stateCheckBox = itemView.findViewById(R.id.checkBox_itemView_Amal);
            ratingBar = itemView.findViewById(R.id.ratingBar_ItemView_Amal);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expanded) expandedRL.setVisibility(View.GONE);
                    else expandedRL.setVisibility(View.VISIBLE);
                }
            });
        }
    }

}
