package com.rahul.simpplr.ui.login;

import com.google.gson.annotations.SerializedName;

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
