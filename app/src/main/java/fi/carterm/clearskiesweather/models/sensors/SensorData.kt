package fi.carterm.clearskiesweather.models.sensors

import fi.carterm.clearskiesweather.models.apiRoomCache.CurrentWeatherModel


data class SensorData(
    val sensorType: String,
    var sensorReading: Any,
    val condition: CurrentWeatherModel? = null
)
