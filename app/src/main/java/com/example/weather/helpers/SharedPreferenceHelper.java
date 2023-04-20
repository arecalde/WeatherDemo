package com.example.weather.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * a class to get/set a key in shared preferences
 */
public class SharedPreferenceHelper {

    private static final String CITIES = "CITIES";
    private static final String CITY_KEY = "city";

    /**
     * @param context Used to get shared preferences
     * @param newCity the new city that is being set in shared preferences
     */
    public static void setCity(Context context, String newCity) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(CITY_KEY, newCity);
        editor.apply();
    }

    /**
     * @param context Used to get shared preferences
     * @return a string with name of the last city searched
     */
    public static String getCity(Context context) {
        return getSharedPreferences(context).getString(CITY_KEY, "");
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(CITIES, Context.MODE_PRIVATE);
    }
}
