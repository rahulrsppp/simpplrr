package com.rahul.simpplr.ui.album;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlbumResponseModel {

    @SerializedName("limit")
    private int limit;

    @SerializedName("offset")
    private int offset;

    @SerializedName("total")
    private int total;

    @SerializedName("items")
    private List<AlbumData> items;

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotal() {
        return total;
    }

    public List<AlbumData> getItems() {
        return items;
    }

    public static class AlbumData implements Parcelable {

        @SerializedName("id")
        private String id ;

        @SerializedName("name")
        private String name ;

        @SerializedName("description")
        private String description ;

        @SerializedName("images")
        private List<AlbumData> images ;

        @SerializedName("url")
        private String imageUrl ;

        @SerializedName("tracks")
        private AlbumData tracks ;

        @SerializedName("total")
        private int total ;

        @SerializedName("type")
        private String type ;

        @SerializedName("uri")
        private String uri ;

        private int isSelected;


        AlbumData(Parcel in) {
            id = in.readString();
            name = in.readString();
            description = in.readString();
            images = in.createTypedArrayList(AlbumData.CREATOR);
            imageUrl = in.readString();
            tracks = in.readParcelable(AlbumData.class.getClassLoader());
            total = in.readInt();
            type = in.readString();
            uri = in.readString();
            isSelected = in.readInt();
        }

        public static final Creator<AlbumData> CREATOR = new Creator<AlbumData>() {
            @Override
            public AlbumData createFromParcel(Parcel in) {
                return new AlbumData(in);
            }

            @Override
            public AlbumData[] newArray(int size) {
                return new AlbumData[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<AlbumData> getImages() {
            return images;
        }

        public void setImages(List<AlbumData> images) {
            this.images = images;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public AlbumData getTracks() {
            return tracks;
        }

        public void setTracks(AlbumData tracks) {
            this.tracks = tracks;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(int isSelected) {
            this.isSelected = isSelected;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(description);
            dest.writeTypedList(images);
            dest.writeString(imageUrl);
            dest.writeParcelable(tracks, flags);
            dest.writeInt(total);
            dest.writeString(type);
            dest.writeString(uri);
            dest.writeInt(isSelected);
        }
    }


}
