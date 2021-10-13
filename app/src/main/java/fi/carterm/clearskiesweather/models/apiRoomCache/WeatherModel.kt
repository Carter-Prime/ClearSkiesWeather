package fi.carterm.clearskiesweather.models.apiRoomCache

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Michael Carter
 * @version 1
 *
 */

@Entity(tableName = "one_call_weather")
data class WeatherModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val timestamp: Long,
    val lat : Double,
    val lon : Double,
    val timezone : String,
    val timezone_offset : Int,
    @Embedded
    val current : CurrentModel,
)
