package fi.carterm.clearskiesweather.database

import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.database.dao.*
import fi.carterm.clearskiesweather.models.api.OneCallWeather
import fi.carterm.clearskiesweather.models.apiRoomCache.WeatherModel
import fi.carterm.clearskiesweather.models.sensors.*
import fi.carterm.clearskiesweather.services.networking.NetworkMapper
import fi.carterm.clearskiesweather.services.networking.OpenWeatherRetrofitFactory


class WeatherRepository(private val lightReadingDao: LightReadingDao,
                        private val temperatureReadingDao: TemperatureReadingDao,
                        private val humidityReadingDao: HumidityReadingDao,
                        private val dewPointAndAbsHumidityReadingDao: DewPointAndAbsHumidityReadingDao,
                        private val pressureReadingDao: PressureReadingDao,
                        private val weatherModelDao: WeatherModelDao) {

    private var API_KEY = "3b652131c8bd2ab5f62f1959b63267f3"

    private val call = OpenWeatherRetrofitFactory.SERVICE
    private val responseMapper = NetworkMapper()

    val allLightReadings: LiveData<List<LightSensorReading>> = lightReadingDao.getAllLightSensorReadings()
    val allTemperatureReadings: LiveData<List<TemperatureSensorReading>> = temperatureReadingDao.getAllTemperatureSensorReadings()
    val allPressureReadings: LiveData<List<PressureSensorReading>> = pressureReadingDao.getAllPressureSensorReadings()
    val allDewPointReadings: LiveData<List<DewPointAndAbsHumidityReading>> = dewPointAndAbsHumidityReadingDao.getAllDewPointAndAbsHumidityReadings()
    val allHumidityReadings: LiveData<List<HumiditySensorReading>> = humidityReadingDao.getAllHumiditySensorReadings()

    val latestPhoneSensorData: LiveData<PhoneSensorData> = lightReadingDao.getLatestReadings()
    val getWeatherModel: LiveData<List<WeatherModel>> = weatherModelDao.getAllWeather()

    val getCurrentWeather: LiveData<WeatherModel> = weatherModelDao.getLatestWeather()

    suspend fun addLightReading(sensorReading: LightSensorReading): Long{
        return lightReadingDao.insert(sensorReading)
    }
    suspend fun addTempReading(sensorReading: TemperatureSensorReading): Long{
        return temperatureReadingDao.insert(sensorReading)
    }

    suspend fun addDewAndAbsReading(sensorReading: DewPointAndAbsHumidityReading): Long{
        return dewPointAndAbsHumidityReadingDao.insert(sensorReading)
    }

    suspend fun addHumidityReading(sensorReading: HumiditySensorReading): Long {
        return humidityReadingDao.insert(sensorReading)
    }

    suspend fun addPressureReading(sensorReading: PressureSensorReading): Long {
        return pressureReadingDao.insert(sensorReading)
    }




    //API Calls

    suspend fun getWeather(lat: String, long: String): OneCallWeather {
       return call.getWeatherByLocation(lat=lat, lon=long, appid = API_KEY)

    }

    suspend fun insertWeatherToDatabase(response: OneCallWeather){
        val model = responseMapper.mapFromEntity(response)
        weatherModelDao.insert(model)
    }


}
