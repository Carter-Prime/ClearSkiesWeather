package fi.carterm.clearskiesweather.database

import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.models.WeatherData

class WeatherRepository(private val weatherDataDAO: WeatherDataDAO) {

    val allWeather: LiveData<List<WeatherData>> = weatherDataDAO.getWeatherData()

    suspend fun addWeather(data: WeatherData): Long {
        return weatherDataDAO.insert(data)
    }
}