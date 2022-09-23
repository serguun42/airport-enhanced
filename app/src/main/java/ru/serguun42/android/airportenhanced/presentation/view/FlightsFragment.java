package ru.serguun42.android.airportenhanced.presentation.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ru.serguun42.android.airportenhanced.EditorActivity;
import ru.serguun42.android.airportenhanced.MainActivity;
import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.databinding.FlightsFragmentBinding;
import ru.serguun42.android.airportenhanced.presentation.repository.network.APIMethods;
import ru.serguun42.android.airportenhanced.presentation.view.adapters.FlightsTypesSectionsAdapter;

public class FlightsFragment extends Fragment {
    FlightsTypesSectionsAdapter sectionsAdapter;

    private SharedPreferences sharedPref;
    private FlightsFragmentBinding binding;

    public static FlightsFragment newInstance() {
        return new FlightsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences_name_key), Context.MODE_PRIVATE);
        binding = FlightsFragmentBinding.inflate(getLayoutInflater(), container, false);

        checkAccountAndMakeCreateButton();

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

    private void checkAccountAndMakeCreateButton() {
        boolean canEdit = sharedPref.getBoolean(getString(R.string.credentials_can_edit_key), false);
        String token = sharedPref.getString(getString(R.string.credentials_token_key), null);
        Log.d(MainActivity.MAIN_LOG_TAG, "Reading: get token " + token);

        switchCreateButton(canEdit);
        APIMethods.checkEditPermission(token).observe(getViewLifecycleOwner(), this::switchCreateButton);
    }

    private void switchCreateButton(boolean showButton) {
        View root = binding.getRoot();

        if (showButton) {
            binding.createNew.setVisibility(View.VISIBLE);
            binding.createNew.setOnClickListener(view ->
                    root.getContext().startActivity(new Intent(root.getContext(), EditorActivity.class))
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