package fi.carterm.clearskiesweather.fragments


import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.viewmodels.SensorViewModel
import android.content.pm.PackageManager
import fi.carterm.clearskiesweather.databinding.FragmentGraphBinding


class GraphFragment : Fragment(R.layout.fragment_graph), SensorEventListener {
    private lateinit var binding: FragmentGraphBinding
    //private lateinit var sensorViewModel: SensorViewModel

    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null
    private var pressure: Sensor? = null
    private var temperature: Sensor? = null
    private var humidity: Sensor? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WeatherTest", "Fragment")
        binding = FragmentGraphBinding.bind(view)
//        val viewModel: SensorViewModel by viewModels()
//        sensorViewModel = viewModel
//
//        sensorViewModel.weatherData.observe(viewLifecycleOwner) {
//            Log.d("dbApp", "Weather Data: $it")
//        }
//
//        checkSensors()
//        setUpSensor()

    }

    private fun checkSensors() {
        val sensorLight: TextView = requireActivity().findViewById(R.id.tv_sensor_light)
        val sensorTemperature: TextView = requireActivity().findViewById(R.id.tv_sensor_temp)
        val sensorPressure: TextView = requireActivity().findViewById(R.id.tv_sensor_pressure)
        val sensorHumidity: TextView = requireActivity().findViewById(R.id.tv_sensor_hum)

        val pm: PackageManager = requireActivity().packageManager
        if (!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            Log.d("Sensor missing", "GPS")
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT)) {
            Log.d("Sensor missing", "Light")
            sensorLight.text = "Light sensor not found"
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)) {
            Log.d("Sensor missing", "Termometer")
            sensorTemperature.text = "Temperature sensor not found"
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)) {
            Log.d("Sensor missing", "Humidity")
            sensorHumidity.text = "Relative humidity sensor not found"
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER)) {
            Log.d("Sensor missing", "Barometer")
            sensorPressure.text = "Pressure sensor not found"
        }

    }

    private fun setUpSensor() {
        sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val sensorLight: TextView = requireActivity().findViewById(R.id.tv_sensor_light)
        val sensorTemperature: TextView = requireActivity().findViewById(R.id.tv_sensor_temp)
        val sensorPressure: TextView = requireActivity().findViewById(R.id.tv_sensor_pressure)
        val sensorHumidity: TextView = requireActivity().findViewById(R.id.tv_sensor_hum)

        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light1 = event.values[0]
            sensorLight.text = "Light sensor: $light1 lx"
        }

        if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            val temp1 = event.values[0]
            sensorTemperature.text = "Temperature sensor: $temp1 °C"
        }

        if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
            val press1 = event.values[0]
            sensorPressure.text = "Pressure sensor: $press1 hPa"
        }

        if (event?.sensor?.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            val hum1 = event.values[0]

            sensorHumidity.text = "Relative humidity sensor: $hum1 %"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

//    override fun onResume() {
//        super.onResume()
//        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_UI)
//        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_UI)
//        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI)
//        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_UI)
//    }
}
