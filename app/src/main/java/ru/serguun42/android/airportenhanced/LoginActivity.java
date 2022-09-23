package ru.serguun42.android.airportenhanced;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ru.serguun42.android.airportenhanced.databinding.ActivityLoginBinding;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private SharedPreferences sharedPref;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getApplication().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) return;

            loginButton.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getUsernameError() != null)
                usernameEditText.setError(getString(loginFormState.getUsernameError()));

            if (loginFormState.getPasswordError() != null)
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) return;

            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null)
                showLoginFailed(loginResult.getError());

            if (loginResult.getSuccess() != null) {
                String token = loginResult.getSuccess().getToken();

                Log.d(MainActivity.MAIN_LOG_TAG, "Editing: set token to " + token);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.credentials_token_key), token);
                editor.apply();

                updateUiWithUser(loginResult.getSuccess());
            }

            setResult(Activity.RESULT_OK);

            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                loginViewModel.loginWithAPI(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );

            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.loginWithAPI(
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString()
            );
        });
    }

    private void updateUiWithUser(Session model) {
        String welcome = getString(R.string.welcome) + model.getUsername();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }
}