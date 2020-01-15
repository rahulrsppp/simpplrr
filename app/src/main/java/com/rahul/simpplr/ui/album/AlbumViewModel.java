package com.rahul.simpplr.ui.album;



import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.rahul.simpplr.base.BaseViewModel;
import com.rahul.simpplr.db.AppPreferencesHelper;
import com.rahul.simpplr.db.PreferencesHelper;
import com.rahul.simpplr.di.APIInterface;
import com.rahul.simpplr.utility.ToastAndErrorMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AlbumViewModel extends BaseViewModel {

    @Inject
    Context context;

    @Inject
    APIInterface apiInterface;

    @Inject
    PreferencesHelper preferencesHelper;

    private MutableLiveData<AlbumResponseModel> albumObservable = new MutableLiveData<>();
    private MutableLiveData<AlbumTracksResponseModel> albumTrackObservable = new MutableLiveData<>();


    public MutableLiveData<AlbumResponseModel> getAlbumObservable() {
        return albumObservable;
    }

    public MutableLiveData<AlbumTracksResponseModel> getAlbumTrackObservable() {
        return albumTrackObservable;
    }

    @Inject
    AlbumViewModel(){ }


    void fetchAlbumInfo() {
        setIsLoading(true);

        String userId = preferencesHelper.getStringData(AppPreferencesHelper.PREF_KEY_USER_ID);

        getCompositeDisposable().add(apiInterface.getUserPlaylist(getAuthToken(preferencesHelper), userId,String.valueOf(1),String.valueOf(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        JSONObject jsonObject=new JSONObject(response);

                        if(!isTokenExpired(jsonObject)) {
                            AlbumResponseModel albumResponseModel =  new Gson().fromJson(response,AlbumResponseModel.class);
                            albumObservable.setValue(albumResponseModel);

                        }else {
                            preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN, null);
                            getToastObservable().setValue(ToastAndErrorMessage.TOKEN_EXPIRED);
                        }
                    }
                    setIsLoading(false);
                }, throwable -> {
                    setIsLoading(false);

                    if(Objects.requireNonNull(throwable.getMessage()).contains("401")){
                        getToastObservable().setValue(ToastAndErrorMessage.TOKEN_EXPIRED);
                        preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN,null);
                        preferencesHelper.setBooleanData(AppPreferencesHelper.PREF_KEY_IS_LOGGED_IN,false);
                    }else{
                        getToastObservable().setValue(ToastAndErrorMessage.PLEASE_CHECK_INTERNET);
                    }
                    albumObservable.setValue(null);

                }));
    }




    public void fetchAlbumTrackInfo(String playlistId) {
        setIsLoading(true);

        getCompositeDisposable().add(apiInterface.getPlaylistTracks(getAuthToken(preferencesHelper), playlistId,String.valueOf(50),String.valueOf(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        JSONObject jsonObject=new JSONObject(response);

                        if(!isTokenExpired(jsonObject)) {
                            AlbumTracksResponseModel albumTracksResponseModel =  new Gson().fromJson(response,AlbumTracksResponseModel.class);
                            albumTrackObservable.setValue(  modifyTrackResponse(albumTracksResponseModel, playlistId));
                        }else {
                            preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN, null);
                            preferencesHelper.setBooleanData(AppPreferencesHelper.PREF_KEY_IS_LOGGED_IN,false);
                            getToastObservable().setValue(ToastAndErrorMessage.TOKEN_EXPIRED);
                            albumTrackObservable.setValue(null);
                        }
                    }
                    setIsLoading(false);
                }, throwable -> {
                    setIsLoading(false);

                    if(Objects.requireNonNull(throwable.getMessage()).contains("401")){
                        getToastObservable().setValue(ToastAndErrorMessage.TOKEN_EXPIRED);
                        preferencesHelper.setStringData(AppPreferencesHelper.PREF_KEY_ACCESS_TOKEN,null);
                        preferencesHelper.setBooleanData(AppPreferencesHelper.PREF_KEY_IS_LOGGED_IN,false);
                    }else{
                        getToastObservable().setValue(ToastAndErrorMessage.PLEASE_CHECK_INTERNET);
                    }
                    albumTrackObservable.setValue(null);
                }));
    }

    private AlbumTracksResponseModel modifyTrackResponse(AlbumTracksResponseModel albumTracksResponseModel, String playlistId) {

        if (albumTracksResponseModel != null) {
            List<AlbumTracksResponseModel.AlbumTracksData> albumList = albumTracksResponseModel.getItems();
            for (AlbumTracksResponseModel.AlbumTracksData albumData : albumList) {
                albumData.setAlbumId(playlistId);
            }
        }
        return albumTracksResponseModel;
    }


}
