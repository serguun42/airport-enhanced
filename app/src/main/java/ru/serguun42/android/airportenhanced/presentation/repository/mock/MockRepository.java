package ru.serguun42.android.airportenhanced.presentation.repository.mock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.RepositoryActions;

public class MockRepository implements RepositoryActions {
    private final List<Flight> flights;
    private Session session;

    public MockRepository() {
        flights = new ArrayList<>();
        session = new Session();

        flights.add(new Flight(
                UUID.randomUUID().toString(),
                true,
                "Example airport in non-existent city",
                "MOC",
                "G01",
                "F0001",
                "Yak 152",
                "2022-12-10T17:20:00.000Z",
                "2022-12-10T20:45:00.000Z"
        ));

        flights.add(new Flight(
                UUID.randomUUID().toString(),
                true,
                "Example airport in non-existent city",
                "MOC",
                "G01",
                "F0001",
                "Yak 152",
                "2022-12-10T17:20:00.000Z",
                "2022-12-10T20:45:00.000Z"
        ));

        flights.add(new Flight(
                UUID.randomUUID().toString(),
                false,
                "Example airport in non-existent city",
                "MOC",
                "G01",
                "F0001",
                "Yak 152",
                "2022-12-10T17:20:00.000Z",
                "2022-12-10T20:45:00.000Z"
        ));
    }

    public LiveData<List<Flight>> listFlights(int skip) {
        return new MutableLiveData<>(flights);
    }


    public LiveData<Flight> getFlight(String flightId) {
        Optional<Flight> result = flights.stream().filter(flight -> Objects.equals(flight.getId(), flightId)).findAny();

        return new MutableLiveData<>(result.orElseGet(Flight::new));
    }


    public LiveData<Flight> createFlight(String token, Flight changedFlight) {
        if (changedFlight.getId() == null || changedFlight.getId().isEmpty())
            changedFlight.setId(flights.get(0).getId());

        flights.add(changedFlight);

        return new MutableLiveData<>(changedFlight);
    }


    public LiveData<Flight> editFlight(String token, String flightId, Flight changedFlight) {
        if (changedFlight.getId() == null || changedFlight.getId().isEmpty())
            changedFlight.setId(flights.get(0).getId());

        return new MutableLiveData<>(changedFlight);
    }


    public LiveData<FlightDeleteResponse> deleteFlight(String token, String flightId) {
        flights.removeIf(flight -> flight.getId() == flightId);
        return new MutableLiveData<>(new FlightDeleteResponse(true));
    }


    public LiveData<Session> signIn(String username, String password) {
        session = new Session("ff114271afe597eb0f09f3455397b7b18846a3b6d0c95cd73eebe29c01109999", true, username, true);
        return new MutableLiveData<>(session);
    }


    public LiveData<Session> checkSession(String token) {
        return new MutableLiveData<>(session);
    }


    public LiveData<Session> signOut(String token) {
        session = new Session();
        return new MutableLiveData<>(session);
    }
}
