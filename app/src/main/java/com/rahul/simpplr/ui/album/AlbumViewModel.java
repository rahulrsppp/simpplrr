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
    private List<CompleteInfoModel> completeInfoList = new ArrayList<>();
    private MutableLiveData<List<CompleteInfoModel>> completeInfoLiveData = new MutableLiveData<>();
    private boolean isLastAlbum=false;

    public MutableLiveData<AlbumResponseModel> getAlbumObservable() {
        return albumObservable;
    }

    public MutableLiveData<AlbumTracksResponseModel> getAlbumTrackObservable() {
        return albumTrackObservable;
    }

    public MutableLiveData<List<CompleteInfoModel>> getCompleteDataObservable() {
        return completeInfoLiveData;
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

                            fetchAndPrepareAlbumWithTracks(albumResponseModel);

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
                        albumObservable.setValue(null);

                    }else{
                        getToastObservable().setValue(throwable.getMessage());

                    }
                }));
    }

    private void fetchAndPrepareAlbumWithTracks(AlbumResponseModel albumResponseModel) {
        List<AlbumResponseModel.AlbumData> albumList =  albumResponseModel.getItems();
        if(albumList.size()>0) {
            for (int i = 0; i < albumList.size(); i++) {
                CompleteInfoModel completeInfoModel = new CompleteInfoModel();
                completeInfoModel.setAlbumId(albumList.get(i).getId());
                completeInfoModel.setAlbumName(albumList.get(i).getName());

                if (albumList.get(i).getImages() != null && albumList.get(i).getImages().get(0) != null)
                    completeInfoModel.setAlbumImage(albumList.get(i).getImages().get(0).getImageUrl());

                fetchAlbumTrackInfo(albumList.get(i).getId(), true, completeInfoModel);

                if(i==albumList.size()-1){
                    isLastAlbum = true;
                }
            }


        }
    }

    public void fetchAlbumTrackInfo(String playlistId, boolean forCompleteData, CompleteInfoModel completeInfoModel) {
        setIsLoading(true);

        getCompositeDisposable().add(apiInterface.getPlaylistTracks(getAuthToken(preferencesHelper), playlistId,String.valueOf(50),String.valueOf(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null) {
                        JSONObject jsonObject=new JSONObject(response);

                        if(!isTokenExpired(jsonObject)) {
                            AlbumTracksResponseModel albumTracksResponseModel =  new Gson().fromJson(response,AlbumTracksResponseModel.class);

                            if(forCompleteData){
                                prepareCompleteInfoList(albumTracksResponseModel,completeInfoModel);
                            }else{
                                albumTrackObservable.setValue(albumTracksResponseModel);
                            }
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
                        albumTrackObservable.setValue(null);

                    }else{
                        getToastObservable().setValue(throwable.getMessage());

                    }
                }));
    }

    private void prepareCompleteInfoList(AlbumTracksResponseModel albumTracksResponseModel, CompleteInfoModel completeInfoModel) {
        if(albumTracksResponseModel.getItems()!=null){
            List<AlbumTracksResponseModel.AlbumTracksData> albumTracksList = albumTracksResponseModel.getItems();
            List<CompleteInfoModel.CompleteInfoData> tracksList =   new ArrayList<>();

            if(albumTracksList.size()>0){
                for (AlbumTracksResponseModel.AlbumTracksData albumTrackData : albumTracksList) {
                    CompleteInfoModel.CompleteInfoData trackData = new CompleteInfoModel.CompleteInfoData();
                    trackData.setTrackName(albumTrackData.getTrack().getName());
                    trackData.setTrackId(albumTrackData.getTrack().getTrackId());
                    tracksList.add(trackData);
                }
                completeInfoModel.setTracks(tracksList);
            }
        }
        completeInfoList.add(completeInfoModel);

        if(isLastAlbum){
            completeInfoLiveData.setValue(completeInfoList);
        }
    }


}
