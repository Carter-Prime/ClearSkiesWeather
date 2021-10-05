package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import androidx.lifecycle.*
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.api.OneCallWeather
import fi.carterm.clearskiesweather.models.sensors.SensorData
import fi.carterm.clearskiesweather.models.sensors.*
import fi.carterm.clearskiesweather.utilities.livedata.LocationLiveData
import fi.carterm.clearskiesweather.utilities.livedata.SensorLiveData
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = getApplication<WeatherApplication>().repository


    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData

    private val latestPhoneSensorData = repository.latestPhoneSensorData
    fun getLatestPhoneSensorData() = latestPhoneSensorData

    private val sensorLiveData = SensorLiveData(application)
    fun getSensorLiveData() = sensorLiveData

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


    fun lightOnChangeSaveToDatabase(current: LightSensorReading) {
        if (getLatestPhoneSensorData().value != null) {
            val minRange = getLatestPhoneSensorData().value!!.reading_light - 1
            val maxRange = getLatestPhoneSensorData().value!!.reading_light + 1
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
        if (getLatestPhoneSensorData().value != null) {
            val minRange = getLatestPhoneSensorData().value!!.reading_temperature - 1
            val maxRange = getLatestPhoneSensorData().value!!.reading_temperature + 1
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
                        getLatestPhoneSensorData().value!!.reading_temperature,
                        getLatestPhoneSensorData().value!!.reading_humidity,
                        getLocationLiveData().value
                    )

                    repository.addDewAndAbsReading(newReadingTwo)
                }

            }
        }

    }

    fun humidityOnChangeSaveToDatabase(current: HumiditySensorReading) {
        if (getLatestPhoneSensorData().value != null) {
            val minRange = getLatestPhoneSensorData().value!!.reading_humidity - 1
            val maxRange = getLatestPhoneSensorData().value!!.reading_humidity + 1
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
                        getLatestPhoneSensorData().value!!.reading_temperature,
                        getLatestPhoneSensorData().value!!.reading_humidity,
                        getLocationLiveData().value
                    )

                    repository.addDewAndAbsReading(newReadingTwo)
                }

            }
        }

    }

    fun pressureOnChangeSaveToDatabase(current: PressureSensorReading) {
        if (getLatestPhoneSensorData().value != null) {
            val minRange = getLatestPhoneSensorData().value!!.reading_pressure - 1
            val maxRange = getLatestPhoneSensorData().value!!.reading_pressure + 1
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


}