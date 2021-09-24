package fi.carterm.clearskiesweather.utilities

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Created by Patricie Suppala, 1910042 in 2021.
 */
/*
class SensorDataCollector : SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var pressure: Sensor? = null
    private var temperature: Sensor? = null
    private var humidity: Sensor? = null
    private var light: Sensor? = null

    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    humidity = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    light = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val millibarsOfPressure = event.values[0]
        // Do something with this sensor data.
    }

    sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)

}

*/