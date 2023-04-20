package com.example.weather.search

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.example.weather.helpers.SharedPreferenceHelper
import com.example.weather.model.WeatherApi
import com.example.weather.model.json.WeatherResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val application: Application) : AndroidViewModel(application) {
    private val context: Context
        get() = getApplication<Application>().applicationContext

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

    fun updateLocationWithCity() {
        inputtedCity.value?.let {
            updateLocationWithCity(it)

        }
    }
    fun updateLocationWithCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        switchLoading(true)

        if (city.isEmpty()) return@launch

        val apiService = WeatherApi.getRetrofitInstance().create(WeatherApi::class.java)

        val call = apiService.getWeather(
            mapOf(
                "appid" to "d073e28efa11970244b9f726a7bd2623",
                "q" to city,
                "units" to "imperial"
            )
        ).execute()

        SharedPreferenceHelper.setCity(context, city)


        cityWeather.postValue(call.body())
        switchLoading(false)
    }

    private suspend fun switchLoading(newValue: Boolean) = withContext(Dispatchers.Main) {
        loading.value = newValue
    }


}