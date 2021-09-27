package fi.carterm.clearskiesweather.fragments


import android.Manifest
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
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import fi.carterm.clearskiesweather.databinding.FragmentGraphBinding
import kotlin.math.exp
import kotlin.math.ln


class GraphFragment : Fragment(R.layout.fragment_graph), SensorEventListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: FragmentGraphBinding
    private lateinit var sensorViewModel: SensorViewModel

    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null
    private var pressure: Sensor? = null
    private var temperature: Sensor? = null
    private var humidity: Sensor? = null

    private var temp1 = 0.0f
    private var light1 = 0.0f
    private var press1 = 0.0f
    private var hum1 = 0.0f
    private var dewPoint = 0.0f
    private var absHumidity = 0.0f
    private var latitude = 0.0
    private var longitude = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WeatherTest", "Fragment")
        binding = FragmentGraphBinding.bind(view)
        val viewModel: SensorViewModel by viewModels()
        sensorViewModel = viewModel

        sensorViewModel.weatherData.observe(viewLifecycleOwner) {
            Log.d("dbApp", "Weather Data: $it")
        }

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity?.applicationContext)
        if (activity?.applicationContext?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && activity?.applicationContext?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        )

            checkSensors()
        setUpSensor()

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
        val timestamp = System.currentTimeMillis()

        getLastLocation()

        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            light1 = event.values[0]

            dataToRoom(timestamp)
            sensorLight.text = "Light sensor: $light1 lx"
        }

        if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temp1 = event.values[0]
            dewPoint =
                (243.12 * (ln(hum1 / 100.0) + 17.62 * temp1 / (243.12 + temp1)) / (17.62 - ln(
                    hum1 / 100.0
                ) - 17.62 * temp1 / (243.12 + temp1))).toFloat()
            absHumidity =
                (((216.7 * (hum1 / 100) * 6.112 * exp((17.62 * temp1) / (243.12 + temp1))) / (273.15 + temp1)).toFloat())
            dataToRoom(timestamp)
            sensorTemperature.text = "Temperature sensor: $temp1 °C"
        }

        if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
            press1 = event.values[0]
            dataToRoom(timestamp)
            sensorPressure.text = "Pressure sensor: $press1 hPa"
            Log.d("Getting location 2", latitude.toString())
        }

        if (event?.sensor?.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            hum1 = event.values[0]
            dewPoint =
                (243.12 * (ln(hum1 / 100.0) + 17.62 * temp1 / (243.12 + temp1)) / (17.62 - ln(
                    hum1 / 100.0
                ) - 17.62 * temp1 / (243.12 + temp1))).toFloat()
            absHumidity =
                (((216.7 * (hum1 / 100) * 6.112 * exp((17.62 * temp1) / (243.12 + temp1))) / (273.15 + temp1)).toFloat())

            dataToRoom(timestamp)
            sensorHumidity.text = "Relative humidity sensor: $hum1 %"
        }
    }

    private fun dataToRoom(timestamp: Long) {
        sensorViewModel.insertWeather(
            timestamp,
            temp1,
            hum1,
            press1,
            light1,
            absHumidity,
            dewPoint,
            latitude,
            longitude,
        )
    }

    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                latitude = task.result!!.latitude
                longitude = task.result!!.longitude
                Log.d("Getting location ", latitude.toString())
            } else {
                Log.d("Getting location problem", "getLastLocation:exception", task.exception)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_UI)
    }
}
