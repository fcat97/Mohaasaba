package com.example.mohaasaba.plans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mohaasaba.R;
import com.example.mohaasaba.database.PlanRepository;
import com.example.mohaasaba.ibadah.bookshelf.FragmentBookDetail;

import java.util.List;


public class PlanActivity extends AppCompatActivity {
    private static final String TAG = PlanActivity.class.getCanonicalName();
    private FragmentPlan fragmentPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorGray));
        }

        PlanRepository repository = new PlanRepository(this);
        LiveData<List<Plan>> planLiveData = repository.getAllPlans();

        fragmentPlan = new FragmentPlan()
                .setPlansLiveData(planLiveData)
                .setOnAddButtonClicked(this::openFragmentEditPlan)
                .setItemClickListener(this::openFragmentPlanDetail);

        openFragmentPlan();
    }

    private void openFragmentPlan() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout_PlanActivity, fragmentPlan)
                .commit();
    }

    public void openFragmentEditPlan(Plan plan) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_PlanActivity, FragmentEditPlan.newInstance(plan))
                .addToBackStack("Fragment Edit Plan")
                .commit();
    }

    public void openFragmentPlanDetail(Plan plan) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_PlanActivity, FragmentPlanDetail.getInstance(plan)
                        .setEditButtonListener(this::openFragmentEditPlan))
                .addToBackStack("Fragment Plan Detail")
                .commit();
    }
}