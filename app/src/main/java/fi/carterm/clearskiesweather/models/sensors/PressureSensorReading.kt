package fi.carterm.clearskiesweather.models.sensors

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fi.carterm.clearskiesweather.models.misc.LocationDetails

/**
 * @author Michael Carter
 * @version 1
 *
 */

@Entity
data class PressureSensorReading(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uuid_pressure")
    val uuid: Long,
    @ColumnInfo(name = "timestamp_pressure")
    var timestamp: Long,
    @ColumnInfo(name = "reading_pressure")
    var sensorReading: Float?,
    @Embedded
    var location: LocationDetails? = null
)
