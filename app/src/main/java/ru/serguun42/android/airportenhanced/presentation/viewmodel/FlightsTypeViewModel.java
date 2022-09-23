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
    private final MutableLiveData<Boolean> canLoadMoreFlights = new MutableLiveData<>(true);
    private final MutableLiveData<List<Flight>> allFlightList = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<List<Flight>> filteredFlightsList = new MutableLiveData<>(Collections.emptyList());
    private final boolean isIncoming;

    public FlightsTypeViewModel(boolean isIncoming) {
        this.isIncoming = isIncoming;
        loadMoreFlights();
    }

    public LiveData<List<Flight>> getFlights() {
        return filteredFlightsList;
    }

    public void loadMoreFlights() {
        if (Boolean.FALSE.equals(canLoadMoreFlights.getValue())) return;

        APIMethods.listFlights(allFlightList.getValue().size()).observeForever(flightsFromAPI -> {
            if (flightsFromAPI.size() == 0)
                canLoadMoreFlights.setValue(false);
            else {
                if (flightsFromAPI.size() < 10) canLoadMoreFlights.setValue(false);
                filteredFlightsList.setValue(
                        Stream.concat(
                                filteredFlightsList.getValue().stream(),
                                flightsFromAPI.stream()
                        ).filter(flight -> flight.isIncoming() == isIncoming).collect(Collectors.toList())
                );
            }
        });
    }
}
