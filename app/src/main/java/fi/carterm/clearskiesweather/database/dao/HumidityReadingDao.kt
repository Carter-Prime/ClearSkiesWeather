package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.sensors.HumiditySensorReading

@Dao
interface HumidityReadingDao {

    @Query("SELECT * FROM HumiditySensorReading")
    fun getAllHumiditySensorReadings(): LiveData<List<HumiditySensorReading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(HumiditySensorData: HumiditySensorReading): Long

    @Update
    suspend fun update(HumiditySensorData: HumiditySensorReading)

    @Delete
    suspend fun delete(HumiditySensorData: HumiditySensorReading)
}