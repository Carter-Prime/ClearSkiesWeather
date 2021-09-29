package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.models.WeatherData
import fi.carterm.clearskiesweather.utilities.WeatherApplication

class GraphViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository
    val weatherData: LiveData<List<WeatherData>> = repository.allWeather

}