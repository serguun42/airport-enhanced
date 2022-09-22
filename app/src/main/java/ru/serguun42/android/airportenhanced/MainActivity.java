package ru.serguun42.android.airportenhanced;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import ru.serguun42.android.airportenhanced.presentation.view.adapters.FlightsSectionsAdapter;
import ru.serguun42.android.airportenhanced.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String API_URL = "https://airport.serguun42.ru/api/v1/";
    public static final String SHARED_PREFS_LOG_TAG = "sharedPrefsLogs";
    public static final String LIVE_DATA_LOG_TAG = "liveDataLogs";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FlightsSectionsAdapter flightsSectionsAdapter = new FlightsSectionsAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(flightsSectionsAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}