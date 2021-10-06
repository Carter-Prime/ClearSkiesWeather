package fi.carterm.clearskiesweather.services.networking

import fi.carterm.clearskiesweather.models.api.OneCallWeather
import fi.carterm.clearskiesweather.models.apiRoomCache.CurrentModel
import fi.carterm.clearskiesweather.models.apiRoomCache.CurrentWeatherModel
import fi.carterm.clearskiesweather.models.apiRoomCache.WeatherModel

import fi.carterm.clearskiesweather.services.networking.util.EntityMapper

class NetworkMapper : EntityMapper<OneCallWeather, WeatherModel> {

    override fun mapFromEntity(entity: OneCallWeather): WeatherModel {
        val current = entity.current
        val newCurrent = CurrentModel(
            current.dt,
            current.sunrise,
            current.sunset,
            current.temp,
            current.feels_like,
            current.pressure,
            current.humidity,
            current.dew_point,
            current.uvi,
            current.clouds,
            current.visibility,
            current.wind_speed,
            current.wind_deg,
            CurrentWeatherModel(current.weather[0].main, current.weather[0].description, current.weather[0].icon)
        )
        return WeatherModel(
            0,
            System.currentTimeMillis(),
            entity.lat,
            entity.lon,
            entity.timezone,
            entity.timezone_offset,
            newCurrent
        )
    }

}