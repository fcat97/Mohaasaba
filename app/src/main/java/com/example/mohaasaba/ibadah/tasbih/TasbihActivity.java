package com.example.mohaasaba.ibadah.tasbih;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.mohaasaba.R;

import java.util.List;

public class TasbihActivity extends AppCompatActivity {
    private FragmentTasbih fragmentTasbih;

    private LiveData<List<Tasbih>> prayerLiveData;
    private LiveData<List<Tasbih>> sleepLiveData;
    private LiveData<List<Tasbih>> morningLiveData;
    private LiveData<List<Tasbih>> eveningLiveData;
    private LiveData<List<Tasbih>> mustahabLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbih);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // https://medium.com/@imstudio/android-change-status-bar-text-color-659680fce49b
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.colorWhite));
        }

        TasbihRepository repository = new TasbihRepository(this);

        prayerLiveData = repository.getTasbihPrayer();
        sleepLiveData = repository.getTasbihSleep();
        morningLiveData = repository.getTasbihMorning();
        eveningLiveData = repository.getTasbihEvening();
        mustahabLiveData = repository.getTasbihMustahab();

        fragmentTasbih = new FragmentTasbih()
                .setListener(this::openFragmentTasbihDetail)
                .setPrayerLiveData(prayerLiveData)
                .setMorningLiveData(morningLiveData)
                .setEveningLiveData(eveningLiveData)
                .setSleepLiveData(sleepLiveData)
                .setMustahabLiveData(mustahabLiveData)
                .setLongClickListener(repository::updateTasbih);
        showFragmentTasbih();
    }

    private void showFragmentTasbih() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout_TasbihActivity,fragmentTasbih)
                .commit();
    }

    private void openFragmentTasbihDetail(Tasbih tasbih) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_TasbihActivity, FragmentTasbihDetail.newInstance(tasbih))
                .addToBackStack("Fragment Tasbih Detail")
                .commit();
    }
}