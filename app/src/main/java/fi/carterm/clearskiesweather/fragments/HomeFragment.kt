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
import fi.carterm.clearskiesweather.models.sensors.LightSensorReading
import fi.carterm.clearskiesweather.models.sensors.TemperatureSensorReading
import fi.carterm.clearskiesweather.utilities.PermissionsManager
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
        sensorAdapter.submitList(homeViewModel.data)

        homeViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
        }

        homeViewModel.getLastLight().observe(viewLifecycleOwner){
            homeViewModel.data[2].sensorReading = it.sensorReading!!
            sensorAdapter.submitList(homeViewModel.data)
            sensorAdapter.notifyDataSetChanged()
        }

        homeViewModel.getLastTemp().observe(viewLifecycleOwner){
            homeViewModel.data[0].sensorReading = it.sensorReading!!
            sensorAdapter.submitList(homeViewModel.data)
            sensorAdapter.notifyDataSetChanged()
        }

        homeViewModel.getSensorLiveData().observe(viewLifecycleOwner) {
            if (homeViewModel.getLocationLiveData().value != null) {
                when (it) {
                    is LightSensorReading -> {
                        homeViewModel.lightOnChangeSaveToDatabase(it)
                        homeViewModel.data[2].sensorReading = it.sensorReading!!
                        sensorAdapter.submitList(homeViewModel.data)
                        sensorAdapter.notifyDataSetChanged()
                    }
                    is TemperatureSensorReading -> {
                        homeViewModel.temperatureOnChangeSaveToDatabase(it)
                        homeViewModel.data[0].sensorReading = it.sensorReading!!
                        sensorAdapter.submitList(homeViewModel.data)
                        sensorAdapter.notifyDataSetChanged()
                    }
                    else -> Log.d("something", "else")
//                    is HumiditySensorReading -> TODO()
//                    is PressureSensorReading -> TODO()
//                    is SensorReading -> TODO()
                }
            }

        }

        homeViewModel.sensorLightReadings.observe(viewLifecycleOwner) {
                Log.d("testingRoom", "Sensor Data from Room: $it")
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

}


