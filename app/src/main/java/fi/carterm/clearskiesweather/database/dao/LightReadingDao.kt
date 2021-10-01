package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.sensors.LightSensorReading


@Dao
interface LightReadingDao {

    @Query("SELECT * FROM LightSensorReading")
    fun getAllLightSensorReadings(): LiveData<List<LightSensorReading>>

    @Query("SELECT * FROM LightSensorReading ORDER BY uuid_light DESC LIMIT 1")
    fun getLatestLightReading(): LiveData<LightSensorReading>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lightSensorData: LightSensorReading): Long

    @Update
    suspend fun update(lightSensorData: LightSensorReading)

    @Delete
    suspend fun delete(lightSensorData: LightSensorReading)
}