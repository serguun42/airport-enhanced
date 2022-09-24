package ru.serguun42.android.airportenhanced.presentation.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.FlightsListFragmentBinding;
import ru.serguun42.android.airportenhanced.domain.model.Session;
import ru.serguun42.android.airportenhanced.presentation.view.adapters.FlightsTypesSectionsAdapter;
import ru.serguun42.android.airportenhanced.presentation.viewmodel.FlightsListViewModel;

public class FlightsListFragment extends Fragment {
    FlightsTypesSectionsAdapter sectionsAdapter;

    private SharedPreferences sharedPref;
    private FlightsListFragmentBinding binding;
    private FlightsListViewModel viewModel;

    public static FlightsListFragment newInstance() {
        return new FlightsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
        binding = FlightsListFragmentBinding.inflate(getLayoutInflater(), container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sectionsAdapter = new FlightsTypesSectionsAdapter(this);
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsAdapter);
        TabLayout tabLayout = binding.tabLayout;
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText(FlightsTypesSectionsAdapter.TAB_TITLES[position])
        ).attach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FlightsListViewModel.class);
        checkAccountAndMakeCreateButton();
    }

    private void checkAccountAndMakeCreateButton() {
        String token = sharedPref.getString(getString(R.string.credentials_token_key), null);
        Log.d(MainActivity.MAIN_LOG_TAG, "Reading: get token " + token);

        viewModel.checkSession(token).observe(getViewLifecycleOwner(), this::switchCreateButton);
    }

    private void switchCreateButton(Session session) {
        if (session.canEdit()) {
            binding.createNew.setVisibility(View.VISIBLE);
            binding.createNew.setOnClickListener(view ->
                    Navigation.findNavController(view).navigate(R.id.action_flightsList_to_editor)
            );
        } else {
            binding.createNew.setVisibility(View.GONE);
            binding.createNew.setOnClickListener(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding.viewPager.setAdapter(null);
        binding.tabLayout.setupWithViewPager(null);
        binding = null;
    }
}