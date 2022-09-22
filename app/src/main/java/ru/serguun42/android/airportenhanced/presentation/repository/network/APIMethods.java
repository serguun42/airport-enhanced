package ru.serguun42.android.airportenhanced.presentation.repository.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.domain.model.Flight;
import ru.serguun42.android.airportenhanced.domain.model.LoginResult;
import ru.serguun42.android.airportenhanced.domain.model.UserLocal;

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

    public static class LoginRequestPayload {
        String username;
        String password;

        public LoginRequestPayload(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class LoginResponse {
        private String token;

        public String getToken() {
            return token;
        }
    }

    public static LiveData<LoginResult> loginWithAPI(String username, String password) {
        MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

        getApi().signIn(new LoginRequestPayload(username, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 404) {
                    getApi().signUp(new LoginRequestPayload(username, password)).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> callRegister, Response<LoginResponse> response) {
                            if (!response.isSuccessful()) {
                                loginResult.setValue(new LoginResult(R.string.login_failed));
                                return;
                            }

                            String token = response.body().getToken();

                            loginResult.setValue(new LoginResult(new UserLocal(username, token)));
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> callRegister, Throwable t) {
                            loginResult.setValue(new LoginResult(R.string.login_failed));
                        }
                    });
                    return;
                }

                if (!response.isSuccessful()) {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                    return;
                }

                String token = response.body().getToken();

                loginResult.setValue(new LoginResult(new UserLocal(username, token)));
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });

        return loginResult;
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
}
