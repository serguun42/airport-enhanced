package ru.serguun42.android.airportenhanced;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.payload.FlightEditPayload;
import ru.serguun42.android.airportenhanced.presentation.repository.network.AirportAPI;
import ru.serguun42.android.airportenhanced.databinding.ActivityEditorBinding;
import ru.serguun42.android.airportenhanced.ui.login.LoginActivity;

public class Editor extends AppCompatActivity {
    public static final String FLIGHT_ID_EXTRA_PARAM = "flight_id_extra_param";

    private SharedPreferences sharedPref;
    private ActivityEditorBinding binding;
    private String flightId;
    private Flight flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getApplication().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
        binding = ActivityEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            flightId = extras.getString(FLIGHT_ID_EXTRA_PARAM);

        if (flightId != null)
            fillInputs(binding.getRoot());

        binding.saveFlight.setOnClickListener(view -> save(binding.getRoot()));
    }

    private void gotoLogin(@NonNull View root) {
        root.getContext().startActivity(new Intent(root.getContext(), LoginActivity.class));
    }

    private void gotoMain(@NonNull View root) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            root.getContext().startActivity(new Intent(root.getContext(), MainActivity.class));
        }, 1500);
    }

    private void save(@NonNull View root) {
        String token = sharedPref.getString(getString(R.string.credentials_token_key), null);
        Log.d(MainActivity.SHARED_PREFS_LOG_TAG, "Got token " + token);

        if (token == null) {
            gotoLogin(root);
            return;
        }

        if (flight == null)
            flight = new Flight();

        flight.setTargetName(((EditText) root.findViewById(R.id.target_name)).getText().toString());
        flight.setTargetIATA(((EditText) root.findViewById(R.id.target_iata)).getText().toString());
        flight.setGate(((EditText) root.findViewById(R.id.gate)).getText().toString());
        flight.setFlightNumber(((EditText) root.findViewById(R.id.flight_number)).getText().toString());
        flight.setPlaneModel(((EditText) root.findViewById(R.id.plane_model)).getText().toString());
        flight.setIncoming(((CheckBox) root.findViewById(R.id.is_incoming)).isChecked());
        flight.setDeparture(getDateFromDatePicker(root.findViewById(R.id.departure)).toString());
        flight.setArrival(getDateFromDatePicker(root.findViewById(R.id.arrival)).toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AirportAPI airportAPI = retrofit.create(AirportAPI.class);
        Call<Object> call;

        if (flightId != null && !flightId.isEmpty())
            call = airportAPI.editFlight(token, new FlightEditPayload(flightId, flight));
        else
            call = airportAPI.createFlight(token, flight);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
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
            public void onFailure(Call<Object> call, Throwable t) {
                Snackbar.make(binding.getRoot(),
                        getString(R.string.cannot_perform_action), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * @param datePicker
     * @return a java.util.Date
     */
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    private void fillInputs(@NonNull View root) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AirportAPI airportAPI = retrofit.create(AirportAPI.class);
        Call<Flight> call = airportAPI.getFlight(flightId);
        call.enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> call, Response<Flight> response) {
                if (!response.isSuccessful()) {
                    flight = new Flight();
                }

                flight = response.body();

                ((EditText) root.findViewById(R.id.target_name)).setText(flight.getTargetName());
                ((EditText) root.findViewById(R.id.target_iata)).setText(flight.getTargetIATA());
                ((EditText) root.findViewById(R.id.gate)).setText(flight.getGate());
                ((EditText) root.findViewById(R.id.flight_number)).setText(flight.getFlightNumber());
                ((EditText) root.findViewById(R.id.plane_model)).setText(flight.getPlaneModel());
                ((CheckBox) root.findViewById(R.id.is_incoming)).setChecked(flight.isIncoming());
            }

            @Override
            public void onFailure(Call<Flight> call, Throwable t) {
                flight = new Flight();
            }
        });
    }
}