package fi.carterm.clearskiesweather.models.apiRoomCache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Michael Carter
 * @version 1
 *
 */
@Entity
data class DailyModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val timestamp: Double,
    val time_offset: Int,
    val sunrise : Double,
    val sunset : Double,
    val tempDay : Double,
    val tempNight: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure : Double,
    val humidity : Double,
    val dew_point : Double,
    val wind_speed : Double,
    @Embedded
    val weather : CurrentWeatherModel,
    val clouds : Double,
    val pop : Double,
    val uvi : Double
)
