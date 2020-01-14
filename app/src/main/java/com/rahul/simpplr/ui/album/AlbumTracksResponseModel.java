package com.rahul.simpplr.ui.album;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlbumTracksResponseModel {

    @SerializedName("limit")
    private int limit;

    @SerializedName("offset")
    private int offset;

    @SerializedName("total")
    private int total;

    @SerializedName("items")
    private List<AlbumTracksData> items;


    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotal() {
        return total;
    }

    public List<AlbumTracksData> getItems() {
        return items;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setItems(List<AlbumTracksData> items) {
        this.items = items;
    }

    public static class AlbumTracksData implements Parcelable {

        @SerializedName("track")
        private TracksResponseData track ;


        @SerializedName("album")
        private AlbumTracksData album ;


        @SerializedName("artists")
        private List<AlbumTracksData> artists ;
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
        private List<AlbumTracksData> images ;
        @SerializedName("url")
        private String imageUrl ;


        protected AlbumTracksData(Parcel in) {
            track = in.readParcelable(TracksResponseData.class.getClassLoader());
            album = in.readParcelable(AlbumTracksData.class.getClassLoader());
            artists = in.createTypedArrayList(AlbumTracksData.CREATOR);
            name = in.readString();
            duration = in.readString();
            href = in.readString();
            trackId = in.readString();
            popularity = in.readString();
            images = in.createTypedArrayList(AlbumTracksData.CREATOR);
            imageUrl = in.readString();
        }

        public static final Creator<AlbumTracksData> CREATOR = new Creator<AlbumTracksData>() {
            @Override
            public AlbumTracksData createFromParcel(Parcel in) {
                return new AlbumTracksData(in);
            }

            @Override
            public AlbumTracksData[] newArray(int size) {
                return new AlbumTracksData[size];
            }
        };

        public AlbumTracksData getAlbum() {
            return album;
        }

        public void setAlbum(AlbumTracksData album) {
            this.album = album;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlbumName() {
            return name;
        }

        public void setAlbumName(String albumName) {
            this.name = albumName;
        }

        public List<AlbumTracksData> getArtists() {
            return artists;
        }

        public void setArtists(List<AlbumTracksData> artists) {
            this.artists = artists;
        }

        public String getArtistName() {
            return name;
        }

        public void setArtistName(String artistName) {
            this.name = artistName;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void setTrackName(String trackName) {
            this.name = trackName;
        }

        public void setTrackId(String trackId) {
            this.trackId = trackId;
        }

        public void setPopularity(String popularity) {
            this.popularity = popularity;
        }

        public List<AlbumTracksData> getImages() {
            return images;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }


        public String getImageUrl() {
            return imageUrl;
        }

        public String getTrackId() {
            return trackId;
        }

        public String getTrackName() {
            return name;
        }

        public String getPopularity() {
            return popularity;
        }

        public String getHref() {
            return href;
        }

        public String getDuration() {
            return duration;
        }

        public TracksResponseData getTrack() {
            return track;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(track, flags);
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


}
