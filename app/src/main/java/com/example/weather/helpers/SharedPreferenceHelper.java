package com.example.weather.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    private static final String CITIES = "CITIES";
    private static final String CITY_KEY = "city";

    public static void setCity(Context context, String newCity) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CITY_KEY, newCity);
        editor.apply();
    }

    public static String getCity(Context context) {
        return getSharedPreferences(context).getString(CITY_KEY, "");
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(CITIES, Context.MODE_PRIVATE);
    }
}
