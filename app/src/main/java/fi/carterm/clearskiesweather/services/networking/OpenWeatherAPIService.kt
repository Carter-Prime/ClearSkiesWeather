package fi.carterm.clearskiesweather.services.networking

import fi.carterm.clearskiesweather.models.api.OneCallWeather
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * API Query interface for OpenWeather
 *
 * @author Michael Carter
 * @version 1
 *
 */

interface OpenWeatherAPIService {

    @GET("2.5/onecall?")
    suspend fun getWeatherByLocation(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("exclude") exclude: String = "minutely",
        @Query("units") units: String = "metric"
    ): OneCallWeather

}