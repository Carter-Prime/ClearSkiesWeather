package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.SensorData
import fi.carterm.clearskiesweather.models.sensors.*
import fi.carterm.clearskiesweather.utilities.LocationLiveData
import fi.carterm.clearskiesweather.utilities.SensorLiveData
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository
    val sensorLightReadings = repository.allLightReadings

    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData

    private val latestHomeScreenData = repository.latestHomeScreenData
    fun getLatestHomeScreenData() = latestHomeScreenData

    private val sensorLiveData = SensorLiveData(application)
    fun getSensorLiveData() = sensorLiveData


    fun lightOnChangeSaveToDatabase(current: LightSensorReading) {
        if (getLatestHomeScreenData().value != null) {
            val minRange = getLatestHomeScreenData().value!!.reading_light - 1
            val maxRange = getLatestHomeScreenData().value!!.reading_light + 1
            if (current.sensorReading!! in minRange..maxRange) {
                return
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val currentTime = System.currentTimeMillis()
                    val newReading = LightSensorReading(
                        0,
                        currentTime,
                        current.sensorReading,
                        getLocationLiveData().value
                    )
                    repository.addLightReading(newReading)
                }

            }
        }
    }

    fun temperatureOnChangeSaveToDatabase(current: TemperatureSensorReading) {
        if (getLatestHomeScreenData().value != null) {
            val minRange = getLatestHomeScreenData().value!!.reading_temperature - 1
            val maxRange = getLatestHomeScreenData().value!!.reading_temperature + 1
            if (current.sensorReading!! in minRange..maxRange) {
                return
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val currentTime = System.currentTimeMillis()
                    val newReading = TemperatureSensorReading(
                        0,
                        currentTime,
                        current.sensorReading,
                        getLocationLiveData().value
                    )
                    repository.addTempReading(newReading)

                    val newReadingTwo = DewPointAndAbsHumidityReading(
                        0,
                        currentTime,
                        getLatestHomeScreenData().value!!.reading_temperature,
                        getLatestHomeScreenData().value!!.reading_humidity,
                        getLocationLiveData().value
                    )

                    repository.addDewAndAbsReading(newReadingTwo)
                }

            }
        }

    }

    fun humidityOnChangeSaveToDatabase(current: HumiditySensorReading) {
        if (getLatestHomeScreenData().value != null) {
            val minRange = getLatestHomeScreenData().value!!.reading_humidity - 1
            val maxRange = getLatestHomeScreenData().value!!.reading_humidity + 1
            if (current.sensorReading!! in minRange..maxRange) {
                return
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val currentTime = System.currentTimeMillis()
                    val newReading = HumiditySensorReading(
                        0,
                        currentTime,
                        current.sensorReading,
                        getLocationLiveData().value
                    )
                    repository.addHumidityReading(newReading)

                    val newReadingTwo = DewPointAndAbsHumidityReading(
                        0,
                        currentTime,
                        getLatestHomeScreenData().value!!.reading_temperature,
                        getLatestHomeScreenData().value!!.reading_humidity,
                        getLocationLiveData().value
                    )

                    repository.addDewAndAbsReading(newReadingTwo)
                }

            }
        }

    }

    fun pressureOnChangeSaveToDatabase(current: PressureSensorReading) {
        if (getLatestHomeScreenData().value != null) {
            val minRange = getLatestHomeScreenData().value!!.reading_pressure - 1
            val maxRange = getLatestHomeScreenData().value!!.reading_pressure + 1
            if (current.sensorReading!! in minRange..maxRange) {
                return
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    val currentTime = System.currentTimeMillis()
                    val newReading = PressureSensorReading(
                        0,
                        currentTime,
                        current.sensorReading,
                        getLocationLiveData().value
                    )
                    repository.addPressureReading(newReading)
                }

            }
        }
    }

    fun createList(data: HomeSensorData): List<SensorData> {
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

}