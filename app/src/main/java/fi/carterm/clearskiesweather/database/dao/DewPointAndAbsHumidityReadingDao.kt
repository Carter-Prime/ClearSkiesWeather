package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.sensors.DewPointAndAbsHumidityReading

/**
 *
 * @author Michael Carter
 * @version 1
 *
 */
@Dao
interface DewPointAndAbsHumidityReadingDao {
    @Query("SELECT * FROM DewPointAndAbsHumidityReading")
    fun getAllDewPointAndAbsHumidityReadings(): LiveData<List<DewPointAndAbsHumidityReading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(DewPointAndAbsHumidityData: DewPointAndAbsHumidityReading): Long

    @Update
    suspend fun update(DewPointAndAbsHumidityData: DewPointAndAbsHumidityReading)

    @Delete
    suspend fun delete(DewPointAndAbsHumidityData: DewPointAndAbsHumidityReading)
}