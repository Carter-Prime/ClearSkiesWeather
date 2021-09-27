package fi.carterm.clearskiesweather.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    val timestamp: Long,
    val temp: Float,
    val humidity: Float,
    val pressure: Float,
    val light: Float,
    val abshumidity: Float,
    val dewpoint: Float,
    val latitude: Double,
    val longitude: Double
) {
}