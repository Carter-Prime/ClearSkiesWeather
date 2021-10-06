package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.sensors.PhoneSensorData
import fi.carterm.clearskiesweather.models.sensors.LightSensorReading


@Dao
interface LightReadingDao {

    @Query("SELECT * FROM LightSensorReading")
    fun getAllLightSensorReadings(): LiveData<List<LightSensorReading>>

    @Query("SELECT * FROM LightSensorReading ORDER BY uuid_light DESC LIMIT 1")
    fun getLatestLightReading(): LiveData<LightSensorReading>

    @Transaction
    @Query("""SELECT reading_light, reading_temperature, reading_humidity, reading_pressure, absHumidityReading, dewPoint
            FROM TemperatureSensorReading, LightSensorReading, HumiditySensorReading, PressureSensorReading, DewPointAndAbsHumidityReading
            ORDER BY uuid_light DESC, uuid_temperature DESC, uuid_humidity DESC, uuid_pressure DESC, uuid_DewPointAbsHumidity DESC LIMIT 1""")
    fun getLatestReadings(): LiveData<PhoneSensorData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lightSensorData: LightSensorReading): Long

    @Update
    suspend fun update(lightSensorData: LightSensorReading)

    @Delete
    suspend fun delete(lightSensorData: LightSensorReading)
}