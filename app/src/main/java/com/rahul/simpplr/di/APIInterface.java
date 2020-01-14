package com.rahul.simpplr.di;

import com.rahul.simpplr.ui.login.ProfileResponseModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("users/{user_id}/playlists") @Scalar
    Single<String>  getUserPlaylist(@Header("Authorization") String header,@Path("user_id") String user_id,  @Query("limit") String limit, @Query("offset") String offset);

    @GET("playlists/{playlist_id}/tracks") @Scalar
    Single<String>  getPlaylistTracks(@Header("Authorization") String header, @Path("playlist_id") String playlist_id, @Query("limit") String limit, @Query("offset") String offset );

    @GET("me") @Scalar
    Single<String>  getUserProfile(@Header("Authorization") String header);
}
