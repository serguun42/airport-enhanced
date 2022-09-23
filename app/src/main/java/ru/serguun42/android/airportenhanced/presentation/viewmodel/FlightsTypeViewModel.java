package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;

public class FlightsTypeViewModel extends ViewModel {
    private MutableLiveData<Boolean> canLoadMoreFlights = new MutableLiveData<>(true);
    private MutableLiveData<List<Flight>> allFlightList = new MutableLiveData<>(Arrays.asList());
    private MutableLiveData<List<Flight>> filteredFlightsList = new MutableLiveData<>(Arrays.asList());
    private boolean isIncoming = true;

    public FlightsTypeViewModel(boolean isIncoming) {
        this.isIncoming = isIncoming;
    }

    public LiveData<List<Flight>> getFlights() {
        if (canLoadMoreFlights.getValue() && allFlightList.getValue().size() == 0)
            loadMoreFlights();

        return filteredFlightsList;
    }

    public void loadMoreFlights() {
        if (!canLoadMoreFlights.getValue()) return;

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
