package fi.carterm.clearskiesweather.utilities

import android.app.Application
import fi.carterm.clearskiesweather.database.WeatherDatabase
import fi.carterm.clearskiesweather.database.WeatherRepository

class WeatherApplication : Application() {
    private val database by lazy { WeatherDatabase.get(this) }
    val repository by lazy { WeatherRepository(database.WeatherDataDAO()) }
}