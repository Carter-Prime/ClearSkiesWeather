package fi.carterm.clearskiesweather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.carterm.clearskiesweather.models.apiRoomCache.DailyModel


@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: DailyModel)

    @Query("SELECT * FROM DailyModel ORDER BY id")
    fun getForecast(): LiveData<List<DailyModel>>
}