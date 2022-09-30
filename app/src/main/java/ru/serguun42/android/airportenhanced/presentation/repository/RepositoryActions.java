package ru.serguun42.android.airportenhanced.presentation.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIRepository;

public interface RepositoryActions {

    class FlightDeleteResponse {
        final boolean success;

        public FlightDeleteResponse() {
            this.success = false;
        }

        public FlightDeleteResponse(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    LiveData<List<Flight>> listFlights(int skip);

    LiveData<Flight> getFlight(String flightId);

    LiveData<Flight> createFlight(String token, Flight changedFlight);

    LiveData<Flight> editFlight(String token, String flightId, Flight changedFlight);

    LiveData<APIRepository.FlightDeleteResponse> deleteFlight(String token, String flightId);

    LiveData<Session> signIn(String username, String password);

    LiveData<Session> checkSession(String token);

    LiveData<Session> signOut(String token);
}
