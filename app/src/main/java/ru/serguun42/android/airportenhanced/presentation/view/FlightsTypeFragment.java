package ru.serguun42.android.airportenhanced.presentation.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import ru.serguun42.android.airportenhanced.presentation.view.adapters.FlightCardAdapter;
import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.databinding.FlightsTypeFragmentBinding;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.FlightsTypeViewModel;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.FlightsTypeViewModelFactory;

public class FlightsTypeFragment extends Fragment {
    public static final String SECTION_TYPE_EXTRA_KEY = "section_type_extra_key";
    private boolean isIncoming = false;

    private FlightsTypeFragmentBinding binding;
    private FlightsTypeViewModel viewModel;
    private FlightCardAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FlightsTypeFragmentBinding.inflate(inflater, container, false);

        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FlightCardAdapter((MainActivity) getActivity());
        binding.recyclerview.setAdapter(adapter);

        binding.loadMoreButton.setOnClickListener(v -> viewModel.loadMoreFlights());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        isIncoming = getArguments().getBoolean(SECTION_TYPE_EXTRA_KEY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this, new FlightsTypeViewModelFactory(isIncoming)).get(FlightsTypeViewModel.class);
        viewModel.getFlights().observe(getViewLifecycleOwner(), flights -> {
            if (flights.size() > 0) {
                binding.progressBar.setVisibility(View.GONE);
                binding.loadMoreButton.setVisibility(View.VISIBLE);
                binding.recyclerview.setVisibility(View.VISIBLE);
                adapter.updateFlightList(flights);
            }
        });
        viewModel.getCanLoadMoreFlights().observe(getViewLifecycleOwner(), canLoadMoreFlights -> {
            Log.d(MainActivity.MAIN_LOG_TAG, "canLoadMoreFlights = " + canLoadMoreFlights);
            binding.loadMoreButton.setVisibility(canLoadMoreFlights ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
        viewModel = null;
    }
}