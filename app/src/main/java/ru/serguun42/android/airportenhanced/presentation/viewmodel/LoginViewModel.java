package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.domain.model.LoginFormState;
import ru.serguun42.android.airportenhanced.domain.model.LoginResult;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void loginWithAPI(String username, String password) {
        APIMethods.signIn(username, password).observeForever(loginResultFromAPI -> {
            if (loginResultFromAPI.getSuccess() != null)
                Log.d(MainActivity.MAIN_LOG_TAG, "loginResultFromAPI: " + loginResultFromAPI.getSuccess());
            else
                Log.d(MainActivity.MAIN_LOG_TAG, "Error in loginResultFromAPI: " + loginResultFromAPI.getError());

            loginResult.setValue(loginResultFromAPI);
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username))
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        else if (!isPasswordValid(password))
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        else loginFormState.setValue(new LoginFormState(true));
    }

    private boolean isUserNameValid(String username) {
        return username != null && !username.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password != null && !password.trim().isEmpty();
    }
}