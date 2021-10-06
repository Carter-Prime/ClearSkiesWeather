package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.api.OneCallWeather
import fi.carterm.clearskiesweather.models.sensors.*
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import fi.carterm.clearskiesweather.utilities.livedata.LocationLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository


    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData

    private val latestPhoneSensorData = repository.latestPhoneSensorData
    fun getLatestPhoneSensorData() = latestPhoneSensorData



    private val getWeatherModel = repository.getWeatherModel
    fun getAllWeatherModel() = getWeatherModel

    var openWeatherCall = locationLiveData.switchMap {
        liveData(Dispatchers.IO){
            emit(repository.getWeather(it.latitude, it.longitude))
    }
    }

    fun insertWeather(data: OneCallWeather){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertWeatherToDatabase(data)
        }
    }


    fun lightOnChangeSaveToDatabase(current: Float) {
        if (current != -1000f) {
                viewModelScope.launch(Dispatchers.IO) {
                    val currentTime = System.currentTimeMillis()
                    val newReading = LightSensorReading(
                        0,
                        currentTime,
                        current,
                        getLocationLiveData().value
                    )
                    val response = repository.addLightReading(newReading)
                    Log.d("weatherTest", "light inserted $response")
                }

            }
        }

    fun temperatureOnChangeSaveToDatabase(current: Float) {
        if (current != -1000f) {
                viewModelScope.launch(Dispatchers.IO) {
                    val currentTime = System.currentTimeMillis()
                    val newReading = TemperatureSensorReading(
                        0,
                        currentTime,
                        current,
                        getLocationLiveData().value
                    )
                    val responseOne = repository.addTempReading(newReading)

                    val newReadingTwo = DewPointAndAbsHumidityReading(
                        0,
                        currentTime,
                        getLatestPhoneSensorData().value!!.reading_temperature,
                        getLatestPhoneSensorData().value!!.reading_humidity,
                        getLocationLiveData().value
                    )

                   val responseTwo = repository.addDewAndAbsReading(newReadingTwo)
                    Log.d("weatherTest", "temp inserted $responseOne and dewPoint inserted $responseTwo")
                }

            }
        }

    fun humidityOnChangeSaveToDatabase(current: Float) {
        if (current != -1000f) {
            viewModelScope.launch(Dispatchers.IO) {
                val currentTime = System.currentTimeMillis()
                val newReading = HumiditySensorReading(
                    0,
                    currentTime,
                    current,
                    getLocationLiveData().value
                )
                val responseOne = repository.addHumidityReading(newReading)

                val newReadingTwo = DewPointAndAbsHumidityReading(
                    0,
                    currentTime,
                    getLatestPhoneSensorData().value!!.reading_temperature,
                    getLatestPhoneSensorData().value!!.reading_humidity,
                    getLocationLiveData().value
                )

                val responseTwo = repository.addDewAndAbsReading(newReadingTwo)
                Log.d("weatherTest", "humidity inserted $responseOne and dewPoint inserted $responseTwo")
            }

        }
    }

    fun pressureOnChangeSaveToDatabase(current: Float) {
        if (current != -1000f) {
            viewModelScope.launch(Dispatchers.IO) {
                val currentTime = System.currentTimeMillis()
                val newReading = PressureSensorReading(
                    0,
                    currentTime,
                    current,
                    getLocationLiveData().value
                )
                val response = repository.addPressureReading(newReading)
                Log.d("weatherTest", "pressure inserted $response")
            }

        }
    }

    fun createListOfPhoneSensorData(data: PhoneSensorData): List<SensorData> {
        return listOf(
            SensorData("Temperature", data.reading_temperature, R.drawable.clipart_temperature),
            SensorData("Humidity", data.reading_humidity, R.drawable.clipart_humidity),
            SensorData("Light", data.reading_light, R.drawable.clipart_light),
            SensorData("Pressure", data.reading_pressure, R.drawable.clipart_barometer),
            SensorData(
                "Absolute Humidity",
                data.absHumidityReading.toFloat(),
                R.drawable.clipart_windy
            ),
            SensorData("Dew Point", data.dewPoint.toFloat(), R.drawable.clipart_uv_rating),
        )
    }

    fun createListOfCurrentWeatherData(data: OneCallWeather): List<SensorData> {
//        val sunrise = Instant.ofEpochSecond(data.current.sunrise.toLong())
//            .atZone(ZoneId.systemDefault())
//            .toLocalTime()
//        val sunset = Instant.ofEpochSecond(data.current.sunset.toLong())
//            .atZone(ZoneId.systemDefault())
//            .toLocalTime()

        return listOf(
            SensorData("Temperature", data.current.temp.toFloat(), R.drawable.clipart_temperature),
            SensorData("Humidity", data.current.humidity.toFloat(), R.drawable.clipart_humidity),
            SensorData("Light", data.current.visibility.toFloat(), R.drawable.clipart_light),
            SensorData("Pressure", data.current.pressure.toFloat(), R.drawable.clipart_barometer),
            SensorData(
                "Wind",
                data.current.wind_speed.toFloat(),
                R.drawable.clipart_windy
            ),
            SensorData("UV Rating", data.current.uvi.toFloat(), R.drawable.clipart_uv_rating),
            SensorData("Sunrise",data.current.sunrise , R.drawable.clipart_sunrise),
            SensorData("Sunset", data.current.sunset, R.drawable.clipart_sunset),
        )
    }


}