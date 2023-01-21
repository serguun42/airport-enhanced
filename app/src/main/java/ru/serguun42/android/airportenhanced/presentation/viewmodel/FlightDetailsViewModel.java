package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.RepositoryActions;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIRepository;

public class FlightDetailsViewModel extends ViewModel {
    private LiveData<Flight> flight = new MutableLiveData<>();

    public LiveData<Flight> getFlight(String flightId) {
        if (flight.getValue() != null) return flight;

        flight = ServiceLocator.getInstance().getRepository().getFlight(flightId);
        return flight;
    }

    public LiveData<RepositoryActions.FlightDeleteResponse> deleteFlight(String token, String flightId) {
        return ServiceLocator.getInstance().getRepository().deleteFlight(token, flightId);
    }

    public LiveData<Session> checkSession(String token) {
        return ServiceLocator.getInstance().getRepository().checkSession(token);
    }
}