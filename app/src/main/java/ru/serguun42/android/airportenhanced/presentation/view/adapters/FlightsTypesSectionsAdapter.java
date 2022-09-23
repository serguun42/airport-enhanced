package ru.serguun42.android.airportenhanced.presentation.view.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.presentation.view.FlightsTypeFragment;

public class FlightsTypesSectionsAdapter extends FragmentStateAdapter {
    @StringRes
    public static final int[] TAB_TITLES = new int[]{
            R.string.incoming_flights,
            R.string.departing_flights,
//            R.string.account_tab,
    };

    public FlightsTypesSectionsAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new FlightsTypeFragment();
        Bundle args = new Bundle();
        args.putBoolean(FlightsTypeFragment.SECTION_TYPE_EXTRA_KEY, position == 0);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}