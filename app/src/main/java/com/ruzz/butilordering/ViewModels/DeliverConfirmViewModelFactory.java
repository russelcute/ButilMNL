package com.ruzz.butilordering.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DeliverConfirmViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DeliverConfirmViewModel.class)) {
            return (T) new DeliverConfirmViewModel();
        }
        throw new IllegalArgumentException("Invalid view model class");
    }
}