package ru.serguun42.android.airportenhanced.presentation.view.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ru.serguun42.android.airportenhanced.R;
import ru.serguun42.android.airportenhanced.presentation.view.FlightsListFragment;

public class FlightsSectionsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.incoming_flights,
            R.string.departing_flights
    };
    private final Context mContext;

    public FlightsSectionsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return FlightsListFragment.newInstance(position == 0);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}