package com.example.mohaasaba.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentTaskEditor extends BottomSheetDialogFragment {
    private EditText titleEditText;
    private EditText maxProgressEditText;
    private EditText stepEditText;
    private EditText progressUnitEditText;
    private TextView typeEditText;
    private TextView currentProgressTextView;
    private ImageButton confirmButton;
    private Task task, mTemp;
    private ImageButton decrementProgressImageButton;
    private FragmentTargetEditorListener listener;

    public FragmentTaskEditor(Task task) {
        this.task = task;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_edit_task, null);

        titleEditText = view.findViewById(R.id.title_EditText_FragmentTaskEditor);
        maxProgressEditText = view.findViewById(R.id.maxProgress_EditText_FragmentTaskEditor);
        stepEditText = view.findViewById(R.id.progressStep_EditText_FragmentTaskEditor);
        progressUnitEditText = view.findViewById(R.id.progressUnit_EditText_FragmentTaskEditor);
        typeEditText = view.findViewById(R.id.taskType_EditText_FragmentTaskEditor);
        currentProgressTextView = view.findViewById(R.id.currentProgress_TextView_FragmentTaskEditor);
        confirmButton = view.findViewById(R.id.confirm_button_FragmentTaskEditor);
        decrementProgressImageButton = view.findViewById(R.id.decrementProgress_ImageButton_FragmentTaskEditor);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleEditText.setText(task.text);
        currentProgressTextView.setText(String.valueOf(task.currentProgress));
        maxProgressEditText.setText(String.valueOf(task.maxProgress));
        stepEditText.setText(String.valueOf(task.step));
        if (! task.unit.trim().isEmpty()) progressUnitEditText.setText(task.unit);

        typeEditText.setText(task.taskType.toString());


        // Demo Task to perform undo operation and not to commit unless onConfirm()
        mTemp = new Task(task.text);
        mTemp.maxProgress = task.maxProgress;
        mTemp.currentProgress = task.currentProgress;
        mTemp.unit = task.unit;
        mTemp.step = task.step;
        mTemp.taskType = task.taskType;

        decrementProgressImageButton.setOnClickListener(v -> {
            mTemp.undo();
            currentProgressTextView.setText(String.valueOf(mTemp.currentProgress));
        });

        confirmButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String unit = progressUnitEditText.getText().toString().trim();
            float maxProgress = Float.parseFloat(maxProgressEditText.getText().toString());
            float step = Float.parseFloat(stepEditText.getText().toString());

            if (! title.isEmpty() && maxProgress  > 0 && step > 0) {
                task.text = title;
                task.maxProgress = maxProgress;
                task.step = step;
                task.unit = unit;
                task.currentProgress = mTemp.currentProgress;
                task.taskType = mTemp.taskType;

                if (listener != null) {
                    listener.onConfirm();
                    dismiss();
                } else {
                    throw new ClassCastException("Must implement Methods");
                }

            }
        });

        typeEditText.setOnClickListener(v -> {
            showTypeMenu();
        });

    }

    private void showTypeMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), typeEditText);
        popupMenu.inflate(R.menu.type_menu_fragment_edit_task);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.type_fazr_FragmentTaskEditor) {
                    setType(Task.Type.Farz);
                    return true;
                } else if (id == R.id.type_wazib_FragmentTaskEditor) {
                    setType(Task.Type.Wazib);
                    return true;
                }
                else if (id == R.id.type_sunnah_FragmentTaskEditor) {
                    setType(Task.Type.Sunnah);
                    return true;
                }
                else if (id == R.id.type_mustahab_FragmentTaskEditor) {
                    setType(Task.Type.Mustahab);
                    return true;
                }
                else if (id == R.id.type_mubah_FragmentTaskEditor) {
                    setType(Task.Type.Mubah);
                    return true;
                }
                else if (id == R.id.type_maqruh_FragmentTaskEditor) {
                    setType(Task.Type.Maqruh);
                    return true;
                }
                else if (id == R.id.type_sagirah_FragmentTaskEditor) {
                    setType(Task.Type.SagiraGunah);
                    return true;
                }
                else if (id == R.id.type_kabirah_FragmentTaskEditor){
                    setType(Task.Type.KabiraGunah);
                    return true;
                }
                else return false;
            }
        });
    }

    private void setType(Task.Type type) {
        mTemp.taskType = type;
        typeEditText.setText(type.toString());
    }

    public void setListener (FragmentTargetEditorListener listener) { this.listener = listener; }
    public interface FragmentTargetEditorListener {
        void onConfirm();
    }

}
