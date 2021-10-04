package fi.carterm.clearskiesweather.models.sensors

data class HomeSensorData(
    val reading_light: Float,
    val reading_temperature: Float,
    val reading_humidity: Float,
    val reading_pressure: Float,
    val absHumidityReading: Double,
    val dewPoint: Double
)
