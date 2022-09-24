package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.domain.model.Session;

public class AccountViewModel extends ViewModel {
    public LiveData<Session> checkSession(String token) {
        return ServiceLocator.getInstance().getRepository().checkSession(token);
    }

    public LiveData<Session> signOut(String token) {
        return ServiceLocator.getInstance().getRepository().signOut(token);
    }
}