package ru.serguun42.android.airportenhanced;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serguun42.android.airportenhanced.adapters.FlightSingleAdapter;
import ru.serguun42.android.airportenhanced.api.Flight;
import ru.serguun42.android.airportenhanced.api.FlightDeletePayload;
import ru.serguun42.android.airportenhanced.api.JSONPlaceholder;
import ru.serguun42.android.airportenhanced.databinding.ActivityPickBinding;
import ru.serguun42.android.airportenhanced.storage.SQLiteDatabaseHandler;
import ru.serguun42.android.airportenhanced.ui.login.LoginActivity;

public class PickActivity extends AppCompatActivity {
    public static final String FLIGHT_ID_EXTRA_TYPE = "flight_id_extra_type";

    private SQLiteDatabaseHandler db;
    private ActivityPickBinding binding;
    private RecyclerView recyclerView;
    private String flightId;
    private Flight flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new SQLiteDatabaseHandler(this);
        binding = ActivityPickBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            flightId = extras.getString(FLIGHT_ID_EXTRA_TYPE);

        recyclerView = binding.recyclerview;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSingleFlight(binding.getRoot());

        if (binding.editFlight != null)
            binding.editFlight.setOnClickListener(view -> gotoEdit(binding.getRoot()));

        if (binding.deleteFlight != null)
            binding.deleteFlight.setOnClickListener(view -> deleteSingleFlight(binding.getRoot()));
    }

    private void getSingleFlight(@NonNull View root) {
        if (flightId == null) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<Flight> call = jsonPlaceholder.getFlight(flightId);
        call.enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> call, Response<Flight> response) {
                if (!response.isSuccessful()) {
                    Snackbar.make(binding.getRoot(),
                            getString(R.string.cannot_get_flights), Snackbar.LENGTH_LONG).show();
                    gotoMain(root);
                    return;
                }

                flight = response.body();
                FlightSingleAdapter flightListAdapter = new FlightSingleAdapter(root.getContext(), flight);
                recyclerView.setAdapter(flightListAdapter);
            }

            @Override
            public void onFailure(Call<Flight> call, Throwable t) {
                Snackbar.make(binding.getRoot(),
                        getString(R.string.cannot_get_flights), Snackbar.LENGTH_LONG).show();
                gotoMain(root);
            }
        });
    }

    private void gotoLogin(@NonNull View root) {
        root.getContext().startActivity(new Intent(root.getContext(), LoginActivity.class));
    }

    private void gotoEdit(@NonNull View root) {
        Intent intent = new Intent(root.getContext(), Editor.class);
        intent.putExtra(Editor.FLIGHT_ID_EXTRA_PARAM, flightId);
        root.getContext().startActivity(intent);
    }

    private void gotoMain(@NonNull View root) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            root.getContext().startActivity(new Intent(root.getContext(), MainActivity.class));
        }, 1500);
    }

    private void deleteSingleFlight(@NonNull View root) {
        if (flightId == null) return;
        String token = db.getCredential().getToken();
        if (token == null) {
            gotoLogin(root);
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<Flight> call = jsonPlaceholder.deleteFlight(token, new FlightDeletePayload(flightId));
        call.enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> call, Response<Flight> response) {
                if (response.code() == 403) {
                    Snackbar.make(binding.getRoot(),
                            getString(R.string.insufficient_rights), Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (response.code() == 401) {
                    gotoLogin(root);
                    return;
                }

                if (!response.isSuccessful()) {
                    Snackbar.make(binding.getRoot(),
                            getString(R.string.cannot_perform_action), Snackbar.LENGTH_LONG).show();
                    return;
                }

                Snackbar.make(binding.getRoot(),
                        getString(R.string.success), Snackbar.LENGTH_LONG).show();
                gotoMain(root);
            }

            @Override
            public void onFailure(Call<Flight> call, Throwable t) {
                Snackbar.make(binding.getRoot(),
                        getString(R.string.cannot_perform_action), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}