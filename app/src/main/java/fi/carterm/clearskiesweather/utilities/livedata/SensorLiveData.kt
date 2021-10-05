package fi.carterm.clearskiesweather.utilities.livedata

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.models.sensors.HumiditySensorReading
import fi.carterm.clearskiesweather.models.sensors.LightSensorReading
import fi.carterm.clearskiesweather.models.sensors.PressureSensorReading
import fi.carterm.clearskiesweather.models.sensors.TemperatureSensorReading

class SensorLiveData( var application: Application): LiveData<Any>(), SensorEventListener {

    private val sensorManager
        get() = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private var brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private var temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    private var pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    private var humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)



    override fun onActive() {
        super.onActive()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onInactive() {
        super.onInactive()
        sensorManager.unregisterListener(this, brightness)
        sensorManager.unregisterListener(this, temperature)
        sensorManager.unregisterListener(this, pressure)
        sensorManager.unregisterListener(this, humidity)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        value = when(event?.sensor?.type){
            Sensor.TYPE_LIGHT -> {
                LightSensorReading(0, System.currentTimeMillis(), event.values[0])
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE ->{
                TemperatureSensorReading(0, System.currentTimeMillis(), event.values[0])
            }
            Sensor.TYPE_PRESSURE ->{
                PressureSensorReading(0, System.currentTimeMillis(), event.values[0])
            }
            Sensor.TYPE_RELATIVE_HUMIDITY ->{
                HumiditySensorReading(0, System.currentTimeMillis(), event.values[0])
            }
            else -> null
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }


}