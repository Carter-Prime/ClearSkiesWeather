package fi.carterm.clearskiesweather.database

import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.database.dao.*
import fi.carterm.clearskiesweather.models.api.OneCallWeather
import fi.carterm.clearskiesweather.models.apiRoomCache.DailyModel
import fi.carterm.clearskiesweather.models.apiRoomCache.WeatherModel
import fi.carterm.clearskiesweather.models.sensors.*
import fi.carterm.clearskiesweather.services.networking.Constant
import fi.carterm.clearskiesweather.services.networking.DailyNetworkMapper
import fi.carterm.clearskiesweather.services.networking.NetworkMapper
import fi.carterm.clearskiesweather.services.networking.OpenWeatherRetrofitFactory

/**
 *
 * Application repository - handles all data calls from api and database
 *
 *
 * @author Michael Carter
 * @version 1
 *
 */

class WeatherRepository(
    private val lightReadingDao: LightReadingDao,
    private val temperatureReadingDao: TemperatureReadingDao,
    private val humidityReadingDao: HumidityReadingDao,
    private val dewPointAndAbsHumidityReadingDao: DewPointAndAbsHumidityReadingDao,
    private val pressureReadingDao: PressureReadingDao,
    private val weatherModelDao: WeatherModelDao,
    private val forecastDao: ForecastDao
) {

    private var apiKey = Constant.API_KEY

    private val call = OpenWeatherRetrofitFactory.SERVICE
    private val responseMapper = NetworkMapper()
    private val responseListMapper = DailyNetworkMapper()

    val allLightReadings: LiveData<List<LightSensorReading>> = lightReadingDao.getAllLightSensorReadings()
    val allTemperatureReadings: LiveData<List<TemperatureSensorReading>> = temperatureReadingDao.getAllTemperatureSensorReadings()
    val allPressureReadings: LiveData<List<PressureSensorReading>> = pressureReadingDao.getAllPressureSensorReadings()
    val allDewPointReadings: LiveData<List<DewPointAndAbsHumidityReading>> = dewPointAndAbsHumidityReadingDao.getAllDewPointAndAbsHumidityReadings()
    val allHumidityReadings: LiveData<List<HumiditySensorReading>> = humidityReadingDao.getAllHumiditySensorReadings()

    val latestPhoneSensorData: LiveData<PhoneSensorData> = lightReadingDao.getLatestReadings()
    val getCurrentWeather: LiveData<WeatherModel> = weatherModelDao.getLatestWeather()
    val getForecast: LiveData<List<DailyModel>> = forecastDao.getForecast()

    suspend fun addLightReading(sensorReading: LightSensorReading): Long {
        return lightReadingDao.insert(sensorReading)
    }

    suspend fun addTempReading(sensorReading: TemperatureSensorReading): Long {
        return temperatureReadingDao.insert(sensorReading)
    }

    suspend fun addDewAndAbsReading(sensorReading: DewPointAndAbsHumidityReading): Long {
        return dewPointAndAbsHumidityReadingDao.insert(sensorReading)
    }

    suspend fun addHumidityReading(sensorReading: HumiditySensorReading): Long {
        return humidityReadingDao.insert(sensorReading)
    }

    suspend fun addPressureReading(sensorReading: PressureSensorReading): Long {
        return pressureReadingDao.insert(sensorReading)
    }

    //API Calls

    /**
     * Makes a call to the weather API using position coordinates
     *
     * @param lat - latitude position as a string
     * @param long - longitude position as a string
     *
     * @return OneCallWeather object for passed location
     */
    suspend fun getWeather(lat: String, long: String): OneCallWeather {
        return call.getWeatherByLocation(lat = lat, lon = long, appid = apiKey)

    }

    /**
     * Maps relevant data to model and inserted to database
     *
     * @param response - API OneCallWeather object
     */
    suspend fun insertWeatherToDatabase(response: OneCallWeather) {
        val model = responseMapper.mapFromEntity(response)
        weatherModelDao.insert(model)
    }

    /**
     * Maps relevant data to model and inserted to database
     *
     * @param response - API OneCallWeather object
     */
    suspend fun insertForecasts(response: OneCallWeather) {
        val listModels = responseListMapper.mapListFromEntity(response)
        listModels.forEach {
            forecastDao.insert(it)
        }

    }


}
