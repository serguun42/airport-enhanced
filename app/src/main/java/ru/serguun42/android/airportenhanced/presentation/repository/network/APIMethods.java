package ru.serguun42.android.airportenhanced.presentation.repository.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.LoginResult;
import ru.serguun42.android.airportenhanced.domain.model.Session;

public class APIMethods {
    private static AirportAPI api;

    private static AirportAPI getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AirportAPI.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(AirportAPI.class);
        }
        return api;
    }

    public static LiveData<List<Flight>> listFlights(int skip) {
        MutableLiveData<List<Flight>> flightsList = new MutableLiveData<>();

        getApi().listFlights(skip).enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(@NonNull Call<List<Flight>> call, Response<List<Flight>> response) {
                if (!response.isSuccessful())
                    flightsList.setValue(Collections.emptyList());
                else
                    flightsList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                flightsList.setValue(Collections.emptyList());
            }
        });

        return flightsList;
    }

    public static LiveData<Flight> getFlight(String flightId) {
        MutableLiveData<Flight> flight = new MutableLiveData<>();

        getApi().getFlight(flightId).enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> call, Response<Flight> response) {
                if (!response.isSuccessful())
                    flight.setValue(new Flight());
                else
                    flight.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Flight> call, Throwable t) {
                flight.setValue(new Flight());
            }
        });

        return flight;
    }

    public static class FlightDeleteRequest {
        final String id;

        public FlightDeleteRequest(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class FlightEditRequest {
        final String id;
        final Flight updated;

        public FlightEditRequest(String id, Flight updated) {
            this.id = id;
            this.updated = updated;
        }

        public String getId() {
            return id;
        }
    }

    public static class LoginRequestPayload {
        String username;
        String password;

        public LoginRequestPayload(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static LiveData<LoginResult> signIn(String username, String password) {
        MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

        getApi().signIn(new LoginRequestPayload(username, password)).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.code() == 404) {
                    getApi().signUp(new LoginRequestPayload(username, password)).enqueue(new Callback<Session>() {
                        @Override
                        public void onResponse(Call<Session> callRegister, Response<Session> response) {
                            if (!response.isSuccessful()) {
                                loginResult.setValue(new LoginResult(R.string.login_failed));
                                return;
                            }

                            Session session = response.body();
                            Log.d(MainActivity.MAIN_LOG_TAG, "New " + session);
                            loginResult.setValue(new LoginResult(session));
                        }

                        @Override
                        public void onFailure(Call<Session> callRegister, Throwable t) {
                            loginResult.setValue(new LoginResult(R.string.login_failed));
                        }
                    });
                    return;
                }

                if (!response.isSuccessful()) {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                    return;
                }

                Session session = response.body();
                Log.d(MainActivity.MAIN_LOG_TAG, "New " + session);
                loginResult.setValue(new LoginResult(session));
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });

        return loginResult;
    }

    public static LiveData<Boolean> checkSession(String token) {
        MutableLiveData<Boolean> checkSuccessful = new MutableLiveData<>();
        if (token == null || token.isEmpty()) {
            checkSuccessful.setValue(false);
            return checkSuccessful;
        }

        getApi().checkAccount(token).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                checkSuccessful.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                checkSuccessful.setValue(false);
            }
        });

        return checkSuccessful;
    }

    public static LiveData<Boolean> checkEditPermission(String token) {
        MutableLiveData<Boolean> checkSuccessful = new MutableLiveData<>();
        if (token == null || token.isEmpty()) {
            checkSuccessful.setValue(false);
            return checkSuccessful;
        }

        getApi().checkAccount(token).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (!response.isSuccessful()) {
                    checkSuccessful.setValue(false);
                    return;
                }

                checkSuccessful.setValue(response.body().canEdit());
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                checkSuccessful.setValue(false);
            }
        });

        return checkSuccessful;
    }
}
