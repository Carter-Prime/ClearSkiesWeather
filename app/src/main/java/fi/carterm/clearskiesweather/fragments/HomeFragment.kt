package fi.carterm.clearskiesweather.fragments

import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
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
    private var sensorToggleOn = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.bind(view)
        val viewModel: HomeViewModel by viewModels()
        homeViewModel = viewModel
        sensorToggleOn = getState()

        PermissionsManager.hasLocationPermissions(requireContext(), requireActivity())
        checkSensors()
        if (sensorToggleOn) {
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                broadcastReceiver, IntentFilter(
                    SensorService.KEY_ON_SENSOR_CHANGED_ACTION
                )
            )
        }

        val mLayoutManager = GridLayoutManager(context, 2)
        mLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (sensorAdapter.getItemViewType(position)) {
                    0 -> 2
                    1 -> 1
                    else -> 1
                }
            }
        }

        binding.rvSensorDataCards.layoutManager = mLayoutManager
        sensorAdapter = SensorAdapter {
            onClick(it)
        }
        binding.rvSensorDataCards.adapter = sensorAdapter

        homeViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
            binding.tvCurrentLocation.text = it.address
        }

        homeViewModel.getLatestPhoneSensorData().observe(viewLifecycleOwner) {
            if (it != null && sensorToggleOn) {
                sensorAdapter.addHeaderAndSubmitList(homeViewModel.createListOfPhoneSensorData(it))
            }
        }

        homeViewModel.openWeatherCall.observe(viewLifecycleOwner) {
            Log.d("OneCallWeather", "$it")
            homeViewModel.insertWeather(it)
        }

        homeViewModel.getLatestWeather().observe(viewLifecycleOwner) {
            if (it != null && !sensorToggleOn) {
                sensorAdapter.addHeaderAndSubmitList(homeViewModel.createListOfCurrentWeatherData(it))
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_right_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_toggle_sensors -> {
                if (sensorToggleOn) {
                    stopService()
                    sensorAdapter.addHeaderAndSubmitList(homeViewModel.getLatestWeather().value?.let {
                        homeViewModel.createListOfCurrentWeatherData(
                            it
                        )
                    })
                } else {
                    startService()
                }
                saveState()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun stopService() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        requireContext().stopService(Intent(activity, SensorService::class.java))


    }

    private fun startService() {
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver, IntentFilter(
                SensorService.KEY_ON_SENSOR_CHANGED_ACTION
            )
        )
        startForegroundServiceForSensors(false)
    }

    private fun saveState() {
        sensorToggleOn = !sensorToggleOn
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean("toggleSensorOn", sensorToggleOn)
            apply()
        }
    }

    private fun getState(): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref!!.getBoolean("toggleSensorOn", true)
    }

    override fun onResume() {
        super.onResume()
        if (sensorToggleOn) {
            startForegroundServiceForSensors(false)
        }
    }

    private fun startForegroundServiceForSensors(background: Boolean) {
        val sensorServiceIntent = Intent(activity, SensorService::class.java)
        sensorServiceIntent.putExtra(SensorService.KEY_BACKGROUND, background)
        requireActivity().startForegroundService(sensorServiceIntent)
    }

    override fun onPause() {
        super.onPause()
        if (sensorToggleOn) {
            startForegroundServiceForSensors(true)
        }
    }

    override fun onDestroy() {
        if (sensorToggleOn) {
            LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(broadcastReceiver)
        }
        super.onDestroy()
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            homeViewModel.lightOnChangeSaveToDatabase(
                intent.getFloatExtra(
                    SensorService.KEY_LIGHT,
                    -1000f
                )
            )
            homeViewModel.temperatureOnChangeSaveToDatabase(
                intent.getFloatExtra(
                    SensorService.KEY_TEMP,
                    -1000f
                )
            )
            homeViewModel.pressureOnChangeSaveToDatabase(
                intent.getFloatExtra(
                    SensorService.KEY_PRESSURE,
                    -1000f
                )
            )
            homeViewModel.humidityOnChangeSaveToDatabase(
                intent.getFloatExtra(
                    SensorService.KEY_HUMIDITY,
                    -1000f
                )
            )
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


