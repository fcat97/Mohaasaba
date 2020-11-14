package com.example.mohaasaba.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mohaasaba.R;
import com.example.mohaasaba.adapter.ColorPickerAdapter;

public class DialogColorPicker extends DialogFragment {
    private static final String TAG = "ColorPickerDialog";
    private int selectedColor;
    private DialogColorPickerListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_color_picker, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle("Choose Color");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_FragmentColorPicker);
        recyclerView.setAdapter(new ColorPickerAdapter(themeID -> {
            if (listener != null) {
                listener.onConfirm(themeID);
            } else throw new ClassCastException("Listeners must be implemented!");
        }));

        return builder.create();

    }

    public void setListener(DialogColorPickerListener listener) {
        this.listener = listener;
    }

    public interface DialogColorPickerListener {
        void onConfirm(int themeID);
    }
}
