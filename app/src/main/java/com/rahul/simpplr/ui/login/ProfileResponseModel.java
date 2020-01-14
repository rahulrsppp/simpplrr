package com.rahul.simpplr.ui.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponseModel {

    @SerializedName("id")
    private String id;

    @SerializedName("birthdate")
    private String birthdate;

    @SerializedName("country")
    private String country;

    @SerializedName("display_name")
    private String name;

    @SerializedName("email")
    private String email;


    public String getId() {
        return id;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


}
