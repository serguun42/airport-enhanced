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
import ru.serguun42.android.airportenhanced.di.ServiceLocator;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.RepositoryActions;
import ru.serguun42.android.airportenhanced.presentation.repository.room.RoomRepository;

public class APIRepository implements RepositoryActions {
    private AirportAPI api;

    public APIRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AirportAPI.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(AirportAPI.class);
    }

    public LiveData<List<Flight>> listFlights(int skip) {
        MutableLiveData<List<Flight>> flightsList = new MutableLiveData<>();

        api.listFlights(skip).enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {
                if (!response.isSuccessful())
                    flightsList.setValue(Collections.emptyList());
                else {
                    List<Flight> flightsFromAPI = response.body();
                    flightsList.setValue(flightsFromAPI);
                    ((RoomRepository) ServiceLocator.getInstance().getRoom()).cacheFlights(flightsFromAPI);
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                flightsList.setValue(Collections.emptyList());
            }
        });

        return flightsList;
    }

    public LiveData<Flight> getFlight(String flightId) {
        MutableLiveData<Flight> flight = new MutableLiveData<>();

        api.getFlight(flightId).enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> call, Response<Flight> response) {
                if (!response.isSuccessful())
                    flight.setValue(new Flight());
                else {
                    Flight flightFromAPI = response.body();
                    flight.setValue(flightFromAPI);
                    ((RoomRepository) ServiceLocator.getInstance().getRoom()).cacheFlight(flightFromAPI);
                }
            }

            @Override
            public void onFailure(Call<Flight> call, Throwable t) {
                flight.setValue(new Flight());
            }
        });

        return flight;
    }

    public LiveData<Flight> createFlight(String token, Flight changedFlight) {
        MutableLiveData<Flight> createdFlight = new MutableLiveData<>();

        api.createFlight(token, changedFlight).enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> call, Response<Flight> response) {
                if (!response.isSuccessful())
                    createdFlight.setValue(new Flight());
                else {
                    Flight flightFromAPI = response.body();
                    createdFlight.setValue(flightFromAPI);
                    ((RoomRepository) ServiceLocator.getInstance().getRoom()).cacheFlight(flightFromAPI);
                }
            }

            @Override
            public void onFailure(Call<Flight> call, Throwable t) {
                createdFlight.setValue(new Flight());
            }
        });

        return createdFlight;
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

    public LiveData<Flight> editFlight(String token, String flightId, Flight changedFlight) {
        FlightEditRequest flightEditRequest = new FlightEditRequest(flightId, changedFlight);

        MutableLiveData<Flight> editedFlight = new MutableLiveData<>();

        api.editFlight(token, flightEditRequest).enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> call, Response<Flight> response) {
                if (!response.isSuccessful())
                    editedFlight.setValue(new Flight());
                else
                    editedFlight.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Flight> call, Throwable t) {
                editedFlight.setValue(new Flight());
            }
        });

        return editedFlight;
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

    public LiveData<FlightDeleteResponse> deleteFlight(String token, String flightId) {
        FlightDeleteRequest flightDeleteRequest = new FlightDeleteRequest(flightId);

        MutableLiveData<FlightDeleteResponse> flightChangeResponse = new MutableLiveData<>();

        api.deleteFlight(token, flightDeleteRequest).enqueue(new Callback<FlightDeleteResponse>() {
            @Override
            public void onResponse(Call<FlightDeleteResponse> call, Response<FlightDeleteResponse> response) {
                if (!response.isSuccessful())
                    flightChangeResponse.setValue(new FlightDeleteResponse());
                else {
                    flightChangeResponse.setValue(response.body());
                    ((RoomRepository) ServiceLocator.getInstance().getRoom()).deleteFlightById(flightId);
                }
            }

            @Override
            public void onFailure(Call<FlightDeleteResponse> call, Throwable t) {
                flightChangeResponse.setValue(new FlightDeleteResponse());
            }
        });

        return flightChangeResponse;
    }

    public static class LoginRequestPayload {
        String username;
        String password;

        public LoginRequestPayload(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public LiveData<Session> signIn(String username, String password) {
        MutableLiveData<Session> sessionLiveData = new MutableLiveData<>();

        api.signIn(new LoginRequestPayload(username, password)).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.code() == 404) {
                    api.signUp(new LoginRequestPayload(username, password)).enqueue(new Callback<Session>() {
                        @Override
                        public void onResponse(Call<Session> callRegister, Response<Session> response) {
                            if (!response.isSuccessful()) {
                                sessionLiveData.setValue(new Session());
                                return;
                            }

                            Session session = response.body();
                            Log.d(MainActivity.MAIN_LOG_TAG, "New: " + session);
                            sessionLiveData.setValue(session);
                            ((RoomRepository) ServiceLocator.getInstance().getRoom()).cacheSession(session);
                        }

                        @Override
                        public void onFailure(Call<Session> callRegister, Throwable t) {
                            sessionLiveData.setValue(new Session());
                        }
                    });
                    return;
                }

                if (!response.isSuccessful()) {
                    sessionLiveData.setValue(new Session());
                    return;
                }

                Session session = response.body();
                Log.d(MainActivity.MAIN_LOG_TAG, "New: " + session);
                sessionLiveData.setValue(session);
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                sessionLiveData.setValue(new Session());
            }
        });

        return sessionLiveData;
    }

    public LiveData<Session> checkSession(String token) {
        MutableLiveData<Session> checkSuccessful = new MutableLiveData<>();
        if (token == null || token.isEmpty()) {
            checkSuccessful.setValue(new Session());
            return checkSuccessful;
        }

        api.checkAccount(token).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (!response.isSuccessful())
                    checkSuccessful.setValue(new Session());
                else {
                    Session sessionFromAPI = response.body();
                    checkSuccessful.setValue(sessionFromAPI);
                    ((RoomRepository) ServiceLocator.getInstance().getRoom()).cacheSession(sessionFromAPI);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                checkSuccessful.setValue(new Session());
            }
        });

        return checkSuccessful;
    }

    public LiveData<Session> signOut(String token) {
        MutableLiveData<Session> signOutSuccess = new MutableLiveData<>();
        if (token == null || token.isEmpty()) {
            signOutSuccess.setValue(new Session());
            return signOutSuccess;
        }

        api.signOut(token).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (!response.isSuccessful())
                    signOutSuccess.setValue(new Session());
                else {
                    Session sessionFromAPI = response.body();
                    signOutSuccess.setValue(sessionFromAPI);
                    ((RoomRepository) ServiceLocator.getInstance().getRoom()).cacheSession(sessionFromAPI);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                signOutSuccess.setValue(new Session());
            }
        });

        return signOutSuccess;
    }
}
