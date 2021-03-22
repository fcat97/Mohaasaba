package com.example.mohaasaba.plans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;

import at.grabner.circleprogress.CircleProgressView;

public class SubPlanAdapter extends ListAdapter<SubPlan, SubPlanAdapter.SubPlanHolder> {
    private DeleteButtonListener deleteButtonListener;

    public SubPlanAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<SubPlan> DIFF_CALLBACK = new DiffUtil.ItemCallback<SubPlan>() {
        @Override
        public boolean areItemsTheSame(@NonNull SubPlan oldItem, @NonNull SubPlan newItem) {
            return oldItem.label.equals(newItem.label);
        }

        @Override
        public boolean areContentsTheSame(@NonNull SubPlan oldItem, @NonNull SubPlan newItem) {
            return oldItem.equals(newItem);
        }
    };


    @NonNull
    @Override
    public SubPlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subplan, parent, false);

        return new SubPlanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubPlanHolder holder, int position) {
        SubPlan subPlan = getItem(position);
        float progress = 0f;
        if (subPlan.track_time) progress = (float) subPlan.time_goal / subPlan.time_progress;
        else if (subPlan.track_count) progress = (float) subPlan.count_goal / subPlan.count_progress;

        holder.circleProgressView.setMaxValue(100f);
        holder.circleProgressView.setValue(progress);
        if (progress == 100f) {
            holder.circleProgressView.setVisibility(View.INVISIBLE);
            holder.completeImageView.setVisibility(View.VISIBLE);
        } else {
            holder.circleProgressView.setVisibility(View.VISIBLE);
            holder.completeImageView.setVisibility(View.INVISIBLE);
        }

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteButtonListener != null) deleteButtonListener.onClick(position);
        });
    }

    static class SubPlanHolder extends RecyclerView.ViewHolder {
        private CircleProgressView circleProgressView;
        private ImageView completeImageView;
        private TextView titleView, goalTextView;
        private ImageButton deleteButton;

        public SubPlanHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.title_TextView_item_subplan);
            goalTextView = itemView.findViewById(R.id.goal_TextView_item_subplan);
            circleProgressView = itemView.findViewById(R.id.circularProgressView_item_subplan);
            completeImageView = itemView.findViewById(R.id.completed_ImageView_item_subplan);
            deleteButton = itemView.findViewById(R.id.deleteButton_ImageButton_item_subplan);
        }
    }

    public SubPlanAdapter setDeleteButtonListener(DeleteButtonListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
        return this;
    }

    public interface DeleteButtonListener {
        void onClick(int index);
    }
}
