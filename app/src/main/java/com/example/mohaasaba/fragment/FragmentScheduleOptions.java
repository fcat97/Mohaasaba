package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mohaasaba.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentScheduleOptions extends BottomSheetDialogFragment {
    private Button copySchedule, deleteSchedule;
    private FragmentScheduleOptionListeners listeners;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule_options, null);

        copySchedule = root.findViewById(R.id.copyButton_FragmentScheduleOption);
        deleteSchedule = root.findViewById(R.id.deleteButton_FragmentScheduleOption);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        copySchedule.setOnClickListener(v -> {
            if (listeners != null) {
                listeners.onCopyButtonClicked();
                Toast.makeText(getContext(), "Schedule Copied", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        deleteSchedule.setOnClickListener(v -> {
            if (listeners != null) {
                listeners.onDeleteButtonClicked();
                Toast.makeText(getContext(), "Schedule Deleted", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public void setListeners(FragmentScheduleOptionListeners listeners) {
        this.listeners = listeners;
    }

    public interface FragmentScheduleOptionListeners {
        void onCopyButtonClicked();
        void onDeleteButtonClicked();
    }
}
