package com.rahul.simpplr.base;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rahul.simpplr.db.AppPreferencesHelper;
import com.rahul.simpplr.db.PreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.CompositeDisposable;

public class BaseViewModel extends ViewModel {

    private final ObservableBoolean mIsLoading = new ObservableBoolean();
    private final CompositeDisposable mCompositeDisposable;
    private MutableLiveData<String> toastObservable = new MutableLiveData<>();




    public BaseViewModel() {
        this.mCompositeDisposable = new CompositeDisposable();
    }

    protected String getAuthToken(PreferencesHelper preferencesHelper) {
        String token = preferencesHelper.getStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN);
        if(token !=null) {
            System.out.println("Token: "+token);

            return "Bearer " + preferencesHelper.getStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN);
        }
        return null;
    }

    public MutableLiveData<String> getToastObservable() {
        return toastObservable;
    }

    protected CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    protected  boolean isTokenExist(JSONObject jsonObject){
        if(jsonObject.has("error")){
            try {
                JSONObject errorJson = jsonObject.getJSONObject("error");
                if(errorJson.getInt("status") == 401){
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if(!mCompositeDisposable.isDisposed()){
            mCompositeDisposable.dispose();
        }
    }



    protected void setIsLoading(boolean isLoading) {
        mIsLoading.set(isLoading);
    }

}
