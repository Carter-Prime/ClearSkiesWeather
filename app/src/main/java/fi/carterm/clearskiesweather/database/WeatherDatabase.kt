package fi.carterm.clearskiesweather.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import fi.carterm.clearskiesweather.models.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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