package fi.carterm.clearskiesweather.viewmodels

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.*
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.api.OneCallWeather
import fi.carterm.clearskiesweather.models.apiRoomCache.WeatherModel
import fi.carterm.clearskiesweather.models.misc.LocationDetails
import fi.carterm.clearskiesweather.models.sensors.*
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import fi.carterm.clearskiesweather.utilities.livedata.LocationLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    var tempSensor = true
    var lightSensor = true
    var gpsSensor = true
    var humiditySensor = true
    var pressureSensor = true
    var app = getApplication<WeatherApplication>()
    var useCurrentLocation = true

    private val repository = app.repository


    private val latestPhoneSensorData = repository.latestPhoneSensorData
    fun getLatestPhoneSensorData() = latestPhoneSensorData

    private val latestWeatherData = repository.getCurrentWeather
    fun getLatestWeather() = latestWeatherData

    private val locationDetails = LocationLiveData(application.applicationContext)
    fun getLocationLiveData() = locationDetails

    private val forecastLiveData = repository.getForecast
    fun getForecast() = forecastLiveData


    private val getOtherLocation = MutableLiveData<LocationDetails>()
    fun getOtherLocation() = getOtherLocation

    val otherLocationWeather = getOtherLocation.switchMap {
        liveData(Dispatchers.IO) {
            emit(repository.getWeather(it.latitude, it.longitude))
        }
    }

    private val locationError = MutableLiveData<String>()
    fun getLocationError() = locationError

    val weather = locationDetails.switchMap {
        liveData(Dispatchers.IO) {
            if (useCurrentLocation) {
                emit(repository.getWeather(it.latitude, it.longitude))
            }
        }
    }

    fun insertWeather(data: OneCallWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWeatherToDatabase(data)
        }
    }

    fun insertForecast(data: OneCallWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertForecasts(data)
        }

    }

    fun getLocationFromName(name: String) {
        if (Geocoder.isPresent()) {
            viewModelScope.launch(Dispatchers.IO) {

                val geocoder = Geocoder(getApplication<WeatherApplication>().applicationContext)
                val list = geocoder.getFromLocationName(name, 1)
                if (list.isNotEmpty()) {
                    val long = list[0].longitude
                    val lat = list[0].latitude
                    val otherLocation =
                        LocationDetails(lat.toString(), long.toString(), list[0].getAddressLine(0))
                    getOtherLocation.postValue(otherLocation)
                } else {
                    locationError.postValue(app.getString(R.string.error_location_not_found))
                }
            }

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

        val list = listOf(
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

        return list.filter { it.sensorReading != -1000f }

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
                getApplication<Application>().getString(R.string.sensor_sunrise),
                data.current.sunrise.toFloat()
            ),
            SensorData(
                getApplication<Application>().getString(R.string.sensor_sunset),
                data.current.sunset.toFloat()
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
                getApplication<Application>().getString(R.string.sensor_visibility),
                data.current.visibility.toFloat()
            ),

            )
    }


}