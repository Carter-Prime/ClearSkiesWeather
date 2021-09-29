package fi.carterm.clearskiesweather.utilities

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import fi.carterm.clearskiesweather.models.SensorReading

class SensorLiveData( var application: Application): LiveData<SensorReading>(), SensorEventListener {

    private val sensorManager
        get() = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private var brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private var temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    private var pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    private var humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

    private var temperatureReading: Float = 0.0f
    private var pressureReading: Float = 0.0f
    private var lightReading: Float = 0.0f
    private var relativeHumidityReading: Float = 0.0f


    override fun onActive() {
        super.onActive()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onInactive() {
        super.onInactive()
        sensorManager.unregisterListener(this, brightness)
        sensorManager.unregisterListener(this, temperature)
        sensorManager.unregisterListener(this, pressure)
        sensorManager.unregisterListener(this, humidity)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when(event?.sensor?.type){
            Sensor.TYPE_LIGHT -> {
                lightReading = event.values[0]

            }
            Sensor.TYPE_AMBIENT_TEMPERATURE ->{
                temperatureReading = event.values[0]
            }
            Sensor.TYPE_PRESSURE ->{
                pressureReading = event.values[0]
            }
            Sensor.TYPE_RELATIVE_HUMIDITY ->{
                relativeHumidityReading = event.values[0]
            }
        }
        value = SensorReading(temperatureReading, pressureReading, lightReading, relativeHumidityReading)
        value!!.calculateValues()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }


}