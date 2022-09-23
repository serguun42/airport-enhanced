package ru.serguun42.android.airportenhanced;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.serguun42.android.airportenhanced.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String API_URL = "https://airport.serguun42.ru/api/v1/";
    public static final String MAIN_LOG_TAG = "mainLogTag";

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}