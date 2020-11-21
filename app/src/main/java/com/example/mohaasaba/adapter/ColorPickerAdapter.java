package com.example.mohaasaba.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.helper.ThemeUtils;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {
    private ColorPickerAdapterListener listener;
    private Context mContext;

    public ColorPickerAdapter(ColorPickerAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_recyclerview_colorpicker, null);
        this.mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int[] attrs = {R.attr.colorPrimary, R.attr.colorPrimaryDark, R.attr.colorAccent};
        int themeID = ThemeUtils.getResourceID(ThemeUtils.getThemeList().get(position));
        TypedArray typedArray = mContext.obtainStyledAttributes(themeID, attrs);
        int colorPrimary = typedArray.getColor(0, Color.BLACK);
        int colorPrimaryDark = typedArray.getColor(1, Color.BLACK); // get typedArray for each id
        int colorAccent = typedArray.getColor(2, Color.LTGRAY); // get typedArray for each id

        holder.colorButton.setBackgroundTintList(ColorStateList.valueOf(colorPrimary));
        holder.colorButton.setText(ThemeUtils.getThemeNames().get(position));

        typedArray.recycle();
    }

    @Override
    public int getItemCount() {
        return ThemeUtils.getThemeList().size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private Button colorButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorButton = itemView.findViewById(R.id.button_item_recyclerView_colorPicker);
            colorButton.setOnClickListener(v -> {
                listener.onClick(ThemeUtils.getThemeList().get(getAdapterPosition()));
            });
        }
    }

    public interface ColorPickerAdapterListener {
        void onClick(int themeID);
    }
}