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
import fi.carterm.clearskiesweather.databinding.FragmentHomeBinding
import fi.carterm.clearskiesweather.viewmodels.HomeViewModel

class HomeFragment: Fragment(R.layout.fragment_home), SensorEventListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null
    private var pressure: Sensor? = null
    private var temperature: Sensor? = null
    private var humidity: Sensor? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        val viewModel : HomeViewModel by viewModels()
        homeViewModel = viewModel

        homeViewModel.weatherData.observe(viewLifecycleOwner){
            Log.d("dbApp", "Weather Data: $it")
        }

        setUpSensor()
    }
    private fun setUpSensor() {
        sensorManager = activity!!.getSystemService(SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light1 = event.values[0]
            val sensorLight: TextView = activity!!.findViewById(R.id.tv_sensor_light)
            sensorLight.text =  "Light sensor: $light1 lx"
        }
        if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            val temp1 = event.values[0]
            val sensorTemperature: TextView = activity!!.findViewById(R.id.tv_sensor_temp)
            sensorTemperature.text =  "Temperature sensor: $temp1 °C"
        }
        if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
            val press1 = event.values[0]
            val sensorPressure: TextView = activity!!.findViewById(R.id.tv_sensor_pressure)
            sensorPressure.text =  "Pressure sensor: $press1 hPa"
        }
        if (event?.sensor?.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            val hum1 = event.values[0]
            val sensorHumidity: TextView = activity!!.findViewById(R.id.tv_sensor_light)
            sensorHumidity.text =  "Relative humidity sensor: $hum1 %"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        // Register a listener for the sensor.
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_UI)
    }
}
