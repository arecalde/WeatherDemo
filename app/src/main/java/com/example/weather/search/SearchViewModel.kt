package com.example.weather.search

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.weather.model.WeatherApi
import com.example.weather.model.json.WeatherResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    val loading = MutableLiveData(false)

    val inputtedCity = MutableLiveData("")

    val cityWeather = MutableLiveData<WeatherResult?>(null)
    val shouldShowCity = cityWeather.map { it != null }
    val weatherDesc = cityWeather.map { it?.weather?.first()?.description }
    val weatherUrl = cityWeather.map { "https://openweathermap.org/img/wn/${it?.weather?.first()?.icon}@4x.png" }
    val temperature = cityWeather.map { "${it?.main?.temp}ºF " }

    fun updateLocation(location: Location) = viewModelScope.launch(Dispatchers.IO) {
        switchLoading(true)
        val apiService = WeatherApi.getRetrofitInstance().create(WeatherApi::class.java)

        val call = apiService.getWeather(
            mapOf(
                "appid" to "d073e28efa11970244b9f726a7bd2623",
                "lat" to "${location.latitude}",
                "lon" to "${location.longitude}",
                "units" to "imperial"
            )
        ).execute()

        cityWeather.postValue(call.body())
        switchLoading(false)
    }

    fun updateLocationWithCity() = viewModelScope.launch(Dispatchers.IO) {
        switchLoading(true)
        val apiService = WeatherApi.getRetrofitInstance().create(WeatherApi::class.java)

        val call = apiService.getWeather(
            mapOf(
                "appid" to "d073e28efa11970244b9f726a7bd2623",
                "q" to "${inputtedCity.value}",
                "units" to "imperial"
            )
        ).execute()

        cityWeather.postValue(call.body())
        switchLoading(false)
    }

    private suspend fun switchLoading(newValue: Boolean) = withContext(Dispatchers.Main) {
        loading.value = newValue
    }


}