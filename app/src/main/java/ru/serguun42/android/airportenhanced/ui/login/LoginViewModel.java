package ru.serguun42.android.airportenhanced.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.api.SuccessWithToken;
import ru.serguun42.android.airportenhanced.api.JSONPlaceholder;
import ru.serguun42.android.airportenhanced.api.UserPayload;
import ru.serguun42.android.airportenhanced.data.LoginRepository;
import ru.serguun42.android.airportenhanced.data.Result;
import ru.serguun42.android.airportenhanced.data.model.LoggedInUser;
import ru.serguun42.android.airportenhanced.R;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<SuccessWithToken> call = jsonPlaceholder.signIn(new UserPayload(username, password));
        call.enqueue(new Callback<SuccessWithToken>() {
            @Override
            public void onResponse(Call<SuccessWithToken> call, Response<SuccessWithToken> response) {
                if (response.code() == 404) {
                    Call<SuccessWithToken> callRegister = jsonPlaceholder.signUp(new UserPayload(username, password));
                    callRegister.enqueue(new Callback<SuccessWithToken>() {
                        @Override
                        public void onResponse(Call<SuccessWithToken> callRegister, Response<SuccessWithToken> response) {
                            if (!response.isSuccessful()) {
                                loginResult.setValue(new LoginResult(R.string.login_failed));
                                return;
                            }

                            String token = response.body().getToken();

                            Result<LoggedInUser> result = loginRepository.login(username, password);
                            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), token)));
                        }

                        @Override
                        public void onFailure(Call<SuccessWithToken> callRegister, Throwable t) {
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

                Result<LoggedInUser> result = loginRepository.login(username, password);
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), token)));
            }

            @Override
            public void onFailure(Call<SuccessWithToken> call, Throwable t) {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}