package fi.carterm.clearskiesweather.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    val temp: Double,
    val humidity: Double,
    val pressure: Double,
    val wind: Double) {
}