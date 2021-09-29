package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.SensorData
import fi.carterm.clearskiesweather.models.WeatherData
import fi.carterm.clearskiesweather.utilities.LocationLiveData
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.exp
import kotlin.math.ln

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository
    val weatherData: LiveData<List<WeatherData>> = repository.allWeather

    private val locationLiveData = LocationLiveData(application.applicationContext)
    fun getLocationLiveData() = locationLiveData

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

    var temperature = 0.0f
    var light = 0.0f
    var pressure = 0.0f
    var relativeHumidity = 0.0f
    private var dewPoint = 0.0
    private var absHumidity = 0.0

    private fun calculateValues() {
        if(temperature != 0.0f && relativeHumidity != 0.0f ){
            dewPoint = (243.12 * (ln(relativeHumidity / 100.0) + (17.62 * temperature / (243.12 + temperature))) / (17.62 -( ln(relativeHumidity / 100.0 ) + (17.62 * temperature) / (243.12 + temperature))))
            Log.d("DEWABS-DEW", dewPoint.toString())
            absHumidity = ((216.7 * (relativeHumidity / 100) * 6.112 * exp((17.62 * temperature) / (243.12 + temperature))) / (273.15 + temperature))
            Log.d("DEWABS-ABS", absHumidity.toString())}
        else{
            Log.d("DEWABS-none", absHumidity.toString())
        }
    }
    
    fun saveToDatabase(){
        val timestamp = System.currentTimeMillis()
        val long = locationLiveData.value?.longitude ?: "null"
        val lat = locationLiveData.value?.latitude ?: "null"
        calculateValues()

        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.addWeatherData(
                WeatherData(
                    timestamp,
                    temperature,
                    relativeHumidity,
                    pressure,
                    light,
                    absHumidity,
                    dewPoint,
                    lat,
                    long
                )
            )
            Log.d("HomeViewModel", "Weather added: $response")
        }
    }

}