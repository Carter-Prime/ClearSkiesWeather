package fi.carterm.clearskiesweather.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.adapters.SensorAdapter
import fi.carterm.clearskiesweather.databinding.FragmentHomeBinding
import fi.carterm.clearskiesweather.models.sensors.HumiditySensorReading
import fi.carterm.clearskiesweather.models.sensors.LightSensorReading
import fi.carterm.clearskiesweather.models.sensors.PressureSensorReading
import fi.carterm.clearskiesweather.models.sensors.TemperatureSensorReading
import fi.carterm.clearskiesweather.utilities.managers.PermissionsManager
import fi.carterm.clearskiesweather.viewmodels.HomeViewModel


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sensorAdapter: SensorAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        val viewModel: HomeViewModel by viewModels()
        homeViewModel = viewModel

        PermissionsManager.hasLocationPermissions(requireContext(), requireActivity())
        checkSensors()

        binding.rvSensorDataCards.layoutManager = GridLayoutManager(context, 2)
        sensorAdapter = SensorAdapter {
            onClick(it)
        }
        binding.rvSensorDataCards.adapter = sensorAdapter

        homeViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
            binding.tvCurrentLocation.text = it.address
        }

        homeViewModel.getLatestPhoneSensorData().observe(viewLifecycleOwner){
            if (it != null){
                sensorAdapter.submitList(homeViewModel.createListOfPhoneSensorData(it))
            }
        }

        homeViewModel.openWeatherCall.observe(viewLifecycleOwner){
            Log.d("OneCallWeather", "$it")
            homeViewModel.insertWeather(it)
        }

        homeViewModel.getAllWeatherModel().observe(viewLifecycleOwner){
            Log.d("WeatherModel", "$it")
        }



        homeViewModel.getSensorLiveData().observe(viewLifecycleOwner) {
            if (homeViewModel.getLocationLiveData().value != null) {
                when (it) {
                    is LightSensorReading -> homeViewModel.lightOnChangeSaveToDatabase(it)
                    is TemperatureSensorReading -> homeViewModel.temperatureOnChangeSaveToDatabase(it)
                    is HumiditySensorReading -> homeViewModel.humidityOnChangeSaveToDatabase(it)
                    is PressureSensorReading -> homeViewModel.pressureOnChangeSaveToDatabase(it)
                    else -> Log.d("something", "else")
                }
            }
        }


    }

    private fun onClick(sensorType: String) {
        Log.d("weatherTest", "Data Type $sensorType")
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

}


