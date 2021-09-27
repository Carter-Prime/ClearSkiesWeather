package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import fi.carterm.clearskiesweather.database.WeatherDatabase
import fi.carterm.clearskiesweather.database.WeatherRepository
import fi.carterm.clearskiesweather.models.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SensorViewModel(application: Application) : AndroidViewModel(application) {

    //private val repository = getApplication<WeatherApplication>().repository
    private val repository :WeatherRepository
   val weatherData : LiveData<List<WeatherData>>

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        val weatherDataDao = WeatherDatabase.get(application, scope).WeatherDataDAO()
        repository = WeatherRepository(weatherDataDao)
        weatherData = repository.allWeather
    }
    fun insertWeather(temp: Float, humidity: Float, pressure: Double, wind: Double){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.addWeatherData(WeatherData(0, temp,
                 humidity, pressure, wind))
            Log.d("SensorViewModsel", "Weather added: $response")
        }
    }
}