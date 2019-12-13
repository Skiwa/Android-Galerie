package com.example.projet.ui.annotations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AnnotationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AnnotationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is annotations fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}