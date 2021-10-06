package fi.carterm.clearskiesweather.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.adapters.SensorAdapter
import fi.carterm.clearskiesweather.databinding.FragmentHomeBinding
import fi.carterm.clearskiesweather.services.background.SensorService
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

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver,  IntentFilter(
            SensorService.KEY_ON_SENSOR_CHANGED_ACTION))

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

    }

    override fun onResume() {
        super.onResume()
        startForegroundServiceForSensors(false)
    }

    private fun startForegroundServiceForSensors(background: Boolean) {
        val sensorServiceIntent = Intent(activity, SensorService::class.java)
        sensorServiceIntent.putExtra(SensorService.KEY_BACKGROUND, background)
        requireActivity().startForegroundService(sensorServiceIntent)
    }

    override fun onPause() {
        super.onPause()
        startForegroundServiceForSensors(true)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
             homeViewModel.lightOnChangeSaveToDatabase(intent.getFloatExtra(SensorService.KEY_LIGHT, -1000f))
             homeViewModel.temperatureOnChangeSaveToDatabase(intent.getFloatExtra(SensorService.KEY_TEMP, -1000f))
             homeViewModel.pressureOnChangeSaveToDatabase(intent.getFloatExtra(SensorService.KEY_PRESSURE, -1000f))
             homeViewModel.humidityOnChangeSaveToDatabase(intent.getFloatExtra(SensorService.KEY_HUMIDITY, -1000f))
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
            Log.d("Sensor missing", "Thermometer")

        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)) {
            Log.d("Sensor missing", "Humidity")
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER)) {
            Log.d("Sensor missing", "Barometer")
        }
    }

}


