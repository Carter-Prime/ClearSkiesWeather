package fi.carterm.clearskiesweather.models


data class SensorData(
    val sensorType: String,
    val sensorReading: Float,
    val sensorImgResourceId: Int
)
