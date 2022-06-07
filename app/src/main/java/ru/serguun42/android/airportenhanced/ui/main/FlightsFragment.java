package ru.serguun42.android.airportenhanced.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serguun42.android.airportenhanced.Editor;
import ru.serguun42.android.airportenhanced.api.Flight;
import ru.serguun42.android.airportenhanced.adapters.FlightListAdapter;
import ru.serguun42.android.airportenhanced.api.JSONPlaceholder;
import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.FragmentFlightsBinding;

public class FlightsFragment extends Fragment {
    private static final String ARG_SECTION_TYPE = "is_incoming";
    private boolean isIncoming = false;

    private FragmentFlightsBinding binding;
    private RecyclerView recyclerView;
    private List<Flight> flightList;
    private int totalLoaded = 0;
    private boolean successfulLast = true;

    public static FlightsFragment newInstance(boolean isIncoming) {
        FlightsFragment fragment = new FlightsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_SECTION_TYPE, isIncoming);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        isIncoming = getArguments().getBoolean(ARG_SECTION_TYPE);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentFlightsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        loadMoreFlights(root);

        if (binding.loadMore != null)
            binding.loadMore.setOnClickListener(view -> loadMoreFlights(root));

        if (binding.createNew != null)
            binding.createNew.setOnClickListener(view -> createNew(root));

        return root;
    }

    private void createNew(@NonNull View root) {
        root.getContext().startActivity(new Intent(root.getContext(), Editor.class));
    }

    private void loadMoreFlights(@NonNull View root) {
        if (!successfulLast) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (flightList == null)
            flightList = new ArrayList<>();

        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<List<Flight>> call = jsonPlaceholder.listFlights(totalLoaded);
        call.enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {
                if (response.code() == 404) {
                    disableListUpdating();
                    return;
                }

                if (!response.isSuccessful()) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            getString(R.string.cannot_get_flights), Snackbar.LENGTH_LONG).show();
                    disableListUpdating();
                    return;
                }

                totalLoaded += response.body().size();

                flightList = Stream.concat(flightList.stream(), response.body().stream())
                        .filter(flight -> flight.isIncoming() == isIncoming)
                        .collect(Collectors.toList());
                FlightListAdapter flightListAdapter = new FlightListAdapter(root.getContext(), flightList);
                recyclerView.setAdapter(flightListAdapter);
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        getString(R.string.cannot_get_flights), Snackbar.LENGTH_LONG).show();

                disableListUpdating();
            }
        });
    }

    public void disableListUpdating() {
        successfulLast = false;
        binding.loadMore.hide();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}