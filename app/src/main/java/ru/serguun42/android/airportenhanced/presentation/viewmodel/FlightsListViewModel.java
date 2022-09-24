package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.domain.model.Session;

public class FlightsListViewModel extends ViewModel {
    public LiveData<Session> checkSession(String token) {
        return ServiceLocator.getInstance().getRepository().checkSession(token);
    }
}
