package ru.serguun42.android.airportenhanced.presentation.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.AdminListFragmentBinding;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.view.adapters.UsersAdapter;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.AdminListViewModel;

public class AdminListFragment extends Fragment {
    private SharedPreferences sharedPref;
    private String token;
    private AdminListFragmentBinding binding;
    private AdminListViewModel viewModel;

    public static AdminListFragment newInstance() {
        return new AdminListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.credentials_token_key), null);
        Log.d(MainActivity.MAIN_LOG_TAG, "Reading: get token " + token);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.loadUsers(token);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = AdminListFragmentBinding.inflate(inflater, container, false);

        binding.loadMoreButton.setOnClickListener(v -> viewModel.loadUsers(token));

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        UsersAdapter adapter = new UsersAdapter((MainActivity) getActivity());
        binding.recyclerview.setAdapter(adapter);

        ((MainActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        viewModel = new ViewModelProvider(this).get(AdminListViewModel.class);

        viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            boolean isEmpty = users.size() == 0;

            binding.loadMoreButton.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            binding.recyclerview.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            adapter.updateUsersList(users);
        });

        viewModel.getCanLoadMoreUsers().observe(getViewLifecycleOwner(), canLoadMoreUsers -> {
            Log.d(MainActivity.MAIN_LOG_TAG, "canLoadMoreUsers = " + canLoadMoreUsers);
            binding.loadMoreButton.setVisibility(canLoadMoreUsers ? View.VISIBLE : View.GONE);
        });

        checkAccountAndMakeCreateButton();

        return binding.getRoot();
    }

    private void checkAccountAndMakeCreateButton() {
        viewModel.checkSession(token).observe(getViewLifecycleOwner(), this::switchCreateButton);
    }

    private void switchCreateButton(Session session) {
        if (session == null || session.getLevel() < 2) {
            Toast.makeText(getContext(), getString(R.string.cannot_view_this_list), Toast.LENGTH_LONG).show();
            Navigation.findNavController(binding.getRoot()).popBackStack();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        viewModel = null;
    }
}