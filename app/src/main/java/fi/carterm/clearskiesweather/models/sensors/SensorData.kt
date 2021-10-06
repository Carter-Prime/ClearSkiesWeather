package fi.carterm.clearskiesweather.models.sensors


data class SensorData(
    val sensorType: String,
    var sensorReading: Any,
    val sensorImgResourceId: Int
)
