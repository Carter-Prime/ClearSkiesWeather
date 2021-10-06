package fi.carterm.clearskiesweather.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import fi.carterm.clearskiesweather.database.dao.*
import fi.carterm.clearskiesweather.models.apiRoomCache.WeatherModel
import fi.carterm.clearskiesweather.models.misc.LocationDetails
import fi.carterm.clearskiesweather.models.sensors.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(
    entities = [LightSensorReading::class, TemperatureSensorReading::class,
        DewPointAndAbsHumidityReading::class, HumiditySensorReading::class,
               PressureSensorReading::class, WeatherModel::class],
    version = 1
)

abstract class WeatherDatabase : RoomDatabase() {
    abstract fun lightSensorReadingDao(): LightReadingDao
    abstract fun temperatureReadingDao(): TemperatureReadingDao
    abstract fun humidityReadingDao(): HumidityReadingDao
    abstract fun dewPointAndAbsHumidityDao(): DewPointAndAbsHumidityReadingDao
    abstract fun pressureReadingDao(): PressureReadingDao
    abstract fun weatherModelDao(): WeatherModelDao

    private class WeatherDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let { database ->
                scope.launch {
                    val lightDao = database.lightSensorReadingDao()
                    val tempDao = database.temperatureReadingDao()
                    val humidityDao = database.humidityReadingDao()
                    val dewPointAbsHumidityDao = database.dewPointAndAbsHumidityDao()
                    val pressureReadingDao = database.pressureReadingDao()
                    val currentTime = System.currentTimeMillis()

                    val locations = LocationDetails("12345", "12345", address = "TestCity")
                    Log.d("weatherTest", "database init")

                    val light = listOf(
                        LightSensorReading(0, currentTime, 23.2f, locations),
                        LightSensorReading(0, currentTime, 25.2f, locations),
                        LightSensorReading(0, currentTime, 24.2f, locations)
                    )

                    val temp = listOf(
                        TemperatureSensorReading(0, currentTime, 10f, locations),
                        TemperatureSensorReading(0, currentTime, 12f, locations),
                        TemperatureSensorReading(0, currentTime, 20f, locations)
                    )

                    val humid = listOf(
                        HumiditySensorReading(0, currentTime, 300f, locations),
                        HumiditySensorReading(0, currentTime, 350f, locations),
                        HumiditySensorReading(0, currentTime, 240f, locations)
                    )

                    val dewAbs = listOf(
                        DewPointAndAbsHumidityReading(0,currentTime, 23f, 300f, locations),
                        DewPointAndAbsHumidityReading(0,currentTime, 20f, 350f, locations),
                        DewPointAndAbsHumidityReading(0,currentTime, 20f, 400f, locations)
                    )

                    val pressure = listOf(
                        PressureSensorReading(0, currentTime, 350f, locations),
                        PressureSensorReading(0, currentTime, 300f, locations),
                        PressureSensorReading(0, currentTime, 250f, locations),
                    )

                    lightDao.insert(light[0])
                    lightDao.insert(light[1])
                    lightDao.insert(light[2])

                    tempDao.insert(temp[0])
                    tempDao.insert(temp[1])
                    tempDao.insert(temp[2])

                    humidityDao.insert(humid[0])
                    humidityDao.insert(humid[1])
                    humidityDao.insert(humid[2])

                    dewPointAbsHumidityDao.insert(dewAbs[0])
                    dewPointAbsHumidityDao.insert(dewAbs[1])
                    dewPointAbsHumidityDao.insert(dewAbs[2])

                    pressureReadingDao.insert(pressure[0])
                    pressureReadingDao.insert(pressure[1])
                    pressureReadingDao.insert(pressure[2])

                }
            }
        }
    }

    companion object {
        private var instance: WeatherDatabase? = null

        @Synchronized
        fun get(context: Context, scope: CoroutineScope): WeatherDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java, "weatherDatabase.db"
                )
                    .addCallback(WeatherDatabaseCallback(scope))
                    .build()
            }
            return instance!!
        }
    }
}