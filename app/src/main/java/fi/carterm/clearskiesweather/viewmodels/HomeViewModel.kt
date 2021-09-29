package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.SensorData
import fi.carterm.clearskiesweather.models.WeatherData
import fi.carterm.clearskiesweather.utilities.LocationLiveData
import fi.carterm.clearskiesweather.utilities.SensorLiveData
import fi.carterm.clearskiesweather.utilities.WeatherApplication

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository
    val weatherData: LiveData<List<WeatherData>> = repository.allWeather

    private val locationLiveData = LocationLiveData(application.applicationContext)
    fun getLocationLiveData() = locationLiveData

    private val sensorLiveData = SensorLiveData(application)
    fun getSensorLiveData() = sensorLiveData

    var sensorArray = listOf(
        SensorData("Temperature", 23.5f, R.drawable.clipart_temperature),
        SensorData("Humidity", 50.5f, R.drawable.clipart_humidity),
        SensorData("Light", 230f, R.drawable.clipart_light),
        SensorData("Pressure", 30f, R.drawable.clipart_barometer),
        SensorData("Wind", 5.5f, R.drawable.clipart_windy),
        SensorData("UV Rating", 10f, R.drawable.clipart_uv_rating),
        SensorData("Sunrise", 0630f, R.drawable.clipart_sunrise),
        SensorData("Sunset", 1830f, R.drawable.clipart_sunset)
    )
    
//    fun saveToDatabase(){
//        val timestamp = System.currentTimeMillis()
//        val long = locationLiveData.value?.longitude ?: "null"
//        val lat = locationLiveData.value?.latitude ?: "null"
//
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = repository.addWeatherData(
//                WeatherData(
//                    timestamp,
//                    temperature,
//                    relativeHumidity,
//                    pressure,
//                    light,
//                    absHumidity,
//                    dewPoint,
//                    lat,
//                    long
//                )
//            )
//            Log.d("HomeViewModel", "Weather added: $response")
//        }
//    }

}