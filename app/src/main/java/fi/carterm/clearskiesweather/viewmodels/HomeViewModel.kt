package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.api.OneCallWeather
import fi.carterm.clearskiesweather.models.apiRoomCache.WeatherModel
import fi.carterm.clearskiesweather.models.sensors.*
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import fi.carterm.clearskiesweather.utilities.livedata.LocationLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var tempSensor = true
    var lightSensor = true
    var gpsSensor = true
    var humiditySensor = true
    var pressureSensor = true

    private val repository = getApplication<WeatherApplication>().repository
    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData

    private val latestPhoneSensorData = repository.latestPhoneSensorData
    fun getLatestPhoneSensorData() = latestPhoneSensorData

    private val latestWeatherData = repository.getCurrentWeather
    fun getLatestWeather() = latestWeatherData

    var openWeatherCall = locationLiveData.switchMap {
        liveData(Dispatchers.IO) {
            emit(repository.getWeather(it.latitude, it.longitude))
        }
    }

    fun insertWeather(data: OneCallWeather) {
        viewModelScope.launch(Dispatchers.IO) {
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
                repository.addLightReading(newReading)

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
                repository.addPressureReading(newReading)
            }

        }
    }

    fun createListOfPhoneSensorData(data: PhoneSensorData): List<SensorData> {

        return listOf(
            SensorData(
                getApplication<Application>().getString(R.string.sensor_temperature),
                if (tempSensor) data.reading_temperature else -1000f
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_humidity),
                if (humiditySensor) data.reading_humidity else -1000f
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_light),
                if (lightSensor) data.reading_light else -1000f

            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_pressure),
                if (pressureSensor) data.reading_pressure else -1000f

            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_absolute_humidity),
                if (tempSensor && humiditySensor) data.absHumidityReading.toFloat() else -1000f
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_dew_point),
                if (tempSensor && humiditySensor) data.dewPoint.toFloat() else -1000f
            ),
        )
    }

    fun createListOfCurrentWeatherData(data: WeatherModel): List<SensorData> {

        return listOf(
            SensorData(
                getApplication<Application>().getString(R.string.sensor_temperature),
                data.current.temp.toFloat(),
                data.current.weather
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_humidity),
                data.current.humidity.toFloat()
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_visibility),
                data.current.visibility.toFloat()
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_pressure),
                data.current.pressure.toFloat()
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_wind),
                data.current.wind_speed.toFloat()
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_uv_rating),
                data.current.uvi.toFloat()
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_sunrise),
                data.current.sunrise.toFloat()
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_sunset),
                data.current.sunset.toFloat()
            ),
        )
    }


}