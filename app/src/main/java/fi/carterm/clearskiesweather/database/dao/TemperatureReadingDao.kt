package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.sensors.LightSensorReading
import fi.carterm.clearskiesweather.models.sensors.TemperatureSensorReading

@Dao
interface TemperatureReadingDao {

    @Query("SELECT * FROM TemperatureSensorReading")
    fun getAllTemperatureSensorReadings(): LiveData<List<TemperatureSensorReading>>

    @Query("SELECT * FROM TemperatureSensorReading ORDER BY uuid_temperature DESC LIMIT 1")
    fun getLatestTempReading(): LiveData<TemperatureSensorReading>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(TemperatureSensorData: TemperatureSensorReading): Long

    @Update
    suspend fun update(TemperatureSensorData: TemperatureSensorReading)

    @Delete
    suspend fun delete(TemperatureSensorData: TemperatureSensorReading)
}