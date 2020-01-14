package com.rahul.simpplr.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.rahul.simpplr.di.PreferenceInfo;

import javax.inject.Inject;


public class AppPreferencesHelper implements PreferencesHelper {

    public static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    public static final String PREF_KEY_IS_LOGGED_IN = "PREF_KEY_IS_LOGGED_IN";
    public static final String PREF_KEY_USER_EMAIL = "PREF_KEY_USER_EMAIL";
    public static final String PREF_KEY_USER_ID = "PREF_KEY_USER_ID";
    public static final String PREF_KEY_USER_NAME = "PREF_KEY_USER_NAME";
    public static final String PREF_KEY_USER_IMAGE= "PREF_KEY_USER_IMAGE";


    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }


    @Override
    public String getStringData(String key) {
        return mPrefs.getString(key, null);
    }

    @Override
    public void setStringData(String key, String data) {
        mPrefs.edit().putString(key, data).apply();
    }

    @Override
    public boolean getBooleanData(String key) {
        return mPrefs.getBoolean(key, false);
    }

    @Override
    public void setBooleanData(String key, Boolean data) {
        mPrefs.edit().putBoolean(key, data).apply();
    }

    @Override
    public Integer getIntData(String key) {
        return mPrefs.getInt(key, 0);
    }

    @Override
    public void setIntData(String key, int data) {
        mPrefs.edit().putInt(key, data).apply();

    }
}
