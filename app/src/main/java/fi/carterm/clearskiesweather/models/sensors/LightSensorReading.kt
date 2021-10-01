package fi.carterm.clearskiesweather.models.sensors

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fi.carterm.clearskiesweather.models.LocationDetails

@Entity
data class LightSensorReading(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uuid_light")
    val uuid: Long,
    @ColumnInfo(name = "timestamp_light")
    var timestamp: Long,
    @ColumnInfo(name = "reading_light")
    var sensorReading: Float?,
    @Embedded
    var location: LocationDetails? = null
)
