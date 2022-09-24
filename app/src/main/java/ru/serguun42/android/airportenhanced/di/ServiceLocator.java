package ru.serguun42.android.airportenhanced.di;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ru.serguun42.android.airportenhanced.presentation.repository.RepositoryInterface;
import ru.serguun42.android.airportenhanced.presentation.repository.mock.MockDataSource;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIDataSource;

public class ServiceLocator {
    private static ServiceLocator instance = null;

    private ConnectivityManager connectivityManager;
    private final RepositoryInterface apiDataSource = new APIDataSource();
    private final RepositoryInterface mockDataSource = new MockDataSource();

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }

    public void init(Application app) {
        connectivityManager = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public RepositoryInterface getRepository() {
        if (isNetworkAvailable()) return apiDataSource;
        return mockDataSource;
    }

    private boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
