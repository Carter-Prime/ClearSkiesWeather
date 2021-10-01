package fi.carterm.clearskiesweather.utilities

import android.app.Application
import fi.carterm.clearskiesweather.database.WeatherDatabase
import fi.carterm.clearskiesweather.database.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WeatherApplication : Application() {
    private val applicationScope by lazy { CoroutineScope(SupervisorJob()) }
    private val database by lazy { WeatherDatabase.get(this, applicationScope) }

    val repository by lazy { WeatherRepository(lightReadingDao = database.lightSensorReadingDao(), temperatureReadingDao = database.temperatureReadingDao(),
    dewPointAndAbsHumidityReadingDao = database.dewPointAndAbsHumidityDao(), pressureReadingDao = database.pressureReadingDao(), humidityReadingDao = database.humidityReadingDao()) }
}