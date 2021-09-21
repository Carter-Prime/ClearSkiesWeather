package fi.carterm.clearskiesweather.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fi.carterm.clearskiesweather.models.WeatherData

@Dao
interface WeatherDataDAO {

    @Query("SELECT * FROM WeatherData")
    fun getWeatherData(): LiveData<List<WeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: WeatherData): Long
}