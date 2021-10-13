package fi.carterm.clearskiesweather.models.sensors

/**
 * @author Michael Carter
 * @version 1
 *
 */

data class PhoneSensorData(
    val reading_light: Float,
    val reading_temperature: Float,
    val reading_humidity: Float,
    val reading_pressure: Float,
    val absHumidityReading: Double,
    val dewPoint: Double
)
