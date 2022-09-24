package ru.serguun42.android.airportenhanced.presentation.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.LoginFragmentBinding;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.LoginViewModel;

public class LoginFragment extends Fragment {
    private LoginViewModel viewModel;
    private SharedPreferences sharedPref;
    private LoginFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
        binding = LoginFragmentBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
            if (loginFormState == null) return;

            loginButton.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getUsernameError() != null)
                usernameEditText.setError(getString(loginFormState.getUsernameError()));

            if (loginFormState.getPasswordError() != null)
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
        });

        viewModel.getSession().observe(getViewLifecycleOwner(), session -> {
            if (session == null) return;

            loadingProgressBar.setVisibility(View.GONE);

            if (session.isSuccess()) {
                String token = session.getToken();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.credentials_token_key), token);
                editor.apply();

                updateUiWithUser(session);
            } else {
                showLoginFailed(R.string.login_failed);
            }

            Navigation.findNavController(binding.getRoot()).popBackStack();
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
                viewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE)
                viewModel.loginWithAPI(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );

            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            viewModel.loginWithAPI(
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString()
            );
        });
    }


    private void updateUiWithUser(Session model) {
        String welcome = getString(R.string.welcome) + model.getUsername();
        Toast.makeText(getContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_LONG).show();
    }
}