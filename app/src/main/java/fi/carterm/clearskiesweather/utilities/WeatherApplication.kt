package fi.carterm.clearskiesweather.utilities

import android.app.Application
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.preference.PreferenceManager
import fi.carterm.clearskiesweather.database.WeatherDatabase
import fi.carterm.clearskiesweather.database.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WeatherApplication : Application() {
    private val applicationScope by lazy { CoroutineScope(SupervisorJob()) }
    private val database by lazy { WeatherDatabase.get(this, applicationScope) }
    private lateinit var sharedPreferences: SharedPreferences
    var onPhoneSensors = false
    var skipOnBoarding = false


    val repository by lazy {
        WeatherRepository(
            lightReadingDao = database.lightSensorReadingDao(),
            temperatureReadingDao = database.temperatureReadingDao(),
            dewPointAndAbsHumidityReadingDao = database.dewPointAndAbsHumidityDao(),
            pressureReadingDao = database.pressureReadingDao(),
            humidityReadingDao = database.humidityReadingDao(),
            weatherModelDao = database.weatherModelDao(),
            forecastDao = database.forecastDao()
        )
    }

    private val listener =
        OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when(key){
                "toggleSensorOn" -> {
                   onPhoneSensors = sharedPreferences.getBoolean(key, false)
                }
                "skipOnBoarding" -> {
                    skipOnBoarding = sharedPreferences.getBoolean(key, false)
                }

            }
        }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        onPhoneSensors = sharedPreferences.getBoolean("toggleSensorOn", false)
        skipOnBoarding = sharedPreferences.getBoolean("skipOnBoarding", false)
    }

}