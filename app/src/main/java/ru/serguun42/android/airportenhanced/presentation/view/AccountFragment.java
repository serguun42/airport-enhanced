package ru.serguun42.android.airportenhanced.presentation.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.AccountFragmentBinding;
import ru.serguun42.android.airportenhanced.domain.model.Session;
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
    public void onResume() {
        super.onResume();

        checkSession();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        checkSession();
    }

    private void checkSession() {
        String token = sharedPref.getString(getString(R.string.credentials_token_key), null);
        viewModel.checkSession(token).observe(getViewLifecycleOwner(), this::buildCardBasedOnSession);
    }

    private void buildCardBasedOnSession(Session session) {
        Log.d(MainActivity.MAIN_LOG_TAG, "buildCardBasedOnSession(Session session): " + session);

        if (session != null && session.isSuccess()) {
            binding.sessionInfoLayout.setVisibility(View.VISIBLE);
            binding.controlButtons.setVisibility(View.VISIBLE);
            binding.adminListButton.setVisibility(View.GONE);
            binding.adminListButton.setOnClickListener(null);
            binding.logoutButton.setVisibility(View.VISIBLE);
            binding.logoutButton.setOnClickListener(v ->
                    viewModel.signOut(sharedPref.getString(getString(R.string.credentials_token_key), null))
                            .observe(getViewLifecycleOwner(), emptySession -> {
                                Toast.makeText(getContext(), getString(R.string.logout_successful), Toast.LENGTH_LONG).show();
                                this.buildCardBasedOnSession(emptySession);
                            })
            );
            binding.loginButton.setVisibility(View.GONE);
            binding.loginButton.setOnClickListener(null);

            binding.sessionUsername.setText(session.getUsername());
            switch (session.getLevel()) {
                case 1:
                    binding.sessionLevel.setText(getString(R.string.moder_level));
                    break;
                case 2:
                    binding.sessionLevel.setText(getString(R.string.admin_level));
                    binding.adminListButton.setVisibility(View.VISIBLE);
                    binding.adminListButton.setOnClickListener(view -> {
                        Navigation.findNavController(view).navigate(R.id.action_flightsList_to_admin_list);
                    });
                    break;
                default:
                    binding.sessionLevel.setText(getString(R.string.regular_level));
                    break;
            }
        } else {
            binding.sessionInfoLayout.setVisibility(View.GONE);
            binding.controlButtons.setVisibility(View.GONE);
            binding.adminListButton.setVisibility(View.GONE);
            binding.adminListButton.setOnClickListener(null);
            binding.logoutButton.setVisibility(View.GONE);
            binding.logoutButton.setOnClickListener(null);
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.loginButton.setOnClickListener(view ->
                    Navigation.findNavController(view).navigate(R.id.action_flightsList_to_login)
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