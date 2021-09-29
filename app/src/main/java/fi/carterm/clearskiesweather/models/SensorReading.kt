package fi.carterm.clearskiesweather.models

import android.util.Log
import kotlin.math.exp
import kotlin.math.ln

data class SensorReading(
    var temperature: Float = 0.0f,
    var pressure: Float = 0.0f,
    var light: Float = 0.0f,
    var relativeHumidity: Float = 0.0f,
    ){

    var dewPoint: Double = 0.0
    var absHumidity: Double = 0.0

    fun calculateValues() {
        if(temperature != 0.0f && relativeHumidity != 0.0f ){
            dewPoint = (243.12 * (ln(relativeHumidity / 100.0) + (17.62 * temperature / (243.12 + temperature))) / (17.62 -( ln(relativeHumidity / 100.0 ) + (17.62 * temperature) / (243.12 + temperature))))
            Log.d("DEWABS-DEW", dewPoint.toString())
            absHumidity = ((216.7 * (relativeHumidity / 100) * 6.112 * exp((17.62 * temperature) / (243.12 + temperature))) / (273.15 + temperature))
            Log.d("DEWABS-ABS", absHumidity.toString())}
        else{
            Log.d("DEWABS-none", absHumidity.toString())
        }
    }
}
