package com.example.mohaasaba.ibadah.tasbih;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ItemClickListener itemClickListener;

    public TasbihAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<Tasbih> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tasbih>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tasbih oldItem, @NonNull Tasbih newItem) {
            return oldItem.tasbihID.equals(newItem.tasbihID);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tasbih oldItem, @NonNull Tasbih newItem) {
            return oldItem.label.equals(newItem.label)
                    && oldItem.reward.equals(newItem.reward)
                    && oldItem.history.getProgress(Calendar.getInstance()).equals(newItem.history.getProgress(Calendar.getInstance()));
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

//        holder.sl_tv.setText(String.valueOf(position + 1));
        holder.label_tv.setText(tasbih.label);
        holder.reward_tv.setText(tasbih.reward);
        Progress progress = tasbih.history.getProgress(Calendar.getInstance());
        float p = (float) progress.progress / progress.target;
        holder.progressView.setValue(p);

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) itemClickListener.onClick(tasbih);
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView label_tv;
        private TextView reward_tv;
        private TextView sl_tv;
        private CircleProgressView progressView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            label_tv = itemView.findViewById(R.id.label_TextView_ItemTasbih);
            reward_tv = itemView.findViewById(R.id.reward_TextView_ItemTasbih);
            progressView = itemView.findViewById(R.id.circularProgressView_ItemTasbih);
//            sl_tv = itemView.findViewById(R.id.itemSL_TextView_ItemTasbih);
        }
    }

    public TasbihAdapter setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public interface ItemClickListener {
        void onClick(Tasbih tasbih);
    }
}
