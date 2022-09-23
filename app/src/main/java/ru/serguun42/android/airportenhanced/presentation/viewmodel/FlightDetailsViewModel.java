package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;

public class FlightDetailsViewModel extends ViewModel {
    private LiveData<Flight> flight = new MutableLiveData<>();

    public LiveData<Flight> getFlight(String flightId) {
        if (flight.getValue() != null) return flight;

        flight = APIMethods.getFlight(flightId);
        return flight;
    }

    public LiveData<APIMethods.FlightChangeResponse> deleteFlight(String token, APIMethods.FlightDeleteRequest body) {
        return APIMethods.deleteFlight(token, body);
    }

    public LiveData<Session> checkSession(String token) {
        return APIMethods.checkSession(token);
    }
}