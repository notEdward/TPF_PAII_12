package com.example.tpf_paii_android.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TutorViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public TutorViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TutorViewModel.class)) {
            return (T) new TutorViewModel(context);
        }
        throw new IllegalArgumentException("Error");
    }
}
