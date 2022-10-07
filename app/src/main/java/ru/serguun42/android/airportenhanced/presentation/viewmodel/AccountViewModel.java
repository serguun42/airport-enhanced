package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.room.RoomRepository;

public class AccountViewModel extends ViewModel {
    public LiveData<Session> checkSession(String token) {
        return ServiceLocator.getInstance().getRepository().checkSession(token);
    }

    public LiveData<Session> signOut(String token) {
        return ServiceLocator.getInstance().getRepository().signOut(token);
    }

    public void clearLocalDB() {
        ((RoomRepository) ServiceLocator.getInstance().getRoom()).deleteAllFlights();
        ((RoomRepository) ServiceLocator.getInstance().getRoom()).deleteSessions();
    }
}