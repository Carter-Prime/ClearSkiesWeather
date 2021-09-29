package fi.carterm.clearskiesweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fi.carterm.clearskiesweather.models.WeatherData

@Database(entities = [(WeatherData::class)],
    version = 1)

abstract class WeatherDatabase: RoomDatabase() {

    abstract fun WeatherDataDAO():WeatherDataDAO

    companion object{
        private var instance: WeatherDatabase? = null

        @Synchronized
        fun get(context: Context): WeatherDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    WeatherDatabase::class.java, "weatherData.db")
                    .build()
            }
            return instance!!
        }
    }
}