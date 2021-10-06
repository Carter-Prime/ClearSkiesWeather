package fi.carterm.clearskiesweather.models.apiRoomCache

import androidx.room.Embedded


data class CurrentModel (
    val dt :  Double,
    val sunrise :  Double,
    val sunset :  Double,
    val temp : Double,
    val feels_like : Double,
    val pressure : Double,
    val humidity :  Double,
    val dew_point : Double,
    val uvi : Double,
    val clouds :  Double,
    val visibility :  Double,
    val wind_speed : Double,
    val wind_deg :  Double,
    @Embedded
    val weather : CurrentWeatherModel
)