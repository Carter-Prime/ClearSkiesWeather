package fi.carterm.clearskiesweather.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    val temp: Float,
    val humidity: Float,
    val pressure: Float,
    val wind: Float) {
}