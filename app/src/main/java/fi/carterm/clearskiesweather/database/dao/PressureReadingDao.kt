package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.sensors.PressureSensorReading

@Dao
interface PressureReadingDao {
    @Query("SELECT * FROM PressureSensorReading")
    fun getAllPressureSensorReadings(): LiveData<List<PressureSensorReading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pressureSensorData: PressureSensorReading): Long

    @Update
    suspend fun update(pressureSensorData: PressureSensorReading)

    @Delete
    suspend fun delete(pressureSensorData: PressureSensorReading)
}