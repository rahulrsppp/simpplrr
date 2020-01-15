package com.rahul.simpplr.ui.main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompleteInfoModel {

    private String albumName;
    private String albumImage;
    private String albumId;
    private List<CompleteInfoData> tracks;

    public List<CompleteInfoData> getTracks() {
        return tracks;
    }

    public void setTracks(List<CompleteInfoData> tracks) {
        this.tracks = tracks;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }


    public static class CompleteInfoData {

        private String trackName;
        private String trackId;

        public String getTrackName() {
            return trackName;
        }

        public void setTrackName(String trackName) {
            this.trackName = trackName;
        }

        public String getTrackId() {
            return trackId;
        }

        public void setTrackId(String trackId) {
            this.trackId = trackId;
        }
    }
}
