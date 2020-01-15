package com.rahul.simpplr.ui.album;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TracksResponseData implements Parcelable{

        @SerializedName("album")
        private AlbumTracksResponseModel.AlbumTracksData album ;


        @SerializedName("artists")
        private List<AlbumTracksResponseModel.AlbumTracksData> artists ;
        @SerializedName("name")
        private String name;


        @SerializedName("duration_ms")
        private String duration;

        @SerializedName("href")
        private String href ;


        @SerializedName("id")
        private String trackId ;

        @SerializedName("popularity")
        private String popularity ;

        @SerializedName("images")
        private List<AlbumTracksResponseModel.AlbumTracksData> images ;
        @SerializedName("url")
        private String imageUrl ;


    private TracksResponseData(Parcel in) {
        album = in.readParcelable(AlbumTracksResponseModel.AlbumTracksData.class.getClassLoader());
        artists = in.createTypedArrayList(AlbumTracksResponseModel.AlbumTracksData.CREATOR);
        name = in.readString();
        duration = in.readString();
        href = in.readString();
        trackId = in.readString();
        popularity = in.readString();
        images = in.createTypedArrayList(AlbumTracksResponseModel.AlbumTracksData.CREATOR);
        imageUrl = in.readString();
    }

    public static final Creator<TracksResponseData> CREATOR = new Creator<TracksResponseData>() {
        @Override
        public TracksResponseData createFromParcel(Parcel in) {
            return new TracksResponseData(in);
        }

        @Override
        public TracksResponseData[] newArray(int size) {
            return new TracksResponseData[size];
        }
    };

    public AlbumTracksResponseModel.AlbumTracksData getAlbum() {
        return album;
    }

    public List<AlbumTracksResponseModel.AlbumTracksData> getArtists() {
        return artists;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getHref() {
        return href;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getPopularity() {
        return popularity;
    }

    public List<AlbumTracksResponseModel.AlbumTracksData> getImages() {
        return images;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(album, flags);
        dest.writeTypedList(artists);
        dest.writeString(name);
        dest.writeString(duration);
        dest.writeString(href);
        dest.writeString(trackId);
        dest.writeString(popularity);
        dest.writeTypedList(images);
        dest.writeString(imageUrl);
    }
}
