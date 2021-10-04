package fi.carterm.clearskiesweather.models


data class SensorData(
    val sensorType: String,
    var sensorReading: Float,
    val sensorImgResourceId: Int
)
