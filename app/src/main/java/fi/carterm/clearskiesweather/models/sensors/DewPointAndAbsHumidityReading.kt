package fi.carterm.clearskiesweather.models.sensors


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fi.carterm.clearskiesweather.models.LocationDetails
import kotlin.math.exp
import kotlin.math.ln

@Entity
data class DewPointAndAbsHumidityReading(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uuid_DewPointAbsHumidity")
    val uuid: Long,
    @ColumnInfo(name = "timestamp_DewPointAbsHumidity")
    val timestamp: Long,
    val temperature: Float?,
    val relativeHumidity: Float?,
    @Embedded
    var location: LocationDetails? = null
){
    var dewPoint: Double? = null
    var absHumidityReading: Double? = null

    init{
        dewPoint = calculateDewPoint(temperature!!, relativeHumidity!!)
       absHumidityReading = calculateAbsHumidity(temperature, relativeHumidity)
    }

    private fun calculateDewPoint(temperature: Float, relativeHumidity:Float): Double {
        return (243.12 * (ln(relativeHumidity / 100.0) + (17.62 * temperature / (243.12 + temperature))) / (17.62 - (ln(
            relativeHumidity / 100.0
        ) + (17.62 * temperature) / (243.12 + temperature))))

    }

    private fun calculateAbsHumidity(temperature: Float, relativeHumidity:Float): Double {
        return ((216.7 * (relativeHumidity / 100) * 6.112 * exp((17.62 * temperature) / (243.12 + temperature))) / (273.15 + temperature))

    }
}
