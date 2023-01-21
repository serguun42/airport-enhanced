package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.domain.model.UserRecord;
import ru.serguun42.android.airportenhanced.domain.model.Session;

public class AdminListViewModel extends ViewModel {
    private List<UserRecord> usersList = Collections.emptyList();
    private final MutableLiveData<List<UserRecord>> usersListLiveData = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<Boolean> canLoadMoreUsers = new MutableLiveData<>(true);

    public LiveData<Session> checkSession(String token) {
        return ServiceLocator.getInstance().getRepository().checkSession(token);
    }

    public LiveData<List<UserRecord>> getUsers() {
        return usersListLiveData;
    }

    public MutableLiveData<Boolean> getCanLoadMoreUsers() {
        return canLoadMoreUsers;
    }

    public void loadUsers(String token) {
        if (Boolean.FALSE.equals(canLoadMoreUsers.getValue())) return;

        ServiceLocator.getInstance().getRepository().listUsers(token, usersList.size()).observeForever(usersFromDataSource -> {
            if (usersFromDataSource.size() == 0) canLoadMoreUsers.setValue(false);
            else {
                canLoadMoreUsers.setValue(true);
                usersList = Stream.concat(usersList.stream(), usersFromDataSource.stream()).collect(Collectors.toList());
                usersListLiveData.setValue(usersList);
            }
        });
    }
}