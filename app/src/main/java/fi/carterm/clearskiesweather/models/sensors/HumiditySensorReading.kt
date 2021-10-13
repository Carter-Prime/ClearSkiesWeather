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
data class HumiditySensorReading(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uuid_humidity")
    val uuid: Long,
    @ColumnInfo(name = "timestamp_humidity")
    var timestamp: Long,
    @ColumnInfo(name = "reading_humidity")
    var sensorReading: Float?,
    @Embedded
    var location: LocationDetails? = null
)
