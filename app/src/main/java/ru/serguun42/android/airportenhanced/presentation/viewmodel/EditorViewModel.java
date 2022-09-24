package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;

public class EditorViewModel extends ViewModel {
    private LiveData<Flight> flight = new MutableLiveData<>();

    public LiveData<Flight> getFlight(String flightId) {
        if (flight.getValue() != null) return flight;

        flight = ServiceLocator.getInstance().getRepository().getFlight(flightId);
        return flight;
    }

    public LiveData<Session> checkSession(String token) {
        return ServiceLocator.getInstance().getRepository().checkSession(token);
    }

    public LiveData<Flight> createFlight(String token, Flight changedFlight) {
        return ServiceLocator.getInstance().getRepository().createFlight(token, changedFlight);
    }

    public LiveData<Flight> editFlight(String token, String flightId, Flight changedFlight) {
        return ServiceLocator.getInstance().getRepository().editFlight(token, flightId, changedFlight);
    }
}