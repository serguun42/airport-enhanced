package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;

public class FlightDetailsViewModel extends ViewModel {
    public LiveData<Flight> getFlight(String flightId) {
        return APIMethods.getFlight(flightId);
    }
}