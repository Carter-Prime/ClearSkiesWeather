package fi.carterm.clearskiesweather.database

import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.database.dao.*
import fi.carterm.clearskiesweather.models.sensors.*


class WeatherRepository(private val lightReadingDao: LightReadingDao, private val temperatureReadingDao: TemperatureReadingDao, private val humidityReadingDao: HumidityReadingDao,
                        private val dewPointAndAbsHumidityReadingDao: DewPointAndAbsHumidityReadingDao, private val pressureReadingDao: PressureReadingDao) {

    val allLightReadings: LiveData<List<LightSensorReading>> = lightReadingDao.getAllLightSensorReadings()
    val latestHomeScreenData: LiveData<HomeSensorData> = lightReadingDao.getLatestReadings()

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


    suspend fun deleteLightReading(sensorReading: LightSensorReading){
        lightReadingDao.delete(sensorReading)
    }

    suspend fun updateLightReading(sensorReading: LightSensorReading){
        lightReadingDao.update(sensorReading)
    }


}
