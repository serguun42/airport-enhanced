package ru.serguun42.android.airportenhanced.presentation.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.FlightDetailsFragmentBinding;
import ru.serguun42.android.airportenhanced.presentation.view.adapters.FlightCardAdapter;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.FlightDetailsViewModel;

public class FlightDetailsFragment extends Fragment {
    public static final String FLIGHT_ID_EXTRA_TYPE = "flight_id_extra_type";
    private String flightId;

    private SharedPreferences sharedPref;
    private FlightDetailsFragmentBinding binding;
    private FlightDetailsViewModel viewModel;

    public static FlightDetailsFragment newInstance() {
        return new FlightDetailsFragment();
    }

    public static FlightDetailsFragment newInstance(String flightId) {
        FlightDetailsFragment fragment = new FlightDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FLIGHT_ID_EXTRA_TYPE, flightId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        flightId = getArguments().getString(FLIGHT_ID_EXTRA_TYPE);
        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FlightDetailsFragmentBinding.inflate(inflater, container, false);

        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        viewModel = new ViewModelProvider(this).get(FlightDetailsViewModel.class);

        viewModel.getFlight(flightId).observe(getViewLifecycleOwner(), flight -> {
            binding.recyclerview.setAdapter(new FlightCardAdapter((MainActivity) getActivity(), Arrays.asList(flight)));
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        viewModel = null;
    }
}