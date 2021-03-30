package com.example.mohaasaba.plans;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import at.grabner.circleprogress.CircleProgressView;

public class SubPlanActionAdapter extends ListAdapter<SubPlan, SubPlanActionAdapter.SubPlanHolder> {
    private static final String TAG = SubPlanActionAdapter.class.getCanonicalName();
    private SubPlanChangeListener changeListener;

    public SubPlanActionAdapter() {
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
        View view = View.inflate(parent.getContext(), R.layout.item_subplan_action, null);

        return new SubPlanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubPlanHolder holder, int position) {
        SubPlan subPlan = getItem(position);
        holder.label_tv.setText(subPlan.label);
        updateProgressView(holder, subPlan);

        // Handler for TimeCount -------------------------------------------------------------------
        AtomicLong startingTime = new AtomicLong();
        AtomicBoolean clockRunning = new AtomicBoolean(false);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: THIS IS NOT THREAD SAFE!! MAKE IT SO...
                long passedTime = SystemClock.uptimeMillis() - startingTime.get();
                Log.d(TAG, "run: i'm running : " + passedTime);
                subPlan.time_progress += 1000 + (passedTime % 1000);

                // update plan in database every 10 sec
                if (passedTime % 5000 < 1000 && changeListener != null) changeListener.dataChanged();

                // loop the same thing each 1 sec
                handler.postDelayed(this, 1000);
                updateProgressView(holder, subPlan);
            }
        };

        // Set button Click Listeners --------------------------------------------------------------
        if (subPlan.track_time) {
            holder.btn_1.setVisibility(View.GONE);
            holder.btn_2.setImageResource(R.drawable.ic_play);

            holder.btn_2.setOnClickListener(v -> {
                if (! clockRunning.get()) {
                    startingTime.set(SystemClock.uptimeMillis());
                    clockRunning.set(true);
                    handler.postDelayed(runnable, 0);
                    holder.btn_2.setImageResource(R.drawable.ic_stop);
                } else {
                    clockRunning.set(false);
                    handler.removeCallbacks(runnable);
                    holder.btn_2.setImageResource(R.drawable.ic_play);
                }
            });
        } else if (subPlan.track_count) {
            holder.btn_1.setImageResource(R.drawable.ic_step);
            holder.btn_1.setRotationY(180); // decrement button
            holder.btn_2.setImageResource(R.drawable.ic_step); // increment Button

            holder.btn_1.setOnClickListener(v -> {
                subPlan.decrementCount();
                updateProgressView(holder, subPlan);
                if (changeListener != null) changeListener.dataChanged();
            });

            holder.btn_2.setOnClickListener(v -> {
                subPlan.incrementCount();
                updateProgressView(holder, subPlan);
                if (changeListener != null) changeListener.dataChanged();
            });
        }
    }

    private void updateProgressView(SubPlanHolder holder, SubPlan subPlan) {
        if (subPlan.track_count) {
            holder.circleProgressView.setMaxValue(subPlan.count_goal);
            holder.circleProgressView.setValueAnimated(subPlan.count_progress);

            if (subPlan.count_goal == subPlan.count_progress) {
                holder.completeImageView.setVisibility(View.VISIBLE);
                holder.circleProgressView.setVisibility(View.INVISIBLE);
            } else {
                holder.completeImageView.setVisibility(View.INVISIBLE);
                holder.circleProgressView.setVisibility(View.VISIBLE);
            }

            String st = subPlan.count_progress + "/" + subPlan.count_goal;
            if (subPlan.count_unit.isEmpty()) st += subPlan.count_goal == 1 ? "time" : "times";
            else st += " " + subPlan.count_unit;
            holder.goal_tv.setText(st);
        }
        else if (subPlan.track_time) {
            int hr_g = (int) (subPlan.time_goal / 1000) / 3600;
            int min_g = (int) (subPlan.time_goal / 1000) / 60;
            int sec_g = (int) (subPlan.time_goal / 1000) % 60;

            int hr = (int) ((subPlan.time_progress / 1000) / 3600);
            int min = (int) ((subPlan.time_progress / 1000) / 60);
            int sec = (int) ((subPlan.time_progress / 1000) % 60);

            String st = hr + "h:" + min + "m:" + sec + "s";
            st += "/" + hr_g + "h:" + min_g + "m:" + sec_g + "s";
            holder.goal_tv.setText(st);

            holder.circleProgressView.setMaxValue(subPlan.time_goal);
            holder.circleProgressView.setValueAnimated(subPlan.time_progress);

            if (subPlan.count_goal == subPlan.time_progress) {
                holder.completeImageView.setVisibility(View.VISIBLE);
                holder.circleProgressView.setVisibility(View.INVISIBLE);
            } else {
                holder.completeImageView.setVisibility(View.INVISIBLE);
                holder.circleProgressView.setVisibility(View.VISIBLE);
            }
        }
    }

    static class SubPlanHolder extends RecyclerView.ViewHolder {
        private TextView label_tv, goal_tv;
        private CircleProgressView circleProgressView;
        private ImageButton btn_1, btn_2;
        private ImageView completeImageView;

        public SubPlanHolder(@NonNull View itemView) {
            super(itemView);

            label_tv = itemView.findViewById(R.id.title_TextView_ItemSubPlanCount);
            goal_tv = itemView.findViewById(R.id.goal_TextView_ItemSubPlanCount);

            completeImageView = itemView.findViewById(R.id.completed_ImageView_ItemSubPlanCount);

            circleProgressView = itemView.findViewById(R.id.circularProgressView_ItemSubPlanCount);
            btn_1 = itemView.findViewById(R.id.button1_ImageButton_ItemSubPlanCount);
            btn_2 = itemView.findViewById(R.id.button2_ImageButton_ItemSubPlanCount);
        }
    }

    public SubPlanActionAdapter setChangeListener(SubPlanChangeListener changeListener) {
        this.changeListener = changeListener;
        return this;
    }

    public interface SubPlanChangeListener {
        void dataChanged();
    }
}
