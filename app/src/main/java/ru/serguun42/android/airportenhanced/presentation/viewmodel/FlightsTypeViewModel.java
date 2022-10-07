package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.domain.model.Flight;

public class FlightsTypeViewModel extends ViewModel {
    private List<Flight> allFlightList = Collections.emptyList();
    private final MutableLiveData<List<Flight>> filteredFlightsList = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<Boolean> canLoadFlights = new MutableLiveData<>(true);
    private final boolean isIncoming;

    public FlightsTypeViewModel(boolean isIncoming) {
        this.isIncoming = isIncoming;
    }

    public LiveData<List<Flight>> getFlights() {
        return filteredFlightsList;
    }

    public void resetFlights() {
        allFlightList = Collections.emptyList();
        canLoadFlights.setValue(true);
    }

    public MutableLiveData<Boolean> getCanLoadFlights() {
        return canLoadFlights;
    }

    public void loadFlights() {
        if (Boolean.FALSE.equals(canLoadFlights.getValue())) return;

        ServiceLocator.getInstance().getRepository().listFlights(allFlightList.size()).observeForever(flightsFromDataSource -> {
            if (flightsFromDataSource.size() == 0)
                canLoadFlights.setValue(false);
            else {
                canLoadFlights.setValue(true);
                allFlightList = Stream.concat(allFlightList.stream(), flightsFromDataSource.stream()).collect(Collectors.toList());
                filteredFlightsList.setValue(
                        allFlightList.stream()
                                .filter(flight -> flight.isIncoming() == isIncoming)
                                .collect(Collectors.toList())
                );
            }
        });
    }
}
