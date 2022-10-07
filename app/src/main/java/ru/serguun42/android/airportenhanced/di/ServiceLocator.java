package ru.serguun42.android.airportenhanced.di;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.serguun42.android.airportenhanced.presentation.repository.RepositoryActions;
import ru.serguun42.android.airportenhanced.presentation.repository.mock.MockRepository;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIRepository;
import ru.serguun42.android.airportenhanced.presentation.repository.room.RoomRepository;

public class ServiceLocator {
    private static ServiceLocator instance = null;

    private ConnectivityManager connectivityManager;
    private final RepositoryActions api = new APIRepository();
    private final RepositoryActions mock = new MockRepository();
    private RepositoryActions room;

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }

    public void init(Application app) {
        connectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        room = new RoomRepository(app);
    }

    public RepositoryActions getRepository() {
        if (isNetworkAvailable()) return api;
        if (room != null) return room;
        return mock;
    }

    public RepositoryActions getApi() {
        return api;
    }

    public RepositoryActions getMock() {
        return mock;
    }

    public RepositoryActions getRoom() {
        return room;
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
