package fi.carterm.clearskiesweather.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.adapters.SensorAdapter
import fi.carterm.clearskiesweather.databinding.FragmentHomeBinding
import fi.carterm.clearskiesweather.services.background.SensorService
import fi.carterm.clearskiesweather.utilities.WeatherApplication
import fi.carterm.clearskiesweather.utilities.managers.PermissionsManager
import fi.carterm.clearskiesweather.viewmodels.HomeViewModel


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sensorAdapter: SensorAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var app: WeatherApplication


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentHomeBinding.bind(view)
        val viewModel: HomeViewModel by activityViewModels()
        homeViewModel = viewModel
        app = activity?.application as WeatherApplication

        PermissionsManager.hasLocationPermissions(requireContext(), requireActivity())
        checkSensors()


        if (app.onPhoneSensors) {
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
        sensorAdapter = SensorAdapter(requireActivity().application) {
            onClick(it)
        }
        binding.rvSensorDataCards.adapter = sensorAdapter
        binding.rvSensorDataCards.itemAnimator = null

        homeViewModel.otherLocationWeather.observe(viewLifecycleOwner) {
            Log.d("other", "location weather observer: $it")
            homeViewModel.insertWeather(it)
        }

        homeViewModel.getLocationError().observe(viewLifecycleOwner) {
            Log.d("other", "location error observer: $it")
        }

        homeViewModel.getLatestPhoneSensorData().observe(viewLifecycleOwner) {
            if (it != null && app.onPhoneSensors) {
                sensorAdapter.addHeaderAndSubmitList(
                    homeViewModel.createListOfPhoneSensorData(it),
                    app.onPhoneSensors
                )
            }
        }

        homeViewModel.getLatestWeather().observe(viewLifecycleOwner) {
            if (it != null && !app.onPhoneSensors) {
                sensorAdapter.addHeaderAndSubmitList(
                    homeViewModel.createListOfCurrentWeatherData(it),
                    app.onPhoneSensors
                )
            }
        }

        homeViewModel.getLocationLiveData().observe(viewLifecycleOwner) {
            binding.tvCurrentLocation.text = if (homeViewModel.useCurrentLocation) {
                it.address
            } else {
                homeViewModel.getOtherLocation().value?.address
            }
        }

        homeViewModel.weather.observe(viewLifecycleOwner) {
            homeViewModel.insertWeather(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_right_menu, menu)
        menu.findItem(R.id.btn_toggle_sensors).tooltipText =
            getString(R.string.tooltip_text_toggle_phone_sensors)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_toggle_sensors -> {
                if (app.onPhoneSensors) {
                    stopService()
                    sensorAdapter.addHeaderAndSubmitList(homeViewModel.getLatestWeather().value?.let {
                        homeViewModel.createListOfCurrentWeatherData(
                            it
                        )
                    }, !app.onPhoneSensors)
                } else {
                    startService()
                }
                saveOnSensorState()
                true
            }
            R.id.btn_settings_menu -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
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

    private fun saveOnSensorState() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        with(sharedPref.edit()) {
            putBoolean("toggleSensorOn", !app.onPhoneSensors)
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        if (app.onPhoneSensors) {
            startForegroundServiceForSensors(false)
        }
        checkSensors()
    }

    private fun startForegroundServiceForSensors(background: Boolean) {
        val sensorServiceIntent = Intent(activity, SensorService::class.java)
        sensorServiceIntent.putExtra(SensorService.KEY_BACKGROUND, background)
        requireActivity().startForegroundService(sensorServiceIntent)
    }

    override fun onPause() {
        super.onPause()
        if (app.onPhoneSensors) {
            startForegroundServiceForSensors(true)
        }
        checkSensors()
    }

    override fun onDestroy() {
        if (app.onPhoneSensors) {
            LocalBroadcastManager.getInstance(requireContext())
                .unregisterReceiver(broadcastReceiver)
        }
        checkSensors()
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
        val bundle = Bundle()
        bundle.putString("sensorType", sensorType)
        findNavController().navigate(R.id.action_homeFragment_to_graphFragment, bundle)
    }

    private fun checkSensors() {

        val pm: PackageManager = requireActivity().packageManager
        if (!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            homeViewModel.gpsSensor = false
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT)) {
            homeViewModel.lightSensor = false
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)) {
            homeViewModel.tempSensor = false
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)) {
            homeViewModel.humiditySensor = false
        }
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER)) {
            homeViewModel.pressureSensor = false
        }
    }

}


