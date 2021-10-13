package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.sensors.PressureSensorReading

interface mySensorDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pressureSensorData: T): Long

    @Update
    suspend fun update(pressureSensorData: T)

    @Delete
    suspend fun delete(pressureSensorData: T)
}

@Query("SELECT * FROM T")
inline fun <reified T : Any> mySensorDao<T>.getAllSensorReadings() {
}
/*
inline final fun <reified T : Data> by() = emptyList<T>()
inline fun <reified T : Data> ListHasShape<*>.by() = emptyList<T>()

@Query("SELECT * FROM T")
inline fun <reified T: Any> getAllSensorReadings(): LiveData<List<T?>>
*/