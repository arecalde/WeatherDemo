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

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    //get application context, don't hold a reference get a new one each time
    private val context: Context
        get() = getApplication<Application>().applicationContext

    //set to true when we are doing a network call, display a spinner
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    //tied to the edit text bidirectionally
    val inputtedCity = MutableLiveData("")

    //the live data holding the result of the network call
    private val _cityWeather = MutableLiveData<WeatherResult?>(null)
    val cityWeather: LiveData<WeatherResult?>
        get() = _cityWeather

    //Map city weather to difference variables that will be used
    val shouldShowCity = cityWeather.map { it != null }
    val weatherDesc = cityWeather.map { it?.weather?.first()?.description }
    val weatherUrl = cityWeather.map { "https://openweathermap.org/img/wn/${it?.weather?.first()?.icon}@4x.png" }
    val temperature = cityWeather.map { "${it?.main?.temp}ºF " }

    /*
    * Update the location and get the weather
    * @param location the device location
    * */
    fun updateLocation(location: Location)  {
        getWeather(mutableMapOf(
            "lat" to "${location.latitude}",
            "lon" to "${location.longitude}"
        ))
    }

    private fun getWeather(params: MutableMap<String, String>) = viewModelScope.launch(Dispatchers.IO) {
        switchLoading(true)

        val apiService = WeatherApi.getRetrofitInstance().create(WeatherApi::class.java)

        params["appid"] = "d073e28efa11970244b9f726a7bd2623"
        params["units"] = "imperial"

        val call = apiService.getWeather(params).execute()
        SharedPreferenceHelper.setCity(context, call.body()?.name)

        _cityWeather.postValue(call.body())
        switchLoading(false)
    }

    /*
    * Get the city that the user inputted and get the weather based on it
    * */
    fun updateLocationWithCity() {
        inputtedCity.value?.let {
            updateLocationWithCity(it)
        }
    }

    /*
    * Get the weather based on the inputted city
    * @param city Name of city where we want to get weather
    * */
    fun updateLocationWithCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        getWeather(mutableMapOf("q" to city))
    }

    private suspend fun switchLoading(newValue: Boolean) = withContext(Dispatchers.Main) {
        _loading.value = newValue
    }


}