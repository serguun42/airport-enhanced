package ru.serguun42.android.airportenhanced;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import ru.serguun42.android.airportenhanced.databinding.ActivityMainBinding;
import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.presentation.view.FlightDetailsFragment;

public class MainActivity extends AppCompatActivity {
    public static final String MAIN_LOG_TAG = "mainLogTag";

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ServiceLocator.getInstance().init(getApplication());

        Uri income = getIntent().getData();
        if (income != null) {
            String flightIdParam = income.getQueryParameter("id");
            if (income.getPath().startsWith("/flight") && flightIdParam != null && !flightIdParam.isEmpty()) {
                Log.d(MainActivity.MAIN_LOG_TAG, "flightIdParam = " + flightIdParam);

                Bundle bundle = new Bundle();
                bundle.putString(FlightDetailsFragment.FLIGHT_ID_EXTRA_KEY, flightIdParam);
                bundle.putBoolean(FlightDetailsFragment.IS_SHARED_EXTRA_KEY, true);
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                if (navHostFragment != null) {
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.action_flightsList_to_flightDetails, bundle);
                } else {
                    Log.d(MainActivity.MAIN_LOG_TAG, "navHostFragment is null");
                }
            }
        }
    }
}