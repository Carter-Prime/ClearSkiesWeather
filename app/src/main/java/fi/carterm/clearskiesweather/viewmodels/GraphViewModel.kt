package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fi.carterm.clearskiesweather.utilities.WeatherApplication

class GraphViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository
    val sensorLightReadings = repository.allLightReadings

}