package com.gmail.zubrapps.showmetheweather;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Michał on 2016-06-24.
 */
public class SavedCity {
    private SharedPreferences mPreferences;
    public SavedCity(Activity activity) {
        mPreferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return mPreferences.getString("city", "Warsaw");
    }

    void setCity(String city){
        mPreferences.edit().putString("city",city).apply();
    }
}
