package ru.serguun42.android.airportenhanced.presentation.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.serguun42.android.airportenhanced.EditorActivity;
import ru.serguun42.android.airportenhanced.LoginActivity;
import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.AccountFragmentBinding;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.AccountViewModel;

public class AccountFragment extends Fragment {
    private SharedPreferences sharedPref;
    private AccountFragmentBinding binding;
    private AccountViewModel viewModel;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
        binding = AccountFragmentBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        buildCardBasedOnSession();
    }

    private void buildCardBasedOnSession() {
        String token = sharedPref.getString(getString(R.string.credentials_token_key), null);
        Log.d(MainActivity.MAIN_LOG_TAG, "Reading: get token " + token);

        viewModel.checkSession(token).observe(getViewLifecycleOwner(), this::switchCreateButton);
    }

    private void switchCreateButton(Session session) {
        View root = binding.getRoot();

        if (session.isSuccess()) {
            binding.sessionInfoLayout.setVisibility(View.VISIBLE);
            binding.logoutButton.setOnClickListener(view ->
                    viewModel.signOut(sharedPref.getString(getString(R.string.credentials_token_key), null))
            );
            binding.loginButton.setVisibility(View.GONE);
            binding.loginButton.setOnClickListener(null);

            binding.sessionUsername.setText(session.getUsername());
            binding.sessionCanEdit.setText(getString(session.canEdit() ? R.string.yes : R.string.no));
        } else {
            binding.sessionInfoLayout.setVisibility(View.GONE);
            binding.logoutButton.setOnClickListener(null);
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.loginButton.setOnClickListener(view ->
                    root.getContext().startActivity(new Intent(root.getContext(), LoginActivity.class))
            );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
        viewModel = null;
    }
}