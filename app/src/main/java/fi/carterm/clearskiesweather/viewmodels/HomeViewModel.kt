package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.sensors.LightSensorReading
import fi.carterm.clearskiesweather.models.SensorData
import fi.carterm.clearskiesweather.models.sensors.TemperatureSensorReading
import fi.carterm.clearskiesweather.utilities.LocationLiveData
import fi.carterm.clearskiesweather.utilities.SensorLiveData
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository
    val sensorLightReadings = repository.allLightReadings

    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData

//    private val allReadings = repository.latestSensorReadings
//    fun getAllReadings() = allReadings

    private val sensorLiveData = SensorLiveData(application)
    fun getSensorLiveData() = sensorLiveData

    private var lastLight = repository.latestLightReading
    fun getLastLight() = lastLight

    private var lastTemp = repository.latestTempReading
    fun getLastTemp() = lastTemp

    var data = listOf(
        SensorData("Temperature", 23.5f, R.drawable.clipart_temperature),
        SensorData("Humidity", 50.5f, R.drawable.clipart_humidity),
        SensorData("Light", 230f, R.drawable.clipart_light),
        SensorData("Pressure", 30f, R.drawable.clipart_barometer),
        SensorData("Wind", 5.5f, R.drawable.clipart_windy),
        SensorData("UV Rating", 10f, R.drawable.clipart_uv_rating),
        SensorData("Sunrise", 0630f, R.drawable.clipart_sunrise),
        SensorData("Sunset", 1830f, R.drawable.clipart_sunset)
    )


    


    fun lightOnChangeSaveToDatabase(current: LightSensorReading){
        val minRange = lastLight.value?.sensorReading!! - 5
        val maxRange = lastLight.value?.sensorReading!! + 5
        if(current.sensorReading!! in minRange..maxRange){
            return
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                val currentTime = System.currentTimeMillis()
                val newReading = LightSensorReading(0,currentTime, current.sensorReading, getLocationLiveData().value)
                val response =  repository.addLightReading(newReading)
                Log.d("lightChange", "Light was changed: $response")
            }

        }

    }

    fun temperatureOnChangeSaveToDatabase(current: TemperatureSensorReading){
        val minRange = lastTemp.value?.sensorReading!! - 2
        val maxRange = lastTemp.value?.sensorReading!! + 2
        if(current.sensorReading!! in minRange..maxRange){
            return
        }else{
            viewModelScope.launch(Dispatchers.IO) {
                val currentTime = System.currentTimeMillis()
                val newReading = TemperatureSensorReading(0,currentTime, current.sensorReading, getLocationLiveData().value)
                val response =  repository.addTempReading(newReading)
                Log.d("TempChange", "Temperature was changed: $response")
            }

        }

    }

}