package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.apiRoomCache.WeatherModel


@Dao
interface WeatherModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherModel: WeatherModel): Long

    @Update
    suspend fun update(weatherModel: WeatherModel)

    @Delete
    suspend fun delete(weatherModel: WeatherModel)

    @Query("SELECT * FROM one_call_weather")
    fun getAllWeather(): LiveData<List<WeatherModel>>
}