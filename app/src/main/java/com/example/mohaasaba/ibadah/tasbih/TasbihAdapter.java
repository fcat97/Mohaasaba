package com.example.mohaasaba.ibadah.tasbih;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import at.grabner.circleprogress.CircleProgressView;
import com.example.mohaasaba.R;
import com.example.mohaasaba.models.Progress;

import java.util.Calendar;

public class TasbihAdapter extends ListAdapter<Tasbih, TasbihAdapter.ViewHolder> {
    private static final String TAG = TasbihAdapter.class.getCanonicalName();
    private ItemClickListener itemClickListener;
    private ItemLongClickListener itemLongClickListener;

    public TasbihAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Tasbih> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tasbih>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tasbih oldItem, @NonNull Tasbih newItem) {
            return oldItem.tasbihID.equals(newItem.tasbihID);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tasbih oldItem, @NonNull Tasbih newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasbih, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tasbih tasbih = getItem(position);

        holder.label_tv.setText(tasbih.label);
        holder.reward_tv.setText(tasbih.reward);
        Progress progress = tasbih.history.getProgress(Calendar.getInstance());
        holder.progressView.setMaxValue(progress.target);
        holder.progressView.setBlockCount(Math.round((float) progress.target / progress.step));
        holder.progressView.setValueAnimated(Math.max(progress.progress - progress.step, 0),
                progress.progress, 1000); // Animation Fix

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onClick(tasbih);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (itemClickListener != null) {
                Progress p = tasbih.history.getProgress(Calendar.getInstance()).doProgress();
                tasbih.history.commitProgress(p, Calendar.getInstance());
                notifyItemChanged(position);
                itemLongClickListener.onLongClick(tasbih);
                return true;
            } else return false;
        });

        if (progress.progress == progress.target) {
            holder.progressView.setVisibility(View.INVISIBLE);
            holder.completedImageView.setVisibility(View.VISIBLE);
        } else {
            holder.progressView.setVisibility(View.VISIBLE);
            holder.completedImageView.setVisibility(View.INVISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView label_tv;
        private TextView reward_tv;
        private TextView sl_tv;
        private CircleProgressView progressView;
        private ImageView completedImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            label_tv = itemView.findViewById(R.id.label_TextView_ItemTasbih);
            reward_tv = itemView.findViewById(R.id.reward_TextView_ItemTasbih);
            progressView = itemView.findViewById(R.id.circularProgressView_ItemTasbih);
            completedImageView = itemView.findViewById(R.id.completed_ImageView_ItemTasbih);
        }
    }

    public TasbihAdapter setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public TasbihAdapter setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
        return this;
    }

    public interface ItemClickListener {
        void onClick(Tasbih tasbih);
    }
    public interface ItemLongClickListener {
        void onLongClick(Tasbih tasbih);
    }
}
