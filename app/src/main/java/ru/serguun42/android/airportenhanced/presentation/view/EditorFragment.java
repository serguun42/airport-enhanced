package ru.serguun42.android.airportenhanced.presentation.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.EditorFragmentBinding;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.EditorViewModel;

public class EditorFragment extends Fragment {
    public static final String FLIGHT_ID_EXTRA_KEY = "flight_id_extra_key";

    private SharedPreferences sharedPref;
    private EditorFragmentBinding binding;
    private EditorViewModel viewModel;

    private String flightId;
    private LocalDateTime departingLocalDateTime = null;
    private LocalDateTime arrivingLocalDateTime = null;

    public static EditorFragment newInstance() {
        return new EditorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            flightId = getArguments().getString(FLIGHT_ID_EXTRA_KEY);
        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = EditorFragmentBinding.inflate(inflater, container, false);

        ((MainActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        viewModel = new ViewModelProvider(this).get(EditorViewModel.class);
        if (flightId != null && !flightId.isEmpty()) {
            viewModel.getFlight(flightId).observe(getViewLifecycleOwner(), this::refillFields);
            binding.toolbar.setTitle(R.string.edit_flight);
        } else {
            binding.toolbar.setTitle(R.string.create_new_flight);
        }

        binding.saveButton.setOnClickListener(v -> {
            String token = sharedPref.getString(getString(R.string.credentials_token_key), null);

            Flight changedFlight = collectFlightByFields();
            LiveData<Flight> changeActionData = (flightId != null && !flightId.isEmpty()) ?
                    viewModel.editFlight(token, flightId, changedFlight) :
                    viewModel.createFlight(token, changedFlight);

            changeActionData.observe(getViewLifecycleOwner(), flightChangeResponse -> {
                if (flightChangeResponse != null && flightChangeResponse.getId() != null && !flightChangeResponse.getId().isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FlightDetailsFragment.FLIGHT_ID_EXTRA_KEY, flightChangeResponse.getId());
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_editor_to_flightDetails, bundle);
                } else
                    Toast.makeText(getContext(), getString(R.string.cannot_save_flight), Toast.LENGTH_LONG).show();
            });
        });

        binding.departure.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog.OnDateSetListener dateSetListener = (datePickerView, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = (timePickerView, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    LocalDateTime temp = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
                    if (arrivingLocalDateTime != null && temp.compareTo(arrivingLocalDateTime) > 0) {
                        departingLocalDateTime = arrivingLocalDateTime;
                        arrivingLocalDateTime = temp;
                    } else {
                        departingLocalDateTime = temp;
                    }

                    binding.departure.setText(departingLocalDateTime.format(DateTimeFormatter.ofPattern("EEE, MMM d HH:mm")));
                };

                new TimePickerDialog(getContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            };

            new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.arrival.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            new DatePickerDialog(getContext(), (datePickerView, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                new TimePickerDialog(getContext(), (timePickerView, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    LocalDateTime temp = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
                    if (departingLocalDateTime != null && temp.compareTo(departingLocalDateTime) < 0) {
                        arrivingLocalDateTime = departingLocalDateTime;
                        departingLocalDateTime = temp;
                    } else {
                        arrivingLocalDateTime = temp;
                    }

                    binding.arrival.setText(arrivingLocalDateTime.format(DateTimeFormatter.ofPattern("EEE, MMM d HH:mm")));
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        checkAccount();

        return binding.getRoot();
    }

    @SuppressLint("SimpleDateFormat")
    private void refillFields(Flight flight) {
        if (flight == null) return;

        binding.isIncoming.setChecked(flight.isIncoming());
        binding.targetName.setText(flight.getTargetName());
        binding.targetIata.setText(flight.getTargetIATA());
        binding.flightNumber.setText(flight.getFlightNumber());
        binding.planeModel.setText(flight.getPlaneModel());
        binding.gate.setText(flight.getGate());

        try {
            Instant instant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(flight.getDeparture()));
            Date departureDate = Date.from(instant);
            departingLocalDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            binding.departure.setText(new SimpleDateFormat("EEE, MMM d HH:mm").format(departureDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Instant instant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(flight.getArrival()));
            Date arrivalDate = Date.from(instant);
            arrivingLocalDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            binding.arrival.setText(new SimpleDateFormat("EEE, MMM d HH:mm").format(arrivalDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Flight collectFlightByFields() {
        Flight flight = new Flight();

        flight.setIncoming(binding.isIncoming.isChecked());
        flight.setTargetName(binding.targetName.getText().toString());
        flight.setTargetIATA(binding.targetIata.getText().toString());
        flight.setFlightNumber(binding.flightNumber.getText().toString());
        flight.setPlaneModel(binding.planeModel.getText().toString());
        flight.setGate(binding.gate.getText().toString());

        if (departingLocalDateTime != null)
            flight.setDeparture(DateTimeFormatter.ISO_INSTANT.format(departingLocalDateTime.toInstant(ZoneOffset.UTC)));
        if (arrivingLocalDateTime != null)
            flight.setArrival(DateTimeFormatter.ISO_INSTANT.format(arrivingLocalDateTime.toInstant(ZoneOffset.UTC)));

        return flight;
    }

    private void checkAccount() {
        String token = sharedPref.getString(getString(R.string.credentials_token_key), null);
        Log.d(MainActivity.MAIN_LOG_TAG, "Reading: get token " + token);

        viewModel.checkSession(token).observe(getViewLifecycleOwner(), session -> {
            if (session == null || !session.isSuccess()) {
                Toast.makeText(getContext(), getString(R.string.cannot_edit_flights), Toast.LENGTH_LONG).show();
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        viewModel = null;
    }
}