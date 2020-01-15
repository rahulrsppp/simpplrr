package com.rahul.simpplr.utility;

import java.util.concurrent.TimeUnit;

public class Util {


    public static String getCompleteTrackDuration(String durationInString) {
        long milliseconds = Long.parseLong(durationInString);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = (milliseconds / 1000) % 60;
        return  minutes +":"+seconds;
    }
}
