package fi.carterm.clearskiesweather.models.sensors

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fi.carterm.clearskiesweather.models.LocationDetails

@Entity
data class TemperatureSensorReading(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uuid_temperature")
    val uuid: Long,
    @ColumnInfo(name = "timestamp_temperature")
    var timestamp: Long,
    @ColumnInfo(name = "reading_temperature")
    var sensorReading: Float?,
    @Embedded
    var location: LocationDetails? = null
)
