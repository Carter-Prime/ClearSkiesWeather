package fi.carterm.clearskiesweather.services.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenWeatherRetrofitFactory {
    private const val BASE_URL = "https://api.openweathermap.org/data/"

    private val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val SERVICE: OpenWeatherAPIService = retrofit.create(OpenWeatherAPIService::class.java)
}