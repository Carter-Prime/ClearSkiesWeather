package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import fi.carterm.clearskiesweather.database.WeatherDatabase
import fi.carterm.clearskiesweather.database.WeatherRepository
import fi.carterm.clearskiesweather.models.WeatherData
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SensorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository
    val weatherData : LiveData<List<WeatherData>> = repository.allWeather

    fun insertWeather(
        timestamp: Long,
        temp: Float,
        humidity: Float,
        pressure: Float,
        light: Float,
        abshumidity: Double,
        dewpoint: Double,
        latitude: Double,
        longitude: Double,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWeatherData(
                WeatherData(
                    timestamp,
                    temp,
                    humidity,
                    pressure,
                    light,
                    abshumidity,
                    dewpoint,
                    latitude,
                    longitude
                )
            )
        }
    }
}