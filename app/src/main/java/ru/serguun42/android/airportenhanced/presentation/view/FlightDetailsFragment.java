package ru.serguun42.android.airportenhanced.presentation.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Arrays;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.FlightDetailsFragmentBinding;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.view.adapters.FlightDetailedCardAdapter;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.FlightDetailsViewModel;

public class FlightDetailsFragment extends Fragment {
    public static final String FLIGHT_ID_EXTRA_KEY = "flight_id_extra_key";
    public static final String IS_SHARED_EXTRA_KEY = "is_shared_extra_key";
    private String flightId;
    private boolean isShared;

    private SharedPreferences sharedPref;
    private FlightDetailsFragmentBinding binding;
    private FlightDetailsViewModel viewModel;

    public static FlightDetailsFragment newInstance() {
        return new FlightDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            flightId = getArguments().getString(FLIGHT_ID_EXTRA_KEY);
            isShared = getArguments().getBoolean(IS_SHARED_EXTRA_KEY);
        }

        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FlightDetailsFragmentBinding.inflate(inflater, container, false);

        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        ((MainActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.toolbar.setTitle(isShared ? R.string.flight_details_shared : R.string.flight_details);

        viewModel = new ViewModelProvider(this).get(FlightDetailsViewModel.class);
        viewModel.getFlight(flightId).observe(getViewLifecycleOwner(), flight -> {
            binding.recyclerview.setAdapter(new FlightDetailedCardAdapter((MainActivity) getActivity(), Arrays.asList(flight)));
        });

        checkAccountAndMakeCreateButton();

        return binding.getRoot();
    }

    private void checkAccountAndMakeCreateButton() {
        String token = sharedPref.getString(getString(R.string.credentials_token_key), null);
        Log.d(MainActivity.MAIN_LOG_TAG, "Reading: get token " + token);

        viewModel.checkSession(token).observe(getViewLifecycleOwner(), this::switchCreateButton);
    }

    private void switchCreateButton(Session session) {
        if (session != null && session.getLevel() > 0) {
            binding.controlButtons.setVisibility(View.VISIBLE);

            binding.editFlight.setOnClickListener(view -> {
                Flight flightFromViewModel = viewModel.getFlight(flightId).getValue();
                if (flightFromViewModel != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(EditorFragment.FLIGHT_ID_EXTRA_KEY, flightFromViewModel.getId());
                    Navigation.findNavController(view).navigate(R.id.action_flightDetails_to_editor, bundle);
                }
            });

            binding.deleteFlight.setOnClickListener(view -> {
                Flight flightFromViewModel = viewModel.getFlight(flightId).getValue();
                if (flightFromViewModel != null) {
                    viewModel.deleteFlight(
                            sharedPref.getString(getString(R.string.credentials_token_key), null),
                            flightFromViewModel.getId()
                    ).observe(getViewLifecycleOwner(), flightDeleteResponse -> {
                        Toast.makeText(
                                getContext(),
                                getString(R.string.removed_flight_number) + flightFromViewModel.getFlightNumber(),
                                Toast.LENGTH_LONG
                        ).show();
                        Navigation.findNavController(view).popBackStack();
                    });
                }
            });
        } else {
            binding.controlButtons.setVisibility(View.GONE);
            binding.editFlight.setOnClickListener(null);
            binding.deleteFlight.setOnClickListener(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        viewModel = null;
    }
}