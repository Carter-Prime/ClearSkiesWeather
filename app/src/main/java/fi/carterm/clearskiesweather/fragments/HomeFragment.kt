package fi.carterm.clearskiesweather.fragments


import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.adapters.SensorAdapter
import fi.carterm.clearskiesweather.databinding.FragmentHomeBinding
import fi.carterm.clearskiesweather.utilities.PermissionsManager
import fi.carterm.clearskiesweather.viewmodels.HomeViewModel



class HomeFragment: Fragment(R.layout.fragment_home), SensorEventListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sensorAdapter: SensorAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null
    private var pressure: Sensor? = null
    private var temperature: Sensor? = null
    private var humidity: Sensor? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        val viewModel: HomeViewModel by viewModels()
        homeViewModel = viewModel

        PermissionsManager.hasLocationPermissions(requireContext(), requireActivity())
        checkSensors()
        setUpSensor()

        binding.rvSensorDataCards.layoutManager = GridLayoutManager(context, 2)
        sensorAdapter = SensorAdapter{
            onClick(it)
        }
        binding.rvSensorDataCards.adapter = sensorAdapter
        sensorAdapter.submitList(homeViewModel.sensorArray)

        homeViewModel.getLocationLiveData().observe(viewLifecycleOwner){
            Log.d("weatherTest", "Location Data $it")
        }

    }

    private fun onClick(position: Int) {
        Log.d("weatherTest", "Position $position")
    }

    private fun checkSensors() {

        val pm: PackageManager = requireActivity().packageManager
        if (!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            Log.d("Sensor missing", "GPS")
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT)) {
            Log.d("Sensor missing", "Light")

        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)) {
            Log.d("Sensor missing", "Termometer")

        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)) {
            Log.d("Sensor missing", "Humidity")
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER)) {
            Log.d("Sensor missing", "Barometer")
        }
    }

    private fun setUpSensor() {
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        humidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        when(event?.sensor?.type){
            Sensor.TYPE_LIGHT -> {
                homeViewModel.light = event.values[0]
                homeViewModel.saveToDatabase()
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE ->{
                homeViewModel.temperature = event.values[0]
                homeViewModel.saveToDatabase()
            }
            Sensor.TYPE_PRESSURE ->{
                homeViewModel.pressure = event.values[0]
                homeViewModel.saveToDatabase()
            }
            Sensor.TYPE_RELATIVE_HUMIDITY ->{
                homeViewModel.relativeHumidity = event.values[0]
                homeViewModel.saveToDatabase()
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


