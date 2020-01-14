package com.rahul.simpplr.ui.login;



import android.content.Context;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.rahul.simpplr.base.BaseViewModel;
import com.rahul.simpplr.db.AppPreferencesHelper;
import com.rahul.simpplr.db.PreferencesHelper;
import com.rahul.simpplr.di.APIInterface;
import com.rahul.simpplr.utility.ToastAndErrorMessage;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LoginViewModel extends BaseViewModel {
    private static final String CLIENT_ID = "72d38cf29b9c49eaa1feed277944fba6";
    private String URL = "http://eduinsight.edunexttechnologies.com/mobapps/management/schoolmailbox";

    private final ObservableBoolean isLoginButtonVisible = new ObservableBoolean(true);

    private MutableLiveData<Integer> outputObservable = new MutableLiveData<>();

    public MutableLiveData<Integer> getOutputObservable() {
        return outputObservable;
    }


    @Inject
    Context context;

    @Inject
    APIInterface apiInterface;

    @Inject
    PreferencesHelper preferencesHelper;

    @Inject
    public LoginViewModel() {
        super();
        init();
    }

    private void init() {
        setIsLoading(true);
        setIsLoginButtonVisible(true);
    }

    public AuthenticationRequest getAuthenticationRequest() {
        return new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, "https://simpplr/callback")
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email"})
                .setCampaign("your-campaign-token")
                .build();
    }

    public ObservableBoolean getIsLoginButtonVisible() {
        return isLoginButtonVisible;
    }
    public void setIsLoginButtonVisible(boolean isVisible) {
         isLoginButtonVisible.set(isVisible);
    }

    void fetchProfileInfo() {
        setIsLoading(true);
        setIsLoginButtonVisible(false);

        getCompositeDisposable().add(apiInterface.getUserProfile(getAuthToken(preferencesHelper))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        JSONObject jsonObject=new JSONObject(response);

                        if(!isTokenExpired(jsonObject)) {

                            ProfileResponseModel  profileResponse =  new Gson().fromJson(response,ProfileResponseModel.class);

                            String userId = profileResponse.getId();
                            String name = profileResponse.getName();
                            String email = profileResponse.getEmail();
                            // String images = profileResponse.getImages();

                            preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_USER_ID, userId != null && userId.length() > 0 ? userId : null);
                            preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_USER_NAME, name != null && name.length() > 0 ? userId : null);
                            preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_USER_EMAIL, email != null && email.length() > 0 ? email : null);

                            outputObservable.setValue(1);
                        }else{
                            preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN,null);
                            getToastObservable().setValue(ToastAndErrorMessage.TOKEN_EXPIRED);
                        }

                    }
                    setIsLoading(false);
                    setIsLoginButtonVisible(true);
                }, throwable -> {
                    setIsLoading(false);
                    setIsLoginButtonVisible(true);
                    getToastObservable().setValue(throwable.getMessage());
                }));
    }



}
