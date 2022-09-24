package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;

public class EditorViewModel extends ViewModel {
    private LiveData<Flight> flight = new MutableLiveData<>();

    public LiveData<Flight> getFlight(String flightId) {
        if (flight.getValue() != null) return flight;

        flight = APIMethods.getFlight(flightId);
        return flight;
    }

    public LiveData<Session> checkSession(String token) {
        return APIMethods.checkSession(token);
    }

    public LiveData<Flight> createFlight(String token, Flight flight) {
        return APIMethods.createFlight(token, flight);
    }

    public LiveData<Flight> editFlight(String token, String flightId, Flight flight) {
        APIMethods.FlightEditRequest flightEditRequest = new APIMethods.FlightEditRequest(flightId, flight);
        return APIMethods.editFlight(token, flightEditRequest);
    }
}