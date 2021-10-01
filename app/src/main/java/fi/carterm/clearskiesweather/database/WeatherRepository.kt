package fi.carterm.clearskiesweather.database

import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.database.dao.*
import fi.carterm.clearskiesweather.models.sensors.LightSensorReading
import fi.carterm.clearskiesweather.models.sensors.TemperatureSensorReading


class WeatherRepository(private val lightReadingDao: LightReadingDao, private val temperatureReadingDao: TemperatureReadingDao, private val humidityReadingDao: HumidityReadingDao,
                        private val dewPointAndAbsHumidityReadingDao: DewPointAndAbsHumidityReadingDao, private val pressureReadingDao: PressureReadingDao) {

    val allLightReadings: LiveData<List<LightSensorReading>> = lightReadingDao.getAllLightSensorReadings()
    val latestLightReading: LiveData<LightSensorReading> = lightReadingDao.getLatestLightReading()
    val latestTempReading: LiveData<TemperatureSensorReading> = temperatureReadingDao.getLatestTempReading()

    suspend fun addLightReading(sensorReading: LightSensorReading): Long{
        return lightReadingDao.insert(sensorReading)
    }
    suspend fun addTempReading(sensorReading: TemperatureSensorReading): Long{
        return temperatureReadingDao.insert(sensorReading)
    }


    suspend fun deleteLightReading(sensorReading: LightSensorReading){
        lightReadingDao.delete(sensorReading)
    }

    suspend fun updateLightReading(sensorReading: LightSensorReading){
        lightReadingDao.update(sensorReading)
    }


}
