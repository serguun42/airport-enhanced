package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;

public class FlightsTypeViewModel extends ViewModel {
    private List<Flight> allFlightList = Collections.emptyList();
    private final MutableLiveData<Boolean> canLoadMoreFlights = new MutableLiveData<>(true);
    private final MutableLiveData<List<Flight>> filteredFlightsList = new MutableLiveData<>(Collections.emptyList());
    private final boolean isIncoming;

    public FlightsTypeViewModel(boolean isIncoming) {
        this.isIncoming = isIncoming;
        loadMoreFlights();
    }

    public LiveData<List<Flight>> getFlights() {
        return filteredFlightsList;
    }

    public MutableLiveData<Boolean> getCanLoadMoreFlights() {
        return canLoadMoreFlights;
    }

    public void loadMoreFlights() {
        if (Boolean.FALSE.equals(canLoadMoreFlights.getValue())) return;

        APIMethods.listFlights(allFlightList.size()).observeForever(flightsFromAPI -> {
            if (flightsFromAPI.size() == 0)
                canLoadMoreFlights.setValue(false);
            else {
                if (flightsFromAPI.size() < 10) canLoadMoreFlights.setValue(false);

                allFlightList = Stream.concat(allFlightList.stream(), flightsFromAPI.stream()).collect(Collectors.toList());

                filteredFlightsList.setValue(
                        allFlightList.stream()
                                .filter(flight -> flight.isIncoming() == isIncoming)
                                .collect(Collectors.toList())
                );
            }
        });
    }
}