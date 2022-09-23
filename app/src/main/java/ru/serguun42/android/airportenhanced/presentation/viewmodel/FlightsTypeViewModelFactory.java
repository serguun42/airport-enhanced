package ru.serguun42.android.airportenhanced.presentation.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FlightsTypeViewModelFactory implements ViewModelProvider.Factory {
    private boolean isIncoming;

    public FlightsTypeViewModelFactory(boolean isIncoming) {
        this.isIncoming = isIncoming;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FlightsTypeViewModel(isIncoming);
    }
}
