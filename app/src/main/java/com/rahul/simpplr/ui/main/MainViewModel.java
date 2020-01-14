package com.rahul.simpplr.ui.main;



import androidx.lifecycle.MutableLiveData;

import com.rahul.simpplr.base.BaseViewModel;

import javax.inject.Inject;


public class MainViewModel extends BaseViewModel {


    private MutableLiveData<String> outputObservable = new MutableLiveData<>();

    public MutableLiveData<String> getOutputObservable() {
        return outputObservable;
    }

    @Inject
    public MainViewModel(){

    }


}
