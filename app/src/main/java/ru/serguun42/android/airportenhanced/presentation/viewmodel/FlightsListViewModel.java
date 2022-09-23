package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;

public class FlightsListViewModel extends ViewModel {
    public LiveData<Session> checkSession(String token) {
        return APIMethods.checkSession(token);
    }
}
