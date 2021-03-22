package com.example.mohaasaba.plans;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.PlanRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentPlanOptions extends BottomSheetDialogFragment {
    private Button deleteButton;
    public static final String EXTRA_PLAN = "com.mohaasaba.EXTRA_PLAN";

    private Plan mPlan;

    public FragmentPlanOptions() {

    }

    public static FragmentPlanOptions getInstance(Plan plan) {
        FragmentPlanOptions fragmentPlanOptions = new FragmentPlanOptions();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PLAN, plan);
        fragmentPlanOptions.setArguments(args);
        return fragmentPlanOptions;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlan = getArguments().getParcelable(EXTRA_PLAN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_plan_options, container, false);

        deleteButton = rootView.findViewById(R.id.delete_Button_FragmentPlanOptions);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deleteButton.setOnClickListener(v -> {
            PlanRepository repository = new PlanRepository(getContext());
            repository.delete(mPlan);
            dismiss();
        });
    }
}
