package com.example.weather.model;

import com.example.weather.model.json.WeatherResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WeatherApi {
    @GET("/data/2.5/weather")
    Call<WeatherResult> getWeather(@QueryMap Map<String, String> params);

    static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

