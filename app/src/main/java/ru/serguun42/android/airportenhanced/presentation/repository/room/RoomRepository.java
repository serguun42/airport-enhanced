package ru.serguun42.android.airportenhanced.presentation.repository.room;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.UUID;

import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.RepositoryActions;
import ru.serguun42.android.airportenhanced.presentation.repository.room.DAO.FlightDAO;
import ru.serguun42.android.airportenhanced.presentation.repository.room.DAO.SessionDAO;

public class RoomRepository implements RepositoryActions {
    private final FlightDAO flightDAO;
    private final SessionDAO sessionDAO;

    public RoomRepository(Application application) {
        RoomDatabase db = RoomDatabase.getDatabase(application);
        flightDAO = db.flightDAO();
        sessionDAO = db.sessionDAO();
    }

    @Override
    public LiveData<List<Flight>> listFlights(int skip) {
        return flightDAO.listItems(skip);
    }

    public void cacheFlights(List<Flight> flights) {
        RoomDatabase.databaseWriteExecutor.execute(() -> flightDAO.addFlights(flights));
    }

    @Override
    public LiveData<Flight> getFlight(String flightId) {
        return flightDAO.getFlightById(flightId);
    }

    public void cacheFlight(Flight flight) {
        RoomDatabase.databaseWriteExecutor.execute(() -> flightDAO.addFlight(flight));
    }

    @Override
    public LiveData<Flight> createFlight(String token, Flight changedFlight) {
        RoomDatabase.databaseWriteExecutor.execute(() -> flightDAO.addFlight(changedFlight));
        return new MutableLiveData<>(changedFlight);
    }

    @Override
    public LiveData<Flight> editFlight(String token, String flightId, Flight changedFlight) {
        changedFlight.setId(flightId);
        RoomDatabase.databaseWriteExecutor.execute(() -> flightDAO.updateFlight(changedFlight));
        return new MutableLiveData<>(changedFlight);
    }

    @Override
    public LiveData<FlightDeleteResponse> deleteFlight(String token, String flightId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> flightDAO.deleteFlightById(flightId));
        return new MutableLiveData<>(new FlightDeleteResponse(true));
    }

    public void deleteFlightById(String flightId) {
        RoomDatabase.databaseWriteExecutor.execute(() -> flightDAO.deleteFlightById(flightId));
    }

    public void deleteAllFlights() {
        RoomDatabase.databaseWriteExecutor.execute(flightDAO::deleteAllFlights);
    }

    @Override
    public LiveData<Session> signIn(String username, String password) {
        Session sessionToSet = new Session(UUID.randomUUID().toString(), true, username, true);
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            sessionDAO.deletePrevious();
            sessionDAO.storeSession(sessionToSet);
        });
        return new MutableLiveData<>(sessionToSet);
    }

    @Override
    public LiveData<Session> checkSession(String token) {
        return sessionDAO.getSessionByToken(token);
    }

    @Override
    public LiveData<Session> signOut(String token) {
        Session sessionToSet = new Session(UUID.randomUUID().toString(), false, "", false);
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            sessionDAO.deletePrevious();
            sessionDAO.storeSession(sessionToSet);
        });
        return new MutableLiveData<>(sessionToSet);
    }

    public void cacheSession(Session session) {
        RoomDatabase.databaseWriteExecutor.execute(() -> {
            sessionDAO.deletePrevious();
            sessionDAO.storeSession(session);
        });
    }

    public void deleteSessions() {
        RoomDatabase.databaseWriteExecutor.execute(sessionDAO::deletePrevious);
    }
}
