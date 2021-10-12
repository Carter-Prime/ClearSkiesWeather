package fi.carterm.clearskiesweather.services.networking

import fi.carterm.clearskiesweather.models.api.OneCallWeather
import fi.carterm.clearskiesweather.models.apiRoomCache.CurrentWeatherModel
import fi.carterm.clearskiesweather.models.apiRoomCache.DailyModel
import fi.carterm.clearskiesweather.services.networking.util.EntityListMapper

class DailyNetworkMapper : EntityListMapper<OneCallWeather, DailyModel> {


    override fun mapListFromEntity(entity: OneCallWeather): List<DailyModel> {
        val daily = entity.daily
        val list = mutableListOf<DailyModel>()
        var id = 1
        daily.forEach {
            val newDaily = DailyModel(
                id,
                it.dt,
                entity.timezone_offset,
                it.sunrise,
                it.sunset,
                it.temp.day,
                it.temp.night,
                it.temp.min,
                it.temp.max,
                it.pressure,
                it.humidity,
                it.dew_point,
                it.wind_speed,
                CurrentWeatherModel(
                    it.weather[0].main,
                    it.weather[0].description,
                    it.weather[0].icon
                ),
                it.clouds,
                it.pop,
                it.uvi
            )
            id++
            list.add(newDaily)
        }
        return list
    }
}