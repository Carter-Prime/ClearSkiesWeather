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

    private class WeatherDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let { database ->
                scope.launch {
                    val weatherDataDao = database.WeatherDataDAO()

                   /* val weatherOne = WeatherData(0, 13.2, 45.2, 75.0, 1.0)
                    var id = weatherDataDao.insert(weatherOne)
                    Log.d("dbApp", "database init weather one $id")
                    val weatherTwo = WeatherData(0, 15.2, 23.2, 78.0, 4.0)
                    id = weatherDataDao.insert(weatherTwo)
                    Log.d("dbApp", "database init weather two $id")
                    val weatherThree = WeatherData(0, 3.2, 65.2, 79.0, 14.0)
                    id = weatherDataDao.insert(weatherThree)
                    Log.d("dbApp", "database init weather three $id")
                    Log.d("dbApp", "database init over")*/

                }
            }
        }
    }

    companion object{
        private var instance: WeatherDatabase? = null

        @Synchronized
        fun get(context: Context, scope: CoroutineScope): WeatherDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    WeatherDatabase::class.java, "weatherData.db")
                    .addCallback(WeatherDatabaseCallback(scope))
                    .build()
            }
            return instance!!
        }
    }
}