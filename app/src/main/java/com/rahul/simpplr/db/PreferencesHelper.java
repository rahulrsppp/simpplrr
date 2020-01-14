package com.rahul.simpplr.db;



public interface PreferencesHelper {

    String getStringData(String key);

    void setStringData(String key, String data);

    boolean getBooleanData(String key);

    void setBooleanData(String key, Boolean data);

    Integer getIntData(String key);

    void setIntData(String key,int data);
}
